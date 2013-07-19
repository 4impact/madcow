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

package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * Test for the table CheckValueContains blade runner.
 *
 * @author Tom Romano
 */
class CheckValueContainsTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('CheckValueContainsTest', new MadcowConfig(), []);
    CheckValueContains checkValueContains = new CheckValueContains();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected MadcowStep executeBlade(GrassBlade blade) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        return step;
    }

    protected MadcowStep verifyTableCheckValueContains(GrassBlade blade, boolean shouldPass) {
        MadcowStep step = executeBlade(blade);
        assertEquals(shouldPass, step.result.passed());
        return step;
    }

    void testCheckValueContains() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValueContains = [\'Column Number 2\' : \'try\']', testCase.grassParser);
        verifyTableCheckValueContains(blade, true);
    }

    void testCheckValueContainsInputField() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValueContains = [\'Column Number 1\' : \'Value\']', testCase.grassParser);
        verifyTableCheckValueContains(blade, true);
    }

    void testCheckValueContainsFail() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValueContains = [\'Column Number 2\' : \'This will fail\']', testCase.grassParser);
        MadcowStep step = verifyTableCheckValueContains(blade, false);
        assertEquals("Expected: 'This will fail', Present: 'Country'", step.result.message);
    }

    void testCheckValueContainsMultiples() {
        GrassBlade blade = new GrassBlade("theTable.table.selectRow = ['Column Number 2' : 'Country']", testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade("theTable.table.currentRow.checkValueContains = ['Column Number 1' : 'Input Value', 'Column Number 2' : 'Coun']", testCase.grassParser);
        verifyTableCheckValueContains(blade, true);
    }

    void testCheckValueContainsNeedToSelectRowFirst() {
        GrassBlade blade = new GrassBlade("theTable.table.currentRow.checkValueContains = ['Column Number 1' : 'Input']", testCase.grassParser);
        MadcowStep step = verifyTableCheckValueContains(blade, false);
        assertEquals('No row has been selected - call selectRow first', step.result.message);
    }

    void testCheckValueContainsMapOnly() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValueContains = Country', testCase.grassParser);
        verifyTableCheckValueContains(blade, false);
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.currentRow.checkValueContains', testCase.grassParser);
            assertFalse(checkValueContains.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
