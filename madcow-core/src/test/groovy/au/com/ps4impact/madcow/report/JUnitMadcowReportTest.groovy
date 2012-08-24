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
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.time.StopWatch
import org.apache.commons.lang3.StringUtils
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.MadcowTestSuite

/**
 * Test for the JUnitMadcowReport class.
 *
 * @author Gavin Bunney
 */
class JUnitMadcowReportTest extends GroovyTestCase {

    JUnitMadcowReport junitReport = new JUnitMadcowReport();

    protected void validateJUnitXML(MadcowTestCase testCase) {
        junitReport.createTestCaseReport(testCase);

        def outputFile = new File(JUnitMadcowReport.JUNIT_RESULTS_XML_DIRECTORY + "/TEST-${StringUtils.replace(testCase.name, '/', '_')}.xml");

        assertEquals(testCase.lastExecutedStep.result.failed(), outputFile.text.contains('<failure'));

        def factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        def schema = factory.newSchema(new StreamSource(ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'ant-junit.xsd')))
        def validator = schema.newValidator()
        validator.validate(new StreamSource(outputFile));
    }

    public void testTestCaseReportForPass() {
        junitReport.prepareReportDirectory();

        MadcowTestCase testCase = new MadcowTestCase('testTestCaseReportForPass', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, null, null));
        testCase.steps.first().result = MadcowStepResult.PASS();
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        validateJUnitXML(testCase);
    }

    public void testTestCaseReportForFailure() {
        junitReport.prepareReportDirectory();

        MadcowTestCase testCase = new MadcowTestCase('testTestCaseReportForFailure', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, null, null));
        testCase.steps.first().result = MadcowStepResult.FAIL('Some error occured');
        testCase.steps.first().result.detailedMessage = 'This could be a stacktrace or something.... \n\nCould be...'
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        validateJUnitXML(testCase);
    }

    public void testTestSuiteReport() {
        junitReport.prepareReportDirectory();

        MadcowTestCase testCase = new MadcowTestCase('testTestSuiteReport01', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, null, null));
        testCase.steps.first().result = MadcowStepResult.PASS();
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        MadcowTestCase testCase2 = new MadcowTestCase('testTestSuiteReport02', MockMadcowConfig.getMadcowConfig(true))
        testCase2.steps.add(new MadcowStep(testCase2, null, null));
        testCase2.steps.first().result = MadcowStepResult.FAIL('Some error </failure> occured');
        testCase2.stopWatch = new StopWatch();
        testCase2.stopWatch.start();
        testCase2.stopWatch.stop();
        testCase2.lastExecutedStep = testCase2.steps.first();

        MadcowTestSuite suite = new MadcowTestSuite('', null, [testCase, testCase2]);

        junitReport.createTestCaseReport(testCase);
        junitReport.createTestCaseReport(testCase2);
        junitReport.createTestSuiteReport(suite);

        def packageFile = new File(JUnitMadcowReport.JUNIT_RESULTS_HTML_DIRECTORY + '/package-summary.html');
        assertTrue(packageFile.exists())
        assertTrue(packageFile.text.contains(testCase.name))
        assertTrue(packageFile.text.contains(testCase2.name))
    }
}
