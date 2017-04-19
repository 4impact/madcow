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

class VerifyRegexTextTest extends AbstractBladeTestCase {

    protected verifyRegexTextContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testVerifyRegexTextOnElementByHtmlId() {
        GrassBlade blade = new GrassBlade('textDiv.verifyRegexText = I ate 23[\\w\\s]*2day. Roxor.', testCase.grassParser);
        verifyRegexTextContents(blade, true);
    }

    @Test
    void testVerifyRegexTextOnElementByName() {
        MadcowMappings.addMapping(testCase, 'squid', ['name': 'textDivName']);
        GrassBlade blade = new GrassBlade('squid.verifyRegexText = I ate 23 (children|monkeys) for lunch 2day. Roxor.', testCase.grassParser);
        verifyRegexTextContents(blade, true);
    }

    @Test
    void testVerifyRegexTextOnElementByXpath() {
        MadcowMappings.addMapping(testCase, 'squid', ['xpath': "//div[@id='textDiv']"]);
        GrassBlade blade = new GrassBlade('squid.verifyRegexText = .*monkeys.*', testCase.grassParser);
        verifyRegexTextContents(blade, true);
    }

    @Test
    void testVerifyRegexTextOnPage() {
        GrassBlade blade = new GrassBlade('verifyRegexText = Column Number [\\d]', testCase.grassParser);
        verifyRegexTextContents(blade, true);
    }

    @Test
    void testVerifyRegexTextFailingOnElementByHtmlId() {
        GrassBlade blade = new GrassBlade('textDiv.verifyRegexText = I ate 23[\\w]*2day. Roxor.', testCase.grassParser);
        verifyRegexTextContents(blade, false);
    }

    @Test
    void testVerifyRegexTextFailingOnElementByName() {
        MadcowMappings.addMapping(testCase, 'squid', ['name': 'textDivName']);
        GrassBlade blade = new GrassBlade('squid.verifyRegexText = I ate 23 (children|nkeys) for lunch 2day. Roxor.', testCase.grassParser);
        verifyRegexTextContents(blade, false);
    }

    @Test
    void testVerifyRegexTextFailingOnElementByXpath() {
        MadcowMappings.addMapping(testCase, 'squid', ['xpath': "//div[@id='textDiv']"]);
        GrassBlade blade = new GrassBlade('squid.verifyRegexText = monkeys.*', testCase.grassParser);
        verifyRegexTextContents(blade, false);
    }

    @Test
    void testVerifyRegexTextFailingOnPage() {
        GrassBlade blade = new GrassBlade('verifyRegexText = Column Number x', testCase.grassParser);
        verifyRegexTextContents(blade, false);
    }
}
