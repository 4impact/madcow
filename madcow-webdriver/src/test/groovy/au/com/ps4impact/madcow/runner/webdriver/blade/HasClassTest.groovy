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
 * This class is to test the HasClass madcow operation
 *
 * @author Tom Romano
 */
class HasClassTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def hasClass;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('HasClassTest', new MadcowConfig(), []);
        hasClass = new HasClass();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected verifyHasClassContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testHasClassByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.hasClass = aLinkClass', testCase.grassParser);
        verifyHasClassContents(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'mapping', ['id': 'aLinkId']);
        blade = new GrassBlade('mapping.hasClass = aLinkClass', testCase.grassParser);
        verifyHasClassContents(blade, true);
    }

    void testHasClassIncorrect() {
        GrassBlade blade = new GrassBlade('aLinkId.hasClass = aClass', testCase.grassParser);
        verifyHasClassContents(blade, false);
    }

    void testHasClassWrongOrderByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasClass =  aSecondLinkClass aLinkClass', testCase.grassParser);
        verifyHasClassContents(blade, true);
    }

    void testHasSecondClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasClass = aSecondLinkClass', testCase.grassParser);
        verifyHasClassContents(blade, true);
    }

    void testHasClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasClass = aLinkClass', testCase.grassParser);
        verifyHasClassContents(blade, true);
    }

    void testHasClassByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.hasClass = aLinkClass', testCase.grassParser);
        verifyHasClassContents(blade, true);
    }

    void testHasClassByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.hasClass = aLinkClass', testCase.grassParser);
        verifyHasClassContents(blade, true);
    }

    void testHasClassForTextArea() {
        GrassBlade blade = new GrassBlade('aTextAreaId.hasClass = aTextAreaClass', testCase.grassParser);
        verifyHasClassContents(blade, true);
    }

    void testHasClassEmpty() {
        GrassBlade blade = new GrassBlade('anEmptyParagraphId.hasClass = ', testCase.grassParser);
        verifyHasClassContents(blade, true);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.hasClass = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(hasClass.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.hasClass = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(hasClass.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.hasClass', testCase.grassParser);
            assertFalse(hasClass.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}

