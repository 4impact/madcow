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

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.mappings.MadcowMappings
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep

import org.junit.Test

import static groovy.test.GroovyAssert.*

/**
 * Test for the SelectCheckbox BladeRunner.
 *
 * @author Andy Souyave
 */
class SelectCheckboxTest extends AbstractBladeTestCase {

    SelectCheckbox selectCheckbox = new SelectCheckbox();

    protected verifyCheckboxExecution(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testCheckboxByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aCheckboxId.selectCheckbox', testCase.grassParser);
        verifyCheckboxExecution(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'ResizeBrowserTest', ['id': 'aCheckboxId']);
        blade = new GrassBlade('ResizeBrowserTest.selectCheckbox', testCase.grassParser);
        verifyCheckboxExecution(blade, true);
    }

    @Test
    void testCheckboxByName() {
        MadcowMappings.addMapping(testCase, 'aCheckboxName', ['name': 'aCheckboxName']);
        GrassBlade blade = new GrassBlade('aCheckboxName.selectCheckbox', testCase.grassParser);
        verifyCheckboxExecution(blade, true);
    }

    @Test
    void testCheckboxByXPath() {
        MadcowMappings.addMapping(testCase, 'aCheckboxXPath', ['xpath': '//input[@id=\'aCheckboxId\']']);
        GrassBlade blade = new GrassBlade('aCheckboxXPath.selectCheckbox', testCase.grassParser);
        verifyCheckboxExecution(blade, true);
    }

    @Test
    void testCheckboxDoesNotExist() {
        GrassBlade blade = new GrassBlade('aCheckboxThatDoesntExist.selectCheckbox', testCase.grassParser);
        verifyCheckboxExecution(blade, false);
    }

    @Test
    void testDefaultMappingSelector() {
        GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectCheckbox', testCase.grassParser);
        assertTrue(selectCheckbox.isValidBladeToExecute(blade));
    }

    @Test
    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectCheckbox', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(selectCheckbox.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, TEXT, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    @Test
    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectCheckbox', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(selectCheckbox.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, TEXT, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    @Test
    void testEquationNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectCheckbox = yeah yeah', testCase.grassParser);
            assertFalse(selectCheckbox.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[STATEMENT]\' are supported.', e.message);
        }
    }
}
