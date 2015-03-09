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

/**
 * This class is to test the VerifyCssValue madcow operation
 *
 * @author Tom Romano
 */
class VerifyCssValueTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def verifyCssValue;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('VerifyCssValueTest', new MadcowConfig(), []);
        verifyCssValue = new VerifyCssValue();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected verifyVerifyCssValueContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testVerifyCssValueByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.verifyCssValue = font-weight: 700;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        blade = new GrassBlade('aLinkId.verifyCssValue = font-weight: 700', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueByHtmlIdInherited() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.verifyCssValue = font-size: 20px;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValuePartiallyIncorrect() {
        GrassBlade blade = new GrassBlade('aLinkId.verifyCssValue = font-size: 20px;color: #333;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, false);
    }

    void testVerifyCssValueWrongOrderByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.verifyCssValue = border-bottom-width: 2px;display: block;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueWithMap() {
        GrassBlade blade = new GrassBlade('aInputId.verifyCssValue = [ \'line-height\': \'28px\', \'cursor\': \'wait\' ]', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueWithMapUnordered() {
        GrassBlade blade = new GrassBlade('aLinkId.verifyCssValue = [ \'font-weight\': \'700\', \'font-size\': \'20px\', \'color\': \'rgb(51, 51, 51)\' ]', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueByName() {
        MadcowMappings.addMapping(testCase, 'aInputName', ['name': 'aInputName']);
        GrassBlade blade = new GrassBlade('aInputName.verifyCssValue = line-height:28px;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueMultiplesByName() {
        MadcowMappings.addMapping(testCase, 'aInputName', ['name': 'aInputName']);
        GrassBlade blade = new GrassBlade('aInputName.verifyCssValue = cursor: wait;line-height:28px;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.verifyCssValue = border-bottom-width: 2px;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.verifyCssValue = border-bottom-width: 2px;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueForTextArea() {
        GrassBlade blade = new GrassBlade('aTextAreaId.verifyCssValue = border-bottom-width: 2px;', testCase.grassParser);
        verifyVerifyCssValueContents(blade, true);
    }

    void testVerifyCssValueEmpty() {
        GrassBlade blade = new GrassBlade('anEmptyParagraphId.verifyCssValue = ', testCase.grassParser);
        verifyVerifyCssValueContents(blade, false);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifyCssValue = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(verifyCssValue.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifyCssValue = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(verifyCssValue.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.verifyCssValue', testCase.grassParser);
            assertFalse(verifyCssValue.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}

