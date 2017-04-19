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
 * Test for the CheckValue BladeRunner.
 *
 * @author Gavin Bunney
 */
class CheckValueTest extends AbstractBladeTestCase {

    CheckValue checkValue = new CheckValue();

    protected verifyCheckValueContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testCheckValueByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.checkValue = A link', testCase.grassParser);
        verifyCheckValueContents(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'mapping', ['id': 'aLinkId']);
        blade = new GrassBlade('mapping.checkValue = A link', testCase.grassParser);
        verifyCheckValueContents(blade, true);
    }

    @Test
    void testCheckValueByCss() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.checkValue = A link', testCase.grassParser);
        verifyCheckValueContents(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'cssLinkName', ['css': '#aLinkId']);
        blade = new GrassBlade('cssLinkName.checkValue = A link', testCase.grassParser);
        verifyCheckValueContents(blade, true);
    }

    @Test
    void testCheckValueIncorrect() {
        GrassBlade blade = new GrassBlade('aLinkId.checkValue = A link that isn\'t a link is still a link', testCase.grassParser);
        verifyCheckValueContents(blade, false);
    }

    @Test
    void testCheckValueByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.checkValue = A link', testCase.grassParser);
        verifyCheckValueContents(blade, true);
    }

    @Test
    void testCheckValueByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.checkValue = A link', testCase.grassParser);
        verifyCheckValueContents(blade, true);
    }

    @Test
    void testCheckValueByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.checkValue = A link', testCase.grassParser);
        verifyCheckValueContents(blade, true);
    }

    @Test
    void testCheckValueForTextArea() {
        GrassBlade blade = new GrassBlade('aTextAreaId.checkValue = Text area contents', testCase.grassParser);
        verifyCheckValueContents(blade, true);
    }

    @Test
    void testCheckValueEmpty() {
        GrassBlade blade = new GrassBlade('anEmptyParagraphId.checkValue = ', testCase.grassParser);
        verifyCheckValueContents(blade, true);
    }

    @Test
    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.checkValue = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(checkValue.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    @Test
    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.checkValue = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(checkValue.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, NAME, XPATH, CSS] are supported.', e.message);
        }
    }

    @Test
    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.checkValue', testCase.grassParser);
            assertFalse(checkValue.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
