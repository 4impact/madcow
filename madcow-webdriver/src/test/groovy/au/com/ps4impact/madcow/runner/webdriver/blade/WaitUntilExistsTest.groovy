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

import static org.junit.Assert.*

/**
 * Test for the ClickLink BladeRunner.
 *
 * @author Gavin Bunney
 */
class WaitUntilExistsTest extends AbstractBladeTestCase {

    ClickLink clickLink = new ClickLink();

    protected verifyLinkExecution(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testWithoutMapping() {
        GrassBlade blade = new GrassBlade('waitUntilExists = 2', testCase.grassParser);
        verifyLinkExecution(blade, false);

    }

    @Test
    void testWithMapping() {
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        GrassBlade blade = new GrassBlade('aLinkId.waitUntilExists = 2', testCase.grassParser);
        verifyLinkExecution(blade, true);

    }

    @Test
    void testMapParamWithoutSeconds() {
        GrassBlade blade = new GrassBlade("waitUntilExists = [value:'test']", testCase.grassParser);
        verifyLinkExecution(blade, false);
    }

    @Test
    void testMapParamWithoutValue() {
        GrassBlade blade = new GrassBlade("waitUntilExists = [seconds:'2']", testCase.grassParser);
        verifyLinkExecution(blade, false);
    }

    @Test
    void testMapParamWithSecondsAndValue() {
        GrassBlade blade = new GrassBlade("waitUntilExists = [seconds:'2', value:'A link']", testCase.grassParser);
        verifyLinkExecution(blade, true);
    }
}
