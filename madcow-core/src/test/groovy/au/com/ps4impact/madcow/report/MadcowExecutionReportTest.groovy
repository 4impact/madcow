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

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.MadcowTestCaseException
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.time.StopWatch
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestSuite
import au.com.ps4impact.madcow.MadcowProject

/**
 * Test for the MadcowExecutionReport class.
 *
 * @author Gavin Bunney
 */
class MadcowExecutionReportTest extends GroovyTestCase {

    MadcowExecutionReport madcowReport = new MadcowExecutionReport();

    protected void validateMadcowTestCaseReport(boolean passed, boolean failed, boolean parseError) {
        madcowReport.prepareReportDirectory();

        def blade = new GrassBlade();
        blade.line = 'tent';

        MadcowTestCase testCase = new MadcowTestCase('testTestCaseReportForPass', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, blade, null));
        testCase.steps.first().result = passed ? MadcowStepResult.PASS() : failed ? MadcowStepResult.FAIL('Some error') : MadcowStepResult.PARSE_ERROR('Unsupported operation');
        testCase.steps.first().blade.type = GrassBlade.GrassBladeType.IMPORT;
        testCase.steps.first().children.add(new MadcowStep(testCase, blade, testCase.steps.first()));
        testCase.steps.first().children.first().result = passed ? MadcowStepResult.PASS() : failed ? MadcowStepResult.FAIL('Some error') : MadcowStepResult.PARSE_ERROR('Unsupported operation');
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();
        testCase.reportDetails.put('Madcow Website Link', '<a href="http://madcow.4impact.net.au">Click Here</a>');

        madcowReport.createTestCaseReport(testCase);
        def result = new File(testCase.getResultDirectory().path + "/index.html");
        String resultHtml = result.text;

        assertTrue(resultHtml.contains(testCase.name));
        assertTrue(resultHtml.contains(blade.line));
        assertEquals(failed, resultHtml.contains('Test Failed'));
        assertEquals(parseError, resultHtml.contains('Test Parse Error'));
    }

    public void testTestCaseReportForPass() {
        validateMadcowTestCaseReport(true, false, false);
    }

    public void testTestCaseReportForFailure() {
        validateMadcowTestCaseReport(false, true, false);
    }

    public void testTestCaseReportForParseError() {
        validateMadcowTestCaseReport(false, false, true);
    }

    public void testTestSuiteReport() {
        madcowReport.prepareReportDirectory();

        def blade = new GrassBlade();
        blade.line = 'tent';

        MadcowTestCase testCase = new MadcowTestCase('testTestSuiteReport01', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, blade, null));
        testCase.steps.first().result = MadcowStepResult.PASS();
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        MadcowTestCase testCase2 = new MadcowTestCase('testTestSuiteReport02', MockMadcowConfig.getMadcowConfig(true))
        testCase2.steps.add(new MadcowStep(testCase2, blade, null));
        testCase2.steps.first().result = MadcowStepResult.FAIL('Some error occured');
        testCase2.stopWatch = new StopWatch();
        testCase2.stopWatch.start();
        testCase2.stopWatch.stop();
        testCase2.lastExecutedStep = testCase2.steps.first();

        MadcowTestSuite suite = new MadcowTestSuite('', null, [testCase, testCase2]);

        madcowReport.createTestCaseReport(testCase);
        madcowReport.createTestCaseReport(testCase2);
        madcowReport.createTestSuiteReport(suite);

        def suiteReportFile = new File(MadcowProject.MADCOW_REPORT_DIRECTORY + '/index.html');
        String suiteReportFileText = suiteReportFile.text;

        assertTrue(suiteReportFile.exists())
        assertTrue(suiteReportFileText.contains('1 passed'))
        assertTrue(suiteReportFileText.contains('1 failed'))
        assertTrue(suiteReportFileText.contains(testCase.name))
        assertTrue(suiteReportFileText.contains(testCase2.name))
    }

    public void testTestSuiteZeroDivisionReport() {
        madcowReport.prepareReportDirectory();

        def blade = new GrassBlade();
        blade.line = 'tent';

        MadcowTestCase testCase = new MadcowTestCase('testTestSuiteReport01', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, blade, null));
        testCase.steps.first().result = MadcowStepResult.FAIL("This one will break....");
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        MadcowTestSuite suite = new MadcowTestSuite('', null, [testCase]);

        madcowReport.createTestCaseReport(testCase);
        madcowReport.createTestSuiteReport(suite);

        def suiteReportFile = new File(MadcowProject.MADCOW_REPORT_DIRECTORY + '/index.html');
        String suiteReportFileText = suiteReportFile.text;

        assertTrue(suiteReportFile.exists())
        assertTrue(suiteReportFileText.contains('1 failed'))
        assertTrue(suiteReportFileText.contains(testCase.name))
    }

    public void testTestSuiteWithRuntimeErrorTest() {
        madcowReport.prepareReportDirectory();

        def blade = new GrassBlade();
        blade.line = 'tent';

        MadcowTestCase testCase3 = new MadcowTestCaseException('testTestSuiteReport03', MockMadcowConfig.getMadcowConfig(true), new Exception("Things are broken mates"))
        testCase3.madcowConfig.stepRunner = "au.com.madcow.error.ThisClassDontExist";
        testCase3.steps.add(new MadcowStep(testCase3, null, null));
        testCase3.steps.first().result = MadcowStepResult.FAIL("Runtime Error has occurred");
        testCase3.stopWatch = new StopWatch();
        testCase3.stopWatch.start();
        testCase3.stopWatch.stop();
        testCase3.lastExecutedStep = testCase3.steps.first();

        MadcowTestSuite suite = new MadcowTestSuite('', null, [testCase3]);

        madcowReport.createTestCaseReport(testCase3);
        madcowReport.createTestSuiteReport(suite);

        def suiteReportFile = new File(MadcowProject.MADCOW_REPORT_DIRECTORY + '/index.html');
        String suiteReportFileText = suiteReportFile.text;

        assertTrue(suiteReportFile.exists())
        assertTrue(suiteReportFileText.contains('1 errors'))
        assertTrue(suiteReportFileText.contains(testCase3.name))
    }
}
