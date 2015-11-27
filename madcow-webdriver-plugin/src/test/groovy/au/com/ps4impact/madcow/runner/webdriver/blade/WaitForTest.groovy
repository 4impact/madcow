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
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.mappings.MadcowMappings

/**
 * Test for the WaitFor BladeRunner.
 *
 * @author Gavin Bunney
 */
class WaitForTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def waitFor;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('WaitForTest', new MadcowConfig(), []);
        waitFor = new WaitFor();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected verifyWaitFor(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testWaitForByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.waitFor', testCase.grassParser);
        verifyWaitFor(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'mapping', ['id': 'aLinkId']);
        blade = new GrassBlade('mapping.waitFor', testCase.grassParser);
        verifyWaitFor(blade, true);
    }

    void testWaitForIncorrect() {
        GrassBlade blade = new GrassBlade('aLinkId.waitFor = A link that isn\'t a link is still a link', testCase.grassParser);
        verifyWaitFor(blade, false);
    }

    void testWaitForByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.waitFor = A link', testCase.grassParser);
        verifyWaitFor(blade, true);
    }

    void testWaitForHTML() {
        GrassBlade blade = new GrassBlade('waitFor = <button id="enabledButton" name="enabledButton">enabledButton</button>', testCase.grassParser);
        verifyWaitFor(blade, true);
        assertEquals(blade.parameters, "<button id=\"enabledButton\" name=\"enabledButton\">enabledButton</button>")
    }

    void testWaitForByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.waitFor = A link', testCase.grassParser);
        verifyWaitFor(blade, true);
    }

    void testWaitForByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.waitFor = A link', testCase.grassParser);
        verifyWaitFor(blade, true);
    }

    void testWaitForEmpty() {
        GrassBlade blade = new GrassBlade('anEmptyParagraphId.waitFor = ', testCase.grassParser);
        verifyWaitFor(blade, true);
    }

    void testWaitForPageText() {
        GrassBlade blade = new GrassBlade('waitFor = Madcow WebDriver Runner Test HTML', testCase.grassParser);
        verifyWaitFor(blade, true);
    }
}