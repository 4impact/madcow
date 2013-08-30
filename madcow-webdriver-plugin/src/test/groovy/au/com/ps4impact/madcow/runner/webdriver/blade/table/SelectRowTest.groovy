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

package au.com.ps4impact.madcow.runner.webdriver.blade.table

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.mappings.MadcowMappings
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather

/**
 * Test for Table SelectRow blade runner.
 *
 * @author Gavin Bunney
 */
class SelectRowTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ClickLinkTest', new MadcowConfig(), []);
    def selectRow = new SelectRow();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifySelectRow(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).initialiseDriverWithRetriesIfRequired();
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testSelectByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        verifySelectRow(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'theTableMapping', ['id': 'theTable']);
        blade = new GrassBlade('theTableMapping.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        verifySelectRow(blade, true);
    }

    void testSelectByName() {
        MadcowMappings.addMapping(testCase, 'theTableMapping', ['name': 'theTableName']);
        GrassBlade blade = new GrassBlade('theTableMapping.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        verifySelectRow(blade, true);
    }

    void testSelectByXPath() {
        MadcowMappings.addMapping(testCase, 'theTableMapping', ['xpath': '//table[@id=\'theTable\']']);
        GrassBlade blade = new GrassBlade('theTableMapping.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        verifySelectRow(blade, true);
    }

    void testSelectBySpecialKeyword() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = first', testCase.grassParser);
        verifySelectRow(blade, true);
        TableXPather xPather = new TableXPather(blade);
        assertEquals('1', testCase.runtimeStorage[xPather.getRuntimeStorageKey()]);

        blade = new GrassBlade('theTable.table.selectRow = row2', testCase.grassParser);
        verifySelectRow(blade, true);
        xPather = new TableXPather(blade);
        assertEquals('2', testCase.runtimeStorage[xPather.getRuntimeStorageKey()]);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.selectRow = first', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(selectRow.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.selectRow = first', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(selectRow.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.selectRow', testCase.grassParser);
            assertFalse(selectRow.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
