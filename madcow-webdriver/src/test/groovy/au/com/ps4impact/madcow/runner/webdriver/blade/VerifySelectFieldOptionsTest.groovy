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

import java.util.concurrent.TimeUnit

/**
 * Test for the VerifySelectFieldOptions BladeRunner.
 *
 * @author Gavin Bunney
 */
class VerifySelectFieldOptionsTest extends AbstractBladeTestCase {

    def verifySelectFieldOptions = new VerifySelectFieldOptions()

    protected verifyValueExecution(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        (testCase.stepRunner as WebDriverStepRunner).driver.manage().timeouts().implicitlyWait(1, TimeUnit.MICROSECONDS);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testSelectFieldByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aSelectId.verifySelectFieldOptions = [\'Australia\', \'New Zealand\', \'United States\']', testCase.grassParser);
        verifyValueExecution(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'mapping', ['id': 'aSelectId']);
        blade = new GrassBlade('mapping.verifySelectFieldOptions = [\'Australia\', \'New Zealand\', \'United States\']', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectFieldByName() {
        MadcowMappings.addMapping(testCase, 'mapping', ['name': 'aSelectName']);
        GrassBlade blade = new GrassBlade('mapping.verifySelectFieldOptions = [\'Australia\', \'New Zealand\', \'United States\']', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectFieldByXPath() {
        MadcowMappings.addMapping(testCase, 'mapping', ['xpath': '//select[@id=\'aSelectId\']']);
        GrassBlade blade = new GrassBlade('mapping.verifySelectFieldOptions = [\'Australia\', \'New Zealand\', \'United States\']', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectFieldDoesNotExist() {
        GrassBlade blade = new GrassBlade('aSelectThatDoesntExist.verifySelectFieldOptions = [\'Tennis\']', testCase.grassParser);
        verifyValueExecution(blade, false);
    }

    @Test
    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifySelectFieldOptions = [\'Tennis\']', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(verifySelectFieldOptions.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    @Test
    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifySelectFieldOptions = [\'Tennis\']', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(verifySelectFieldOptions.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    @Test
    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifySelectFieldOptions', testCase.grassParser);
            assertFalse(verifySelectFieldOptions.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
