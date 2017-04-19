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
 * This class is to test the HasStyle madcow operation
 *
 * @author Tom Romano
 */
class HasStyleTest extends AbstractBladeTestCase {

    HasStyle hasStyle = new HasStyle();

    protected verifyHasStyleContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(step.result.message, shouldPass, step.result.passed());
    }

    @Test
    void testHasStyleByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.hasStyle = display: block', testCase.grassParser);
        verifyHasStyleContents(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'mapping', ['id': 'aLinkId']);
        blade = new GrassBlade('mapping.hasStyle = border-bottom-width: 2px', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    @Test
    void testHasStylePartiallyIncorrect() {
        GrassBlade blade = new GrassBlade('aLinkId.hasStyle = display: block; border-bottom-width: 1px;', testCase.grassParser);
        verifyHasStyleContents(blade, false);
    }

    @Test
    void testHasStyleWrongOrderByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasStyle = border-bottom-width: 2px;display: block;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    @Test
    void testHasSecondClassByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasStyle = border-bottom-width: 2px;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    @Test
    void testHasStyleByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.hasStyle = display: block;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    @Test
    void testHasStyleByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.hasStyle = border-bottom-width: 2px;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    @Test
    void testHasStyleByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.hasStyle = border-bottom-width: 2px;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    @Test
    void testHasStyleForTextArea() {
        GrassBlade blade = new GrassBlade('aTextAreaId.hasStyle = border-bottom-width: 2px;', testCase.grassParser);
        verifyHasStyleContents(blade, true);
    }

    @Test
    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.hasStyle = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(hasStyle.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, NAME, XPATH] are supported.', e.message);
        }
    }

    @Test
    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.hasStyle = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(hasStyle.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, NAME, XPATH] are supported.', e.message);
        }
    }

    @Test
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

