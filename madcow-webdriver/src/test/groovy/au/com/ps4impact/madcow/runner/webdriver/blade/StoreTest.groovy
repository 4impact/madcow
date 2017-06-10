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
 * Test for the StoreTest BladeRunner.
 *
 * @author Andy Souyave
 */
class StoreTest extends AbstractBladeTestCase {

    protected verifyStoreContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testStoreValueTest () {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.checkValue = A link', testCase.grassParser);
        verifyStoreContents(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'mapping', ['id': 'aLinkId']);
        blade = new GrassBlade('mapping.store = storedParam', testCase.grassParser);
        verifyStoreContents(blade, true);
    }

    @Test
    void testStoreByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.store = A link', testCase.grassParser);
        verifyStoreContents(blade, true);

        // explicit id
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        blade = new GrassBlade('aLinkId.store = A link', testCase.grassParser);
        verifyStoreContents(blade, true);
    }

    @Test
    void testStoreByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.store = A link', testCase.grassParser);
        verifyStoreContents(blade, true);
    }

    @Test
    void testStoreByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.store = A link', testCase.grassParser);
        verifyStoreContents(blade, true);
    }

    @Test
    void testStoreByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.store = A link', testCase.grassParser);
        verifyStoreContents(blade, true);
    }

    @Test
    void testStoreEmpty() {
        GrassBlade blade = new GrassBlade('anEmptyParagraphId.store = ', testCase.grassParser);
        verifyStoreContents(blade, false);
    }

    @Test
    void testStoreDateTest () {
        // defaults to html id
        GrassBlade blade = new GrassBlade('tblDate.store = saveDate', testCase.grassParser);
        verifyStoreContents(blade, true);
    }

    @Test
    void testStoreDateTimeTest () {
        // defaults to html id
        GrassBlade blade = new GrassBlade('tblDateTime.store = saveDateTime', testCase.grassParser);
        verifyStoreContents(blade, true);
    }
}
