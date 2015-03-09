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
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.Assert.*

import static junit.framework.Assert.assertEquals
import static org.junit.Assert.assertFalse;

/**
 * Test for the ResizeBrowser BladeRunner.
 *
 * @author Tom Romano
 */
class ResizeBrowserTest {

    void testDummy() {

    }

//    MadcowConfig config
//    MadcowTestCase testCase
//    def resizeBrowser
//    def invokeUrl
//    String testHtmlFilePath
//
//    @Before
//    public void setUp() {
//        config = new MadcowConfig('DEV','conf/madcow-config-remote-firefox.xml')
//        testCase = new MadcowTestCase('ResizeBrowserTest', config, [])
//        resizeBrowser = new ResizeBrowser();
//        invokeUrl = new InvokeUrl()
//        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
//    }
//
//    protected verifyResizeBrowserExecution(GrassBlade blade, boolean shouldPass) {
//        GrassBlade startBlade = new GrassBlade("invokeUrl = http://madcow-test.4impact.net.au:8080/madcow-test-site-2", testCase.grassParser)
//        MadcowStep step1 = new MadcowStep(testCase, startBlade, null);
//        testCase.stepRunner.execute(step1);
//        if (!step1.result.failed()){
//            MadcowStep step2 = new MadcowStep(testCase, blade, step1);
//            testCase.stepRunner.execute(step2);
//            assertEquals(shouldPass, step2.result.passed());
//        }
//    }
//
//    protected verifyResizeBrowserExecution(GrassBlade blade, boolean shouldPass, String resultingMessage) {
//        GrassBlade startBlade = new GrassBlade("invokeUrl = http://madcow-test.4impact.net.au:8080/madcow-test-site-2", testCase.grassParser)
//        MadcowStep step1 = new MadcowStep(testCase, startBlade, null);
//        testCase.stepRunner.execute(step1);
//        if (!step1.result.failed()){
//            MadcowStep step2 = new MadcowStep(testCase, blade, step1);
//            testCase.stepRunner.execute(step2);
//            assertEquals(shouldPass, step2.result.passed());
//            assertEquals(resultingMessage, step2.result.message)
//        }
//    }
//
//
//    @Test
//    void testResizeBrowserByHtmlId() {
//        // defaults to html id
//        GrassBlade blade = new GrassBlade('addressLines.resizeBrowser', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, false);
//    }
//
//    @Test
//    void testResizeBrowserByHtmlIdMapping() {
//        // explicit htmlid
//        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'addressLines']);
//        GrassBlade blade = new GrassBlade('aLinkId.resizeBrowser', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, false);
//    }
//
//    @Test
//    void testResizeBrowserWithParams() {
//        GrassBlade blade = new GrassBlade('resizeBrowser = [\'320\',\'480\']', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, true);
//    }
//
//    @Test
//    void testResizeBrowserWithParamMap() {
//        GrassBlade blade = new GrassBlade('resizeBrowser = [width: 320 , height: 480 ]', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, true);
//    }
//
//    @Test
//    void testResizeBrowserWithParamsAndFirefox() {
//        GrassBlade blade = new GrassBlade('resizeBrowser = [\'320\',\'480\']', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, true);
//    }
//
//    @Test
//    void testResizeBrowserByName() {
//        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
//        GrassBlade blade = new GrassBlade('aLinkName.resizeBrowser', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, false);
//    }
//
//    @Test
//    void testResizeBrowserByXPath() {
//        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
//        GrassBlade blade = new GrassBlade('aLinkXPath.resizeBrowser', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, false);
//    }
//
//    @Test
//    void testResizeBrowserByText() {
//        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
//        GrassBlade blade = new GrassBlade('aLinkText.resizeBrowser', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, false);
//    }
//
//    @Test
//    void testResizeBrowserDoesNotExist() {
//        GrassBlade blade = new GrassBlade('aLinkThatDoesntExist.resizeBrowser', testCase.grassParser);
//        verifyResizeBrowserExecution(blade, false);
//    }
//
//    @Test
//    void testDefaultMappingSelectorNoParams() {
//        try{
//            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.resizeBrowser', testCase.grassParser);
//            assertFalse(resizeBrowser.isValidBladeToExecute(blade));
//            fail('should always exception');
//        } catch (e) {
//            assertEquals('Unsupported grass format. Parameter must have a value supplied.', e.message);
//        }
//
//    }
//
//    @Test
//    void testMappingSelectorInvalidRequired() {
//        try {
//            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.resizeBrowser', testCase.grassParser);
//            blade.mappingSelectorType = 'invalidOne';
//            assertFalse(resizeBrowser.isValidBladeToExecute(blade));
//            fail('should always exception');
//        } catch (e) {
//            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, TEXT, NAME, XPATH, CSS] are supported.', e.message);
//        }
//
//    }
//
//    @Test
//    void testMappingSelectorRequired() {
//        try {
//            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.resizeBrowser', testCase.grassParser);
//            blade.mappingSelectorType = null;
//            assertFalse(resizeBrowser.isValidBladeToExecute(blade));
//            fail('should always exception');
//        } catch (e) {
//            assertEquals('Unsupported grass format. Parameter must have a value supplied.', e.message);
//        }
//
//    }
//
//    @Test
//    void testMappingSelectorNotSupported() {
//        GrassBlade blade = new GrassBlade('testsite_menu_createAddress.resizeBrowser = yeah yeah', testCase.grassParser);
//        String message = "You cannot specify a selector for the ResizeBrowser madcow operation"
//        verifyResizeBrowserExecution(blade, false, message);
//
//    }
//
//    @Test
//    void testParametersNotSupported() {
//        GrassBlade blade = new GrassBlade('resizeBrowser = yeah yeah', testCase.grassParser);
//        String message = "ResizeBrowser operation requires two numeric parameters of [width,height] not yeah yeah"
//        verifyResizeBrowserExecution(blade, false, message);
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        testCase.stepRunner.finishTestCase();
//    }
}
