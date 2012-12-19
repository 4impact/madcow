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
 * This class is to test the HasStyle madcow operation
 *
 * @author Tom Romano
 */
class HasStyleTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('HasStyleTest', new MadcowConfig(), []);
    def hasStyle = new HasStyle();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifyHasStyleContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testHasStyleByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.hasStyle = display: block', testCase.grassParser);
        verifyHasStyleContents(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        blade = new GrassBlade('aLinkId.hasStyle = border-bottom-width: 2px', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    void testHasStyleByHtmlIdInherited() {
        //test doesnt report font size as this is a css inherited style value
        GrassBlade blade = new GrassBlade('aLinkId.hasStyle = font-size: 20px;', testCase.grassParser);
        verifyHasStyleContents(blade, false);
    }

    void testHasStylePartiallyIncorrect() {
        GrassBlade blade = new GrassBlade('aLinkId.hasStyle = display: block;border-bottom-width: 1px;', testCase.grassParser);
        verifyHasStyleContents(blade, false);
    }

    void testHasStyleWrongOrderByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasStyle = border-bottom-width: 2px;display: block;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    void testHasSecondClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasStyle = border-bottom-width: 2px;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    void testHasStyleByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasStyle = display: block;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    void testHasStyleByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.hasStyle = border-bottom-width: 2px;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    void testHasStyleByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.hasStyle = border-bottom-width: 2px;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    void testHasStyleForTextArea() {
        GrassBlade blade = new GrassBlade('aTextAreaId.hasStyle = border-bottom-width: 2px;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    void testHasStyleEmpty() {
        GrassBlade blade = new GrassBlade('anEmptyParagraphId.hasStyle = ', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.hasStyle = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(hasStyle.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.hasStyle = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(hasStyle.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.hasStyle', testCase.grassParser);
            assertFalse(hasStyle.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}

