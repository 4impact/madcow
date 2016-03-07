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

import java.util.concurrent.TimeUnit

import static groovy.test.GroovyAssert.*

/**
 * Test for the SelectField BladeRunner.
 *
 * @author Gavin Bunney
 */
class SelectFieldTest extends AbstractBladeTestCase {

    SelectField selectField = new SelectField()

    protected verifyValueExecution(GrassBlade blade, boolean shouldPass, String resultingOutput = null, boolean exactMatch = true) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        (testCase.stepRunner as WebDriverStepRunner).driver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
        if (exactMatch && resultingOutput) {
            assertEquals step.result.message, resultingOutput
        } else {
           if (!exactMatch && resultingOutput)
               assertTrue(step.result.message.indexOf(resultingOutput) != -1)
        }
    }

    @Test
    void testSelectFieldByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aSelectId.selectField = New Zealand', testCase.grassParser);
        verifyValueExecution(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'aSelectId', ['id': 'aSelectId']);
        blade = new GrassBlade('aSelectId.selectField = United States', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectFieldWithOptionGroupsByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('carMakes.selectField = saab', testCase.grassParser);
        verifyValueExecution(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'carMakers', ['id': 'carMakes']);
        blade = new GrassBlade('carMakers.selectField = Audi', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectMultiFieldByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('carModels.selectField = [\"VLK123\",\"a45\"]', testCase.grassParser);
        verifyValueExecution(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'models', ['id': 'carModels']);
        blade = new GrassBlade('models.selectField = [\"a45\",\"clk\"]', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectMultiFieldOneFailedByHtmlId() {
        // explicit id
        MadcowMappings.addMapping(testCase, 'models', ['id': 'carModels']);
        GrassBlade blade = new GrassBlade('models.selectField = [\"a45\",\"clk200\"]', testCase.grassParser);
        verifyValueExecution(blade, false, "Cannot find a valid option/s for item [clk200]");
    }

    @Test
    void testSelectNonMultiFieldFailedByHtmlId() {
        GrassBlade blade = new GrassBlade('carMakes.selectField = ["a45","clk200"]', testCase.grassParser);
        verifyValueExecution(blade, false, "Cannot specify list when select element doesn't have multiple attribute");
    }

    @Test
    void testSelectWithInvalidIntegerParamFails() {
        GrassBlade blade = new GrassBlade('carCylinders.selectField = 1', testCase.grassParser);
        verifyValueExecution(blade, false, "Unable to find specified option");
    }

    @Test
    void testSelectWithValidIntegerParamPass() {
        GrassBlade blade = new GrassBlade('carCylinders.selectField = 2', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectFieldByName() {
        MadcowMappings.addMapping(testCase, 'aSelectName', ['name': 'aSelectName']);
        GrassBlade blade = new GrassBlade('aSelectName.selectField = New Zealand', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectFieldByXPath() {
        MadcowMappings.addMapping(testCase, 'aSelectXPath', ['xpath': '//select[@id=\'aSelectId\']']);
        GrassBlade blade = new GrassBlade('aSelectXPath.selectField = New Zealand', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    @Test
    void testSelectFieldDoesNotExist() {
        GrassBlade blade = new GrassBlade('aSelectThatDoesntExist.selectField = Tennis', testCase.grassParser);
        verifyValueExecution(blade, false);
    }

    @Test
    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectField = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(selectField.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    @Test
    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectField = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(selectField.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    @Test
    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectField', testCase.grassParser);
            assertFalse(selectField.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
