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
 * Test for the UnselectFrame Madcow operation Blade Runner.
 *
 * @author Tom Romano
 */
class UnselectFrameTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('UnselectFrameTest', new MadcowConfig(), []);
    def unselectFrame = new UnselectFrame();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected unselectFrameValid(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).initialiseDriverWithRetriesIfRequired();
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testUnselectFrameByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aIframeId.unselectFrame', testCase.grassParser);
        unselectFrameValid(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aIframe', ['id': 'aIframeId']);
        blade = new GrassBlade('aIframe.unselectFrame', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameValidWithIncorrectParam() {
        GrassBlade blade = new GrassBlade('aIframeId.unselectFrame = A link that isn\'t a link is still a link', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameByName() {
        MadcowMappings.addMapping(testCase, 'aIframeByName', ['name': 'aIframeName']);
        GrassBlade blade = new GrassBlade('aIframeByName.unselectFrame', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameByNameWithParam() {
        MadcowMappings.addMapping(testCase, 'aIframeByName', ['name': 'aIframeName']);
        GrassBlade blade = new GrassBlade('aIframeByName.unselectFrame = Bob', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameByInvalidName() {
        GrassBlade blade = new GrassBlade('aIframeIdWrong.unselectFrame = A link that isn\'t a link is still a link', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameByXPath() {
        MadcowMappings.addMapping(testCase, 'aIframeXPath', ['xpath': '//iframe[@id=\'aIframeId\']']);
        GrassBlade blade = new GrassBlade('aIframeXPath.unselectFrame', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameByText() {
        MadcowMappings.addMapping(testCase, 'aIframeText', ['text': 'Like']);
        GrassBlade blade = new GrassBlade('aIframeText.unselectFrame', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameEmpty() {
        GrassBlade blade = new GrassBlade('aIframeId.unselectFrame = ', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameNoSelectorNoParams() {
        try {
            GrassBlade blade = new GrassBlade('unselectFrame', testCase.grassParser);
            assertFalse(unselectFrame.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('No \'.\' or \'=\' found - doesn\'t appear to do anything.', e.message);
        }
    }

    void testUnselectFrameNoParams() {
        GrassBlade blade = new GrassBlade('page.unselectFrame', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameOnPage() {
        GrassBlade blade = new GrassBlade('unselectFrame = Madcow WebDriver Runner Test HTML', testCase.grassParser);
        unselectFrameValid(blade, true);
    }

    void testUnselectFrameNotOnPage() {
        GrassBlade blade = new GrassBlade('unselectFrame = This won\'t be on the page', testCase.grassParser);
        unselectFrameValid(blade, true);
    }
}
