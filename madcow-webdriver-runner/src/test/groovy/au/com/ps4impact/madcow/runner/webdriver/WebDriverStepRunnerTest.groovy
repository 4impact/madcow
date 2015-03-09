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

package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParser
import au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.runner.webdriver.driver.remote.MadcowRemoteWebDriver
import au.com.ps4impact.madcow.step.MadcowStep
import groovy.mock.interceptor.MockFor

/**
 * Test for running WebDriver grass
 *
 * @author Gavin Bunney
 */
class WebDriverStepRunnerTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('WebDriverStepRunnerTest', MockMadcowConfig.getMadcowConfig());
    WebDriverBladeRunner fakeBladeRunner = new MockWebDriverBladeRunner();

    public void testDefaultSelector() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner(testCase, [:]);
        assertEquals('htmlid', stepRunner.defaultSelector);
    }

    public void testDefaultBrowser() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner(testCase, [:]);
        assertEquals(MadcowHtmlUnitDriver.class, stepRunner.driverType.driverClass);
    }

//    public void testRemoteNotInitialised() {
//        WebDriverStepRunner stepRunner = new WebDriverStepRunner(testCase,
//                ['browser':'REMOTE',
//                 'remoteServerUrl': 'http://webdriver.4impact.net.au:4444/wd/hub',
//                 'emulate': 'firefox']);
//        assertNull(stepRunner.driver)
//        assertNull(stepRunner?.driver?.class)
//    }
//
//    public void testRemoteExecuteCausesInitialised() {
//        WebDriverStepRunner stepRunner = new WebDriverStepRunner(testCase,
//                ['browser':'REMOTE',
//                'remoteServerUrl': 'http://4impactmadcow:ba4dc6eb-1101-45c5-a1b1-4bfd3e793497@ondemand.saucelabs.com:80/wd/hub',
//                'emulate': 'firefox']);
//        assertNull(stepRunner.driver)
//        assertNull(stepRunner?.driver?.class)
//        def step = new MadcowStep(testCase, new GrassBlade("invokeUrl = http://www.google.com", testCase.grassParser), null);
//        def mockWebDriverBladeRunner = new MockFor(WebDriverBladeRunner.class)
//        mockWebDriverBladeRunner.demand.getBladeRunner {
//            return fakeBladeRunner
//        }
//        mockWebDriverBladeRunner.use {
//            stepRunner.execute(step)
//        }
//        assertNotNull(stepRunner.driver)
//        assertNotNull(stepRunner?.driver?.class)
//        assertEquals(MadcowRemoteWebDriver.class, stepRunner.driver.class);
//    }


    public void testBrowserNotFound() {
        try {
            WebDriverStepRunner stepRunner = new WebDriverStepRunner(null, ['browser':'tent.tent.tennis.tent']);
            fail('should always exception with ClassNotFoundException');
        } catch (e) {
            assertTrue( e.message.startsWith("The specified Browser 'TENT.TENT.TENNIS.TENT' cannot be found"));
        }
    }

    class MockWebDriverBladeRunner extends WebDriverBladeRunner {
        void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
            //
        }
    }
}
