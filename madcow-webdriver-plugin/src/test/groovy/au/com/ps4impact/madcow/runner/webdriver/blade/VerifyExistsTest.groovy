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
import au.com.ps4impact.madcow.mappings.MadcowMappings
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * This class is a test case for verifying that the element exists
 *
 * @author Tom Romano
 */
class VerifyExistsTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def verifyExists;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('VerifyExistsTest', new MadcowConfig(), []);
        verifyExists = new VerifyExists();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected verifyExistsExecution(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testLinkByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.verifyExists', testCase.grassParser);
        verifyExistsExecution(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        blade = new GrassBlade('aLinkId.verifyExists', testCase.grassParser);
        verifyExistsExecution(blade, true);
    }

    void testLinkByHtmlIdHidden() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('hiddenTextInput.verifyExists', testCase.grassParser);
        verifyExistsExecution(blade, false);
    }

    void testLinkByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.verifyExists', testCase.grassParser);
        verifyExistsExecution(blade, true);
    }

    void testLinkByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.verifyExists', testCase.grassParser);
        verifyExistsExecution(blade, true);
    }

    void testLinkByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.verifyExists', testCase.grassParser);
        verifyExistsExecution(blade, true);
    }

    void testLinkDoesNotExist() {
        GrassBlade blade = new GrassBlade('aLinkThatDoesntExist.verifyExists', testCase.grassParser);
        verifyExistsExecution(blade, false);
    }

    void testDefaultMappingSelector() {
        GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifyExists', testCase.grassParser);
        assertTrue(verifyExists.isValidBladeToExecute(blade));
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifyExists', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(verifyExists.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, TEXT, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifyExists', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(verifyExists.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, TEXT, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    void testEquationNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifyExists = yeah yeah', testCase.grassParser);
            assertFalse(verifyExists.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[STATEMENT]\' are supported.', e.message);
        }
    }
}
