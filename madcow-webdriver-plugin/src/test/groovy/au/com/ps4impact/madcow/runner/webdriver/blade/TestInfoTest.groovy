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

package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * Test for the TestInfo BladeRunner.
 *
 * @author Gavin Bunney
 */
class TestInfoTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def testInfo;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('TestInfoTest', new MadcowConfig(), []);
        testInfo = new TestInfo();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected verifyTestInfo(GrassBlade blade, boolean shouldPass, String expectedMessage) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.noOperation());
        assertEquals(expectedMessage, step.result.message)
    }

    void testTestInfoValid() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('testInfo = This is my awesome test case', testCase.grassParser);
        verifyTestInfo(blade, true, "This is my awesome test case");
    }

    void testTestInfoValidAlso() {
        GrassBlade blade = new GrassBlade('testInfo = The link name', testCase.grassParser);
        verifyTestInfo(blade, true, "The link name");
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.testInfo = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(testInfo.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must not be supplied.', e.message);
        }
    }

    void testMappingSelectorNotRequired() {
        GrassBlade blade = new GrassBlade('aLinkId.testInfo = Tennis', testCase.grassParser);
        blade.mappingSelectorType = null;
        assertTrue(testInfo.isValidBladeToExecute(blade));
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.testInfo', testCase.grassParser);
            assertFalse(testInfo.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
