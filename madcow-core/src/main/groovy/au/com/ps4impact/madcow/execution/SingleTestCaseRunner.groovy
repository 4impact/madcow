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

package au.com.ps4impact.madcow.execution

import au.com.ps4impact.madcow.MadcowTestCaseException
import org.apache.log4j.Logger
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.report.IMadcowReport
import au.com.ps4impact.madcow.logging.MadcowLog

/**
 * Single Test Runner is used to run a single madcow test case
 *
 * Perhaps useful for when running vs a remote server and getting a lot of timeouts
 *
 * @author Tom Romano
 */
class SingleTestCaseRunner {

    protected static final Logger LOG = Logger.getLogger(SingleTestCaseRunner.class);

    def SingleTestCaseRunner(MadcowTestCase testCase, List<IMadcowReport> reporters ) {
        try {

            if (testCase instanceof MadcowTestCaseException){
                testCase.logInfo("Failed attempting to run ${testCase.name}");
            }else{
                testCase.logInfo("Running ${testCase.name}");
                try {
                    testCase.execute();
                    testCase.logInfo("Test ${testCase.name} Passed");
                } catch (e) {
                    testCase.testCaseError = true;
                    testCase.logError("Test ${testCase.name} Failed!\n\nException: $e");
                }
            }
        } catch (e) {
            LOG.error("${testCase.name} throw an unexpected exception:\n$e");
        }finally{
            MadcowLog.shutdownLogging(testCase);
            reporters.each { reporter -> reporter.createTestCaseReport(testCase) };
        }
    }

}