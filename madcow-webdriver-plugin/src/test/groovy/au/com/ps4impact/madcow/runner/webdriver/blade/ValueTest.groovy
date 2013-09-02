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
import org.openqa.selenium.By

import java.util.concurrent.TimeUnit

/**
 * Test for the Value BladeRunner.
 *
 * @author Gavin Bunney
 */
class ValueTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ValueTest', new MadcowConfig(), []);
    def value = new Value();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifyValueExecution(GrassBlade blade, boolean shouldPass, String newValue, String elementId = "aInputId") {
        (testCase.stepRunner as WebDriverStepRunner).initialiseDriverWithRetriesIfRequired();
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        (testCase.stepRunner as WebDriverStepRunner).driver.manage().timeouts().implicitlyWait(1, TimeUnit.MICROSECONDS);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
        if (shouldPass) {
            def inputElement = (testCase.stepRunner as WebDriverStepRunner).driver.findElement(By.id(elementId))
            assertTrue inputElement.element.getText().equals(newValue)
        }
    }

    void testValueByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aInputId.value = Tennis', testCase.grassParser);
        verifyValueExecution(blade, true, "Tennis");

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aInputId', ['id': 'aInputId']);
        blade = new GrassBlade('aInputId.value = NoTennis', testCase.grassParser);
        verifyValueExecution(blade, true, "NoTennis");
    }

    void testValueByCSS() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aInputId.value = Tennis!', testCase.grassParser);
        verifyValueExecution(blade, true, "Tennis!");

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'cssInputId', ['css': '#aInputId']);
        blade = new GrassBlade('cssInputId.value = TennisTwo', testCase.grassParser);
        verifyValueExecution(blade, true, "TennisTwo");
    }

    void testValueByCSSClass() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aInputId.value = TennisT', testCase.grassParser);
        verifyValueExecution(blade, true, "TennisT");

        // explicit css
        MadcowMappings.addMapping(testCase, 'cssInputId', ['css': '.aInputClass']);
        blade = new GrassBlade('cssInputId.value = TennisThree', testCase.grassParser);
        verifyValueExecution(blade, true, "TennisThree");

    }

    void testValueByName() {
        MadcowMappings.addMapping(testCase, 'aInputName', ['name': 'aInputName']);
        GrassBlade blade = new GrassBlade('aInputName.value = TennisByName', testCase.grassParser);
        verifyValueExecution(blade, true, "TennisByName");
    }

    void testValueByXPath() {
        MadcowMappings.addMapping(testCase, 'aInputXPath', ['xpath': '//input[@id=\'aInputId\']']);
        GrassBlade blade = new GrassBlade('aInputXPath.value = TennisByXpath', testCase.grassParser);
        verifyValueExecution(blade, true, "TennisByXpath");
    }

    void testValueForTextArea() {
        GrassBlade blade = new GrassBlade('aTextAreaId.value = TennisForTextArea', testCase.grassParser);
        verifyValueExecution(blade, true, "TennisForTextArea", "aTextAreaId");

        // explicit css
        MadcowMappings.addMapping(testCase, 'cssaTextAreaId', ['css': '#aTextAreaId']);
        blade = new GrassBlade('cssaTextAreaId.value = TennisFour', testCase.grassParser);
        verifyValueExecution(blade, true, "TennisFour", "aTextAreaId");
    }

    void testValueDoesNotExist() {
        GrassBlade blade = new GrassBlade('aInputThatDoesntExist.value = Tennis', testCase.grassParser);
        verifyValueExecution(blade, false, null);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.value = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(value.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.value = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(value.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.value', testCase.grassParser);
            assertFalse(value.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
