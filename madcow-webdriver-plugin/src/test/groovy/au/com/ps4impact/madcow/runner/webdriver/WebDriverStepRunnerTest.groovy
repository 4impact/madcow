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

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver
import au.com.ps4impact.madcow.step.MadcowStep

/**
 * Test for running WebDriver grass
 *
 * @author Gavin Bunney
 */
class WebDriverStepRunnerTest extends GroovyTestCase {

    public void testRunInlineScript() {
        String grassScriptString = """
            invokeUrl = ADDRESSBOOK
            @expectedValue = Search Address

            # verify the text exists on the page
            verifyText = @expectedValue
            verifyText = Create Address

            testsite_menu_createAddress.verifyExists
            testsite_menu_createAddress.clickLink
            verifyText = Address Line 1
            verifyText = Check For Duplicates
        """;
        ArrayList<String> grassScript = new ArrayList<String>();
        grassScriptString.eachLine { line -> grassScript.add(line) }

        MadcowTestCase testCase = new MadcowTestCase('WebDriverStepRunnerTest-testRunIt', grassScript);
        testCase.execute();
    }

    public void testRunInlineScriptUsingStoredParams() {
        String grassScriptString = """
            invokeUrl = ADDRESSBOOK
            @expectedValue = Search Address

            addressLines.value = STORETHIS
            addressLines.store = storedValue

            addressLines.checkValue = @storedValue

            addressLines.value = Why would you want to STORETHIS
            verifyText = "Why would you want to @{storedValue}"

            # verify the text exists on the page
            verifyText = @expectedValue

            testsite_menu_createAddress.clickLink
            verifyText = Check For Duplicates
        """;
        ArrayList<String> grassScript = new ArrayList<String>();
        grassScriptString.eachLine { line -> grassScript.add(line) }

        MadcowTestCase testCase = new MadcowTestCase('WebDriverStepRunnerTest-testRunIt', grassScript);
        testCase.execute();
    }

    MadcowTestCase testCase = new MadcowTestCase('WebDriverStepRunnerTest', MockMadcowConfig.getMadcowConfig());
    WebDriverBladeRunner fakeBladeRunner = new MockWebDriverBladeRunner();

    public void testDefaultSelector() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner(testCase, [:]);
        assertEquals('id', stepRunner.defaultSelector);
    }

    public void testDefaultBrowser() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner(testCase, [:]);
        assertEquals(MadcowHtmlUnitDriver.class, stepRunner.driverType.driverClass);
    }

    public void testDefaultBrowserResized() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner(testCase, ['windowWidth': '300', 'windowHeight': '920']);
        assertEquals(MadcowHtmlUnitDriver.class, stepRunner.driverType.driverClass);
        assertEquals(300, stepRunner.driver.manage().window().size.width);
        assertEquals(920, stepRunner.driver.manage().window().size.height);
    }

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
