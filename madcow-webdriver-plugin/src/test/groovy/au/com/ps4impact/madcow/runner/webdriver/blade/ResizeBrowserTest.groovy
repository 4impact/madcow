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
import org.junit.Ignore

/**
 * Test for the ResizeBrowser BladeRunner.
 *
 * @author Tom Romano
 */
@Ignore("Ignored because this will require firefox installed on building machine...")
class ResizeBrowserTest extends GroovyTestCase {

    MadcowConfig config = new MadcowConfig('DEV','conf/madcow-config-firefox.xml')

    MadcowTestCase testCase = new MadcowTestCase('ResizeBrowserTest', config, [])
    def resizeBrowser = new ResizeBrowser();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifyResizeBrowserExecution(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
        testCase.stepRunner.finishTestCase();
    }

    void testResizeBrowserByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.resizeBrowser', testCase.grassParser);
        verifyResizeBrowserExecution(blade, false);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        blade = new GrassBlade('aLinkId.resizeBrowser', testCase.grassParser);
        verifyResizeBrowserExecution(blade, false);
    }

    void testResizeBrowserWithParams() {
        GrassBlade blade = new GrassBlade('resizeBrowser = [\'320\',\'480\']', testCase.grassParser);
        verifyResizeBrowserExecution(blade, true);
    }

    void testResizeBrowserWithParamMap() {
        GrassBlade blade = new GrassBlade('resizeBrowser = [width: 320 , height: 480 ]', testCase.grassParser);
        verifyResizeBrowserExecution(blade, true);
    }

    void testResizeBrowserWithParamsAndFirefox() {
        GrassBlade blade = new GrassBlade('resizeBrowser = [\'320\',\'480\']', testCase.grassParser);
        verifyResizeBrowserExecution(blade, true);
    }

    void testResizeBrowserByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.resizeBrowser', testCase.grassParser);
        verifyResizeBrowserExecution(blade, false);
    }

    void testResizeBrowserByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.resizeBrowser', testCase.grassParser);
        verifyResizeBrowserExecution(blade, false);
    }

    void testResizeBrowserByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.resizeBrowser', testCase.grassParser);
        verifyResizeBrowserExecution(blade, false);
    }

    void testResizeBrowserDoesNotExist() {
        GrassBlade blade = new GrassBlade('aLinkThatDoesntExist.resizeBrowser', testCase.grassParser);
        verifyResizeBrowserExecution(blade, false);
    }

    void testDefaultMappingSelectorNoParams() {
        try{
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.resizeBrowser', testCase.grassParser);
            assertFalse(resizeBrowser.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Parameter must have a value supplied.', e.message);
        }
        testCase.stepRunner.finishTestCase();
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.resizeBrowser', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(resizeBrowser.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, TEXT, NAME, XPATH] are supported.', e.message);
        }
        testCase.stepRunner.finishTestCase();
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.resizeBrowser', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(resizeBrowser.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Parameter must have a value supplied.', e.message);
        }
        testCase.stepRunner.finishTestCase();
    }

    void testMappingSelectorNotSupported() {
        GrassBlade blade = new GrassBlade('testsite_menu_createAddress.resizeBrowser = yeah yeah', testCase.grassParser);
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertFalse(step.result.passed());
        assertEquals("You cannot specify a selector for the ResizeBrowser madcow operation",step.result.message)
        testCase.stepRunner.finishTestCase();
    }

    void testParametersNotSupported() {
        GrassBlade blade = new GrassBlade('resizeBrowser = yeah yeah', testCase.grassParser);
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertFalse(step.result.passed());
        assertEquals("ResizeBrowser operation requires two numeric parameters of [width,height] not yeah yeah", step.result.message)
        testCase.stepRunner.finishTestCase();
    }
}
