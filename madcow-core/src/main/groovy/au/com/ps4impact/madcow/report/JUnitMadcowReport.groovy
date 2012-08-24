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

import groovy.text.GStringTemplateEngine
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.MadcowProject
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.MadcowTestSuite
import org.apache.commons.lang3.StringEscapeUtils

/**
 * JUnit specific Test Case Report
 *
 * @author Gavin Bunney
 */
class JUnitMadcowReport implements IMadcowReport {

    public static final String JUNIT_RESULTS_DIRECTORY = MadcowProject.RESULTS_DIRECTORY + "/junit-results";
    public static final String JUNIT_RESULTS_XML_DIRECTORY = JUNIT_RESULTS_DIRECTORY + "/xml";
    public static final String JUNIT_RESULTS_HTML_DIRECTORY = JUNIT_RESULTS_DIRECTORY + "/html";

    public void prepareReportDirectory() {

        if (new File(JUNIT_RESULTS_DIRECTORY).exists())
            new File(JUNIT_RESULTS_DIRECTORY).deleteDir();

        new File(JUNIT_RESULTS_XML_DIRECTORY).mkdirs();
        new File(JUNIT_RESULTS_HTML_DIRECTORY).mkdirs();
    }

    /**
     * Create the result files.
     */
    public void createTestCaseReport(MadcowTestCase testCase) {

        def testCaseResult = testCase.lastExecutedStep.result;

        String escapedMessage = StringEscapeUtils.escapeXml(testCaseResult.message);

        def binding = [ 'errorCount'        : '0',
                        'failureCount'      : testCase.lastExecutedStep.result.failed() ? '1' : '0',
                        'hostname'          : StringEscapeUtils.escapeXml(InetAddress.localHost.hostName),
                        'testName'          : StringEscapeUtils.escapeXml(testCase.name),
                        'time'              : testCase.getTotalTimeInSeconds(),
                        'timestamp'         : new Date(testCase.stopWatch.startTime).format("yyyy-MM-dd'T'HH:mm:ss"),
                        'systemOut'         : testCaseResult.passed() ? "Passed" : '',
                        'systemErr'         : testCaseResult.failed() ? "Failed: " + escapedMessage : '',
                        'failure'           : testCaseResult.failed() ? escapedMessage : '',
                        'failureDetails'    : testCaseResult.failed() ? testCaseResult.detailedMessage ?: '' : '',
        ];

        def engine = new GStringTemplateEngine();
        def template = engine.createTemplate(ResourceFinder.locateResourceOnClasspath(testCase.class.classLoader, 'result-junit.gtemplate').URL).make(binding);

        String templateContents = template.toString();
        def result = new File(JUNIT_RESULTS_XML_DIRECTORY + "/TEST-${StringUtils.replace(testCase.name, '/', '_')}.xml");
        result.write(templateContents);
    }

    /**
     * Create a HTML JUnit Report for all the TEST-*.xml files.
     */
    public void createTestSuiteReport(MadcowTestSuite testSuite) {

        def antBuilder = new AntBuilder();

        antBuilder.junitreport(todir: JUNIT_RESULTS_HTML_DIRECTORY) {
            fileset(dir: JUnitMadcowReport.JUNIT_RESULTS_XML_DIRECTORY) {
                include(name: "TEST-*.xml")
            }
            report(todir: JUNIT_RESULTS_HTML_DIRECTORY);
        }
    }
}