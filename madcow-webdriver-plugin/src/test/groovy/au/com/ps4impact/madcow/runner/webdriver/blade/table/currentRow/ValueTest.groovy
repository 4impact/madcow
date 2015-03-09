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
 * Test for the table CheckValue blade runner.
 *
 * @author Gavin Bunney
 */
class ValueTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def value;
    String testHtmlFilePath;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('ValueTest', new MadcowConfig(), []);
        value = new Value();
        testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;
    }

    protected MadcowStep executeBlade(GrassBlade blade, boolean reloadPage = true) {
        if (reloadPage){
            (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        }
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        return step;
    }

    protected MadcowStep verifyTableValue(GrassBlade blade, boolean shouldPass, boolean reloadPage = true) {
        MadcowStep step = executeBlade(blade, reloadPage);
        assertEquals(shouldPass, step.result.passed());
        return step;
    }

    void testSetThenCheckValue() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade, true);

        blade = new GrassBlade("theTable.table.currentRow.value = ['Column Number 1' : 'Tennis']", testCase.grassParser);
        verifyTableValue(blade, true, false);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = [\'Column Number 1\' : \'Tennis\']', testCase.grassParser);
        verifyTableValue(blade, true, false);
    }

    void testSetValueNeedToSelectRowFirst() {
        GrassBlade blade = new GrassBlade("theTable.table.currentRow.value = ['Column Number 1' : 'Tent']", testCase.grassParser);
        MadcowStep step = verifyTableValue(blade, false);
        assertEquals('No row has been selected - call selectRow first', step.result.message);
    }

    void testSetValueMapOnly() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 1\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.value = Tennis', testCase.grassParser);
        verifyTableValue(blade, false);
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.currentRow.value', testCase.grassParser);
            assertFalse(value.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
