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

package au.com.ps4impact.madcow.report

import au.com.ps4impact.madcow.MadcowProject
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.MadcowTestCaseException
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.util.VersionUtil
import groovy.json.JsonOutput
import org.apache.log4j.Logger
import org.apache.commons.io.FileUtils
import java.text.DecimalFormat
import au.com.ps4impact.madcow.MadcowTestSuite
import groovy.text.GStringTemplateEngine

/**
 * Madcow Execution Report.
 *
 * @author Gavin Bunney
 */
class MadcowExecutionReport implements IMadcowReport {

    protected static final Logger LOG = Logger.getLogger(MadcowExecutionReport.class);
    protected static final DecimalFormat TIME_SECONDS_FORMAT = new DecimalFormat("########.###");

    public void prepareReportDirectory() {

        if (new File(MadcowProject.MADCOW_REPORT_DIRECTORY).exists()) {
            new File(MadcowProject.MADCOW_REPORT_DIRECTORY).deleteDir();
        }

        new File(MadcowProject.MADCOW_REPORT_DIRECTORY).mkdirs();
        new File(MadcowProject.MADCOW_TESTCASE_REPORT_DIRECTORY).mkdirs();

        // copy the report assets
        File assetsDir = new File('./.madcow/report');
        if (assetsDir.exists()) {
            LOG.info("Preparing assets for Madcow Report...");
            FileUtils.copyDirectory(assetsDir, new File("${MadcowProject.MADCOW_REPORT_DIRECTORY}/"));
        } else {
            LOG.warn("No assets found for Madcow Report...");
        }
    }

    /**
     * Create the result files.
     */
    public void createTestCaseReport(MadcowTestCase testCase) {
        // nothing... yet - suite report handles all this now
    }

    /**
     * Creates an error test case report
     *
     * @param testName the test name
     * @param parsedException the exception that was thrown
     */
    public void createErrorTestCaseReport(String testName, Throwable parsedException) {
        // nothing... yet - suite report handles all this now
    }

    /**
     * Create Test Suite level report.
     */
    public void createTestSuiteReport(MadcowTestSuite testSuite) {

        def allTestCases = testSuite.getTestCasesRecusively();

        int passedCount = 0;
        int failedCount = 0;
        int errorCount  = 0;
        int skippedCount = 0;
        Long totalTime = 0L;
        allTestCases.each { testCase ->

            if (testCase instanceof MadcowTestCaseException) {
                errorCount++;
            } else if (testCase.ignoreTestCase) {
                skippedCount++;
            } else if (testCase.lastExecutedStep == null
                       || testCase.lastExecutedStep.result?.failed()) {
                failedCount++;
            } else {
                passedCount++;
            }

            if (testCase?.lastExecutedStep != null
                && !testCase.lastExecutedStep.result?.parseError())
                totalTime += testCase.stopWatch.time;
        }

        try {
            def resultJSON = [
                    passedCount: passedCount,
                    errorCount: errorCount,
                    failedCount: failedCount,
                    skippedCount: skippedCount,
                    timestamp: new Date().time,
                    timestampFormatted: new Date().format('dd MMM yyyy HH:mm:ss'),
                    totalTime: TIME_SECONDS_FORMAT.format(totalTime > 0 ? (totalTime / 1000) : 0),
                    totalTimeExec: TIME_SECONDS_FORMAT.format(totalTime > 0 ? (testSuite.stopWatch.time / 1000) : 0),
                    madcowVersion: VersionUtil.getVersionString(),
                    config: MadcowConfig.SHARED_CONFIG.toJSON(),
                    results: testSuite.toJSON()
            ];

            def engine = new GStringTemplateEngine();
            def templateEngine = engine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'result-js.gtemplate').URL);
            def template = templateEngine.make([resultJSON: JsonOutput.prettyPrint(JsonOutput.toJson(resultJSON))]);
            String templateContents = template.toString();
            def result = new File("${MadcowProject.MADCOW_TESTCASE_REPORT_DIRECTORY}/results.js");
            result.write(templateContents);
        } catch (e) {
            LOG.error("Error creating the Madcow Test Suite Results: $e");
        }
    }
}