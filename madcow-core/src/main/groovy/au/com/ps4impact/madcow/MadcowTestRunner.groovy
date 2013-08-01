/*
 * Copyright 2012 4impact, Brisbane, Australia
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.execution.SingleTestCaseRunner
import au.com.ps4impact.madcow.logging.MadcowLog
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.log4j.Logger
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.util.PathFormatter
import fj.Effect
import fj.data.Option
import fj.Unit
import fj.control.parallel.Actor
import fj.control.parallel.Strategy
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import au.com.ps4impact.madcow.execution.ParallelTestCaseRunner

import au.com.ps4impact.madcow.report.JUnitMadcowReport
import au.com.ps4impact.madcow.report.MadcowExecutionReport
import au.com.ps4impact.madcow.report.IMadcowReport
import java.util.concurrent.atomic.AtomicInteger

/**
 * Madcow Test Coordinator class.
 *
 * @author Gavin Bunney
 */
class MadcowTestRunner {

    protected static final Logger LOG = Logger.getLogger(MadcowTestRunner.class);

    protected static List<IMadcowReport> reporters = [new MadcowExecutionReport(), new JUnitMadcowReport()];

    /**
     * Prep the results directory, but removing it
     * and creating folders ready for results!
     */
    protected static void prepareResultsDirectory() {
        
        if (new File(MadcowProject.RESULTS_DIRECTORY).exists())
            new File(MadcowProject.RESULTS_DIRECTORY).deleteDir();

        new File(MadcowProject.RESULTS_DIRECTORY).mkdir();

        reporters.each() { reporter -> reporter.prepareReportDirectory() };
    }

    /**
     * Main entry point to execute all the given tests.
     */
    static void executeTests(ArrayList<String> testNames = [], MadcowConfig madcowConfig) {

        prepareResultsDirectory();

        MadcowTestSuite rootTestSuite = prepareTestSuite(testNames, madcowConfig);
        int numberTestsToRun = rootTestSuite.size();

        LOG.info("Found ${numberTestsToRun} test cases to run");
        def exceptions = [];
        AtomicInteger numberOfTestsRan = new AtomicInteger(0);
        def allTestCases = rootTestSuite.getTestCasesRecusively();

        rootTestSuite.stopWatch.start();


        ExecutorService pool
        Strategy<Unit> strategy

        if (!madcowConfig.parallel) {
            LOG.info "Running in Single Execution mode..."
            pool = Executors.newSingleThreadExecutor();
            //strategy = Strategy.executorStrategy(pool);
            strategy = Strategy.seqStrategy()

        } else {
            LOG.info "Running in Parallel Execution mode..."
            pool = Executors.newFixedThreadPool(madcowConfig.threads);
            strategy = Strategy.executorStrategy(pool);
        }

        def callback = Actor.queueActor(strategy, {Option<Exception> result ->
            numberOfTestsRan.andIncrement;
            result.foreach({Exception e -> exceptions.add(e)} as Effect)
            if (numberOfTestsRan.get() >= numberTestsToRun) {
                pool.shutdown()
            }
        } as Effect);

        def parallelTestCaseRunner = new ParallelTestCaseRunner(strategy, callback)

        allTestCases.each { MadcowTestCase testCase ->
            parallelTestCaseRunner.act(fj.P.p(testCase, reporters));
        }


        while (numberOfTestsRan.get() < numberTestsToRun) {
            Thread.sleep(500);
        }

        while (madcowConfig.retryCount > 0) {
            def failed = allTestCases.count { MadcowTestCase testCase ->
                testCase.lastExecutedStep.result.failed()
            }
            def exception = allTestCases.findAll { MadcowTestCase testCase ->
                testCase instanceof MadcowTestCaseException
            }
            LOG.info "Attempting retry number ${madcowConfig.retryCount} of ${exception?.size()} errored tests..."

            exception.each { MadcowTestCase testCase ->
                MadcowTestCase matchingTest = allTestCases.find{ it.name.equals(testCase.name) }
                new SingleTestCaseRunner(matchingTest, reporters)
            }

            madcowConfig.retryCount--
        }

        rootTestSuite.stopWatch.stop();
        reporters.each() { reporter -> reporter.createTestSuiteReport(rootTestSuite) };
    }

    /**
     * Create the test suite collection for the given tests.
     */
    public static MadcowTestSuite prepareTestSuite(ArrayList<String> testNames = [], MadcowConfig madcowConfig) {

        ArrayList<File> testFilesToRun = new ArrayList<File>();

        if (testNames == null || testNames.empty) {
            testFilesToRun.addAll(ResourceFinder.locateFilesOnClasspath(this.classLoader, "**/*.grass", MadcowProject.TESTS_DIRECTORY));
        } else {
            testNames.each { String testName ->
                def filename = ResourceFinder.addFileExtensionIfRequired(testName, '.grass');
                testFilesToRun.add(ResourceFinder.locateFileOnClasspath(this.classLoader, "**/${filename}", MadcowProject.TESTS_DIRECTORY));
            }
        }

        if (testFilesToRun.empty) {
            LOG.error('No tests found to execute');
            throw new RuntimeException('No tests found to execute');
        }

        MadcowTestSuite rootSuite = new MadcowTestSuite('');
        walkDirectoryTree(new File(MadcowProject.TESTS_DIRECTORY), rootSuite);

        testFilesToRun.each { File testFile ->

            String testName = StringUtils.removeEnd(PathFormatter.formatPathToPackage(testFile.canonicalPath, new File(MadcowProject.TESTS_DIRECTORY).canonicalPath), '.grass');
            MadcowTestCase testCase;
            try {
                testCase = new MadcowTestCase(testName, madcowConfig, testFile.readLines() as ArrayList<String>);
            } catch (Exception error) {
                testCase = new MadcowTestCaseException(testName, madcowConfig, testFile.readLines() as ArrayList<String>, error);
            }
            MadcowTestSuite suite = rootSuite.locateSuite(testName) ?: rootSuite;
            testCase.testSuite = suite;
            suite.testCases.add(testCase);
        }
        return rootSuite;
    }


    protected static void walkDirectoryTree(File directory, MadcowTestSuite parentSuite) {

        if (!directory.exists())
            return;

        directory.eachDir { subdir ->
            MadcowTestSuite subdirSuite = new MadcowTestSuite(subdir.name, parentSuite);
            parentSuite.children.add(subdirSuite);
            walkDirectoryTree(subdir, subdirSuite);
        }
    }
}
