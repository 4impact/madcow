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
 * This class is to test the DoesNotHaveClass madcow operation
 *
 * @author Tom Romano
 */
class DoesNotHaveClassTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def doesNotHaveClass;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('DoesNotHaveClassTest', new MadcowConfig(), []);
        doesNotHaveClass = new DoesNotHaveClass();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected verifyDoesNotHaveClassContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testDoesNotHaveClassByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.doesNotHaveClass = aLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        blade = new GrassBlade('aLinkId.doesNotHaveClass = aLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);
    }

    void testDoesNotHaveClassCorrect() {
        GrassBlade blade = new GrassBlade('aLinkId.doesNotHaveClass = aClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, true);
    }

    void testDoesNotHaveClassWrongOrderButValidByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.doesNotHaveClass =  aSecondLinkClass aLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);
    }

    void testDoesntHaveSecondClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.doesNotHaveClass = aClass aSecondLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, true);
    }

    void testDoesntHaveEitherClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.doesNotHaveClass = aClass aLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, true);
    }

    void testHasSecondClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.doesNotHaveClass = aSecondLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);
    }

    void testNotHasSecondClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.doesNotHaveClass = aSecondClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, true);
    }

    void testDoesNotHaveClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.doesNotHaveClass = aLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);
    }

    void testDoesNotHaveClassByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.doesNotHaveClass = aLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);
    }

    void testDoesNotHaveClassByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.doesNotHaveClass = aLinkClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);
    }

    void testDoesNotHaveClassForTextArea() {
        GrassBlade blade = new GrassBlade('aTextAreaId.doesNotHaveClass = aTextAreaClass', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);
    }

    void testDoesNotHaveClassEmpty() {
        GrassBlade blade = new GrassBlade('anEmptyParagraphId.doesNotHaveClass = ', testCase.grassParser);
        verifyDoesNotHaveClassContents(blade, false);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.doesNotHaveClass = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(doesNotHaveClass.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.doesNotHaveClass = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(doesNotHaveClass.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.doesNotHaveClass', testCase.grassParser);
            assertFalse(doesNotHaveClass.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}

