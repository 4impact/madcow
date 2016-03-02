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
 * Test for the VerifySelectFieldContains BladeRunner.
 *
 * @author Paul Bevis
 */
class VerifySelectFieldContainsTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def verifySelectFieldContains;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('VerifySelectFieldContainsTest', new MadcowConfig(), []);
        verifySelectFieldContains = new VerifySelectFieldContains();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected verifyValueExecution(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        (testCase.stepRunner as WebDriverStepRunner).driver.manage().timeouts().implicitlyWait(1, TimeUnit.MICROSECONDS);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testSelectFieldByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aSelectId.verifySelectFieldContains = ["Australia", "New Zealand"]', testCase.grassParser);
        verifyValueExecution(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'mapping', ['id': 'aSelectId']);
        blade = new GrassBlade('mapping.verifySelectFieldContains = ["United States", "Australia", "New Zealand"]', testCase.grassParser);
        verifyValueExecution(blade, true);

        //failure example
        blade = new GrassBlade('mapping.verifySelectFieldContains = [\'Uk\']', testCase.grassParser);
        verifyValueExecution(blade, false);
    }

    void testSelectFieldByName() {
        MadcowMappings.addMapping(testCase, 'mapping', ['name': 'aSelectName']);
        GrassBlade blade = new GrassBlade('mapping.verifySelectFieldContains = ["Australia", "New Zealand"]', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testSelectFieldByXPath() {
        MadcowMappings.addMapping(testCase, 'mapping', ['xpath': '//select[@id=\'aSelectId\']']);
        GrassBlade blade = new GrassBlade('mapping.verifySelectFieldContains = [\'Australia\']', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testSelectFieldDoesNotExist() {
        GrassBlade blade = new GrassBlade('aSelectThatDoesntExist.verifySelectFieldContains = [\'Uk\']', testCase.grassParser);
        verifyValueExecution(blade, false);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifySelectFieldContains = [\'Uk\']', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(verifySelectFieldContains.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifySelectFieldContains = [\'Uk\']', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(verifySelectFieldContains.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifySelectFieldContains', testCase.grassParser);
            assertFalse(verifySelectFieldContains.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
