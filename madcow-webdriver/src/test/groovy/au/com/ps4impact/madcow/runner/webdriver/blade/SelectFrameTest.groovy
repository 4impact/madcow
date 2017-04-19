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
 * Test for the SelectFrameTest BladeRunner.
 *
 * @author Tom Romano
 */
class SelectFrameTest extends AbstractBladeTestCase {

    protected selectFrameValid(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testSelectFrameByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aIframeId.selectFrame', testCase.grassParser);
        selectFrameValid(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'aIframe', ['id': 'aIframeId']);
        blade = new GrassBlade('aIframe.selectFrame', testCase.grassParser);
        selectFrameValid(blade, true);
    }

    @Test
    void testSelectFrameValidWithIncorrectParam() {
        GrassBlade blade = new GrassBlade('aIframeId.selectFrame = A link that isn\'t a link is still a link', testCase.grassParser);
        selectFrameValid(blade, true);
    }

    @Test
    void testSelectFrameByName() {
        MadcowMappings.addMapping(testCase, 'aIframeByName', ['name': 'aIframeName']);
        GrassBlade blade = new GrassBlade('aIframeByName.selectFrame', testCase.grassParser);
        selectFrameValid(blade, true);
    }

    @Test
    void testSelectFrameByNameWithParam() {
        MadcowMappings.addMapping(testCase, 'aIframeByName', ['name': 'aIframeName']);
        GrassBlade blade = new GrassBlade('aIframeByName.selectFrame = Bob', testCase.grassParser);
        selectFrameValid(blade, true);
    }

    @Test
    void testSelectFrameByInvalidName() {
        GrassBlade blade = new GrassBlade('aIframeIdWrong.selectFrame = A link that isn\'t a link is still a link', testCase.grassParser);
        selectFrameValid(blade, false);
    }

    @Test
    void testSelectFrameByXPath() {
        MadcowMappings.addMapping(testCase, 'aIframeXPath', ['xpath': '//iframe[@id=\'aIframeId\']']);
        GrassBlade blade = new GrassBlade('aIframeXPath.selectFrame', testCase.grassParser);
        selectFrameValid(blade, true);
    }

    @Test
    void testSelectFrameByText() {
        MadcowMappings.addMapping(testCase, 'aIframeText', ['text': 'Like']);
        GrassBlade blade = new GrassBlade('aIframeText.selectFrame', testCase.grassParser);
        selectFrameValid(blade, false);
    }

    @Test
    void testSelectFrameEmpty() {
        GrassBlade blade = new GrassBlade('aIframeId.selectFrame = ', testCase.grassParser);
        selectFrameValid(blade, true);
    }

    @Test
    void testSelectFrameOnPage() {
        GrassBlade blade = new GrassBlade('selectFrame = Madcow WebDriver Runner Test HTML', testCase.grassParser);
        selectFrameValid(blade, false);
    }

    @Test
    void testSelectFrameNotOnPage() {
        GrassBlade blade = new GrassBlade('selectFrame = This won\'t be on the page', testCase.grassParser);
        selectFrameValid(blade, false);
    }
}
