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

package au.com.ps4impact.madcow.runner.webdriver.blade.table.countRows

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.mappings.MadcowMappings
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.runner.webdriver.blade.AbstractBladeTestCase
import org.junit.Test

import static groovy.test.GroovyAssert.*

/**
 * Test for Table GreaterThanOrEquals blade runner.
 *
 * @author Paul Bevis
 */
class CountRowsLessThanOrEqualsTest extends AbstractBladeTestCase {

    def countRowsLessThanOrEquals = new LessThanOrEquals();

    protected verifyCountRow(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }
    /**
     * EQUALS TESTING
     */
    @Test
    void testCountByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('theTable.table.countRows.lessThanOrEquals = 4', testCase.grassParser);
        verifyCountRow(blade, false);

        // explicit id, with correct number of rows
        MadcowMappings.addMapping(testCase, 'theTableMapping', ['id': 'theTable']);
        blade = new GrassBlade('theTableMapping.table.countRows.lessThanOrEquals = 5', testCase.grassParser);
        verifyCountRow(blade, true);

        // incorrect number of rows
        blade = new GrassBlade('theTableMapping.table.countRows.lessThanOrEquals = 4', testCase.grassParser);
        verifyCountRow(blade, false);
    }

    @Test
    void testCountByName() {
        MadcowMappings.addMapping(testCase, 'theTableMapping', ['name': 'theTableName']);
        GrassBlade blade = new GrassBlade('theTableMapping.table.countRows.lessThanOrEquals = 5', testCase.grassParser);
        verifyCountRow(blade, true);
    }

    @Test
    void testCountByXPath() {
        MadcowMappings.addMapping(testCase, 'theTableMapping', ['xpath': '//table[@id=\'theTable\']']);
        GrassBlade blade = new GrassBlade('theTableMapping.table.countRows.lessThanOrEquals = 5', testCase.grassParser);
        verifyCountRow(blade, true);
    }

    @Test
    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.countRows.lessThanOrEquals = 5', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(countRowsLessThanOrEquals.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [ID, NAME, XPATH] are supported.', e.message);
        }
    }

    @Test
    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.countRows.lessThanOrEquals = 5', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(countRowsLessThanOrEquals.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [ID, NAME, XPATH] are supported.', e.message);
        }
    }

    @Test
    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.countRows.lessThanOrEquals', testCase.grassParser);
            assertFalse(countRowsLessThanOrEquals.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
