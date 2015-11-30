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
import au.com.ps4impact.madcow.mappings.MadcowMappings
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder

import java.util.concurrent.TimeUnit

/**
 * Test for the SelectFieldByIndex BladeRunner.
 *
 * @author Tom Romano
 */
class SelectFieldByIndexTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def selectFieldByIndex;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('SelectFieldByIndexTest', new MadcowConfig(), []);
        selectFieldByIndex = new SelectFieldByIndex();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected verifyValueExecution(GrassBlade blade, boolean shouldPass, String resultingOutput = null) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        (testCase.stepRunner as WebDriverStepRunner).driver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
        if (resultingOutput)
            assertEquals step.result.message, resultingOutput
    }

    void testSelectFieldByIndexByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('carCylinders.selectFieldByIndex = 2', testCase.grassParser);
        verifyValueExecution(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'carCylinders', ['id': 'carCylinders']);
        blade = new GrassBlade('carCylinders.selectFieldByIndex = 1', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testSelectFieldByIndexByName() {
        MadcowMappings.addMapping(testCase, 'aSelectName', ['name': 'carCylindersName']);
        GrassBlade blade = new GrassBlade('aSelectName.selectFieldByIndex = 0', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testSelectFieldByIndexByNameOutsideRange() {
        MadcowMappings.addMapping(testCase, 'aSelectName', ['name': 'carCylindersName']);
        GrassBlade blade = new GrassBlade('aSelectName.selectFieldByIndex = 3', testCase.grassParser);
        verifyValueExecution(blade, false);
    }

    void testSelectFieldByIndexByXPath() {
        MadcowMappings.addMapping(testCase, 'aSelectXPath', ['xpath': '//select[@id=\'carCylinders\']']);
        GrassBlade blade = new GrassBlade('aSelectXPath.selectFieldByIndex = 2', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testSelectFieldDoesNotExist() {
        GrassBlade blade = new GrassBlade('carCylinders.selectFieldByIndex = 6', testCase.grassParser);
        verifyValueExecution(blade, false, "Unable to find specified option");
    }

    void testSelectFieldByIndexWithAListShouldFail() {
        try {
            GrassBlade blade = new GrassBlade('carCylinders.selectFieldByIndex = ["1","2"]', testCase.grassParser);
            assertFalse(selectFieldByIndex.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only parameters of type \'[Integer]\' are supported.', e.message);
        }
    }

    void testSelectFieldByIndexWithStringShouldFail() {
        try {
            GrassBlade blade = new GrassBlade('carCylinders.selectFieldByIndex = "1"', testCase.grassParser);
            assertFalse(selectFieldByIndex.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only parameters of type \'[Integer]\' are supported.', e.message);
        }
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectFieldByIndex = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(selectFieldByIndex.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectFieldByIndex = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(selectFieldByIndex.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectFieldByIndex', testCase.grassParser);
            assertFalse(selectFieldByIndex.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
