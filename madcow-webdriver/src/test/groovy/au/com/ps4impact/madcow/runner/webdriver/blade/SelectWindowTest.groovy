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
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.junit.Test

import static org.junit.Assert.*

class SelectWindowTest extends AbstractBladeTestCase {

    protected verifySelectWindowExecution(GrassBlade blade, boolean shouldPass) {
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testSelectWindowByTitle() {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");

        GrassBlade blade = new GrassBlade('waitFor = Madcow WebDriver Runner Test HTML', testCase.grassParser)
        verifySelectWindowExecution(blade, true)

        // defaults to html id
        blade = new GrassBlade('myBtn.click', testCase.grassParser)
        verifySelectWindowExecution(blade, true)

        blade = new GrassBlade('selectWindow = SampleTitle', testCase.grassParser);
        verifySelectWindowExecution(blade, true)
    }

    @Test
    void testSelectWindowInvalidTitle() {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");

        GrassBlade blade = new GrassBlade('waitFor = Madcow WebDriver Runner Test HTML', testCase.grassParser)
        verifySelectWindowExecution(blade, true)

        // defaults to html id
        blade = new GrassBlade('myBtn.click', testCase.grassParser)
        verifySelectWindowExecution(blade, true)

        blade = new GrassBlade('selectWindow = SampleTitleNotExist', testCase.grassParser);
        verifySelectWindowExecution(blade, false)
    }
}
