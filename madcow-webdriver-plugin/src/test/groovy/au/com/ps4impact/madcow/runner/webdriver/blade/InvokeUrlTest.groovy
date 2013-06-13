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

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.step.MadcowStep

/**
 * Test for InvokeUrl Blade Runner
 *
 * @author Gavin Bunney
 */
class InvokeUrlTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('InvokeUrlTest', new MadcowConfig(), []);
    def invokeUrl = new InvokeUrl();

    void testInvokeUrl() {
        GrassBlade blade = new GrassBlade('invokeUrl = ADDRESSBOOK/test.html', testCase.grassParser);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertTrue(step.result.passed());
        assertTrue(step.result.message.startsWith("URL now:"));
    }

    void testInvokeUrlRefresh() {
        GrassBlade blade = new GrassBlade('invokeUrl = ADDRESSBOOK/test.html', testCase.grassParser);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertTrue(step.result.passed());
        assertTrue(step.result.message.startsWith("URL now:"));
        GrassBlade blade2 = new GrassBlade('invokeUrl = REFRESH', testCase.grassParser);
        MadcowStep step2 = new MadcowStep(testCase, blade2, step);
        testCase.stepRunner.execute(step2);
        assertTrue(step2.result.passed());
        assertTrue(step2.result.message.startsWith("Page Refreshed. URL now:"));
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('someElement.invokeUrl', testCase.grassParser);
            assertFalse(invokeUrl.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }

    void testParameterMustBeSupplied() {
        try {
            GrassBlade blade = new GrassBlade('invokeUrl =   ', testCase.grassParser);
            assertFalse(invokeUrl.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Parameter must have a value supplied.', e.message);
        }
    }

    void testRefreshWithoutInitialPageSetup() {
        GrassBlade blade2 = new GrassBlade('invokeUrl = REFRESH', testCase.grassParser);
        MadcowStep step2 = new MadcowStep(testCase, blade2, null);
        testCase.stepRunner.execute(step2);
        assertTrue(step2.result.passed());
        assertTrue(step2.result.message.startsWith("Page Refreshed. URL now:"));
    }
}
