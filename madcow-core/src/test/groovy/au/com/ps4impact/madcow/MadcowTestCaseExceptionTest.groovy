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

import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * Test for the MadcowTestCaseExceptionTest class.
 *
 * @author Tom Romano
 */
class MadcowTestCaseExceptionTest extends GroovyTestCase {

    protected ArrayList<String> getGrassScript() {
        String grassScriptString = """
            @expectedValue = Australia

            # verify the expected country
            addressbook_search_country.verifyText = @expectedValue
            addressbook_search_country.verifySelectFieldOptions = ['One', 'Two']

            # perform a search and check the field options
            addressbook_search_button.clickLink
            addressbook_search_country.verifySelectFieldOptions = [options : ['@expectedValue', 'New Zealand']]
        """;
        ArrayList<String> grassScript = new ArrayList<String>();
        grassScriptString.eachLine { line -> grassScript.add(line) }
        return grassScript;
    }

    void testCreateAndNoStepRunnerMade() {
        Exception error = new Exception("This is an awesome error buddy")
        MadcowTestCase testCase = new MadcowTestCaseException('testCreateAndParse', MockMadcowConfig.getMadcowConfig(),
                getGrassScript(), error);
        assertEquals(testCase.error, error)
        assertEquals(testCase.ignoreTestCase, false)
        assertEquals(testCase.error.message, "This is an awesome error buddy")
        assertNull(testCase.stepRunner)
        assertEquals(testCase.steps.size(), 1)
        assertEquals(testCase.lastExecutedStep.result.getMessage(), "This is an awesome error buddy")
        assertNull(testCase.testSuite)
    }



}
