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
import org.apache.commons.lang3.time.StopWatch

/**
 * Test for InvokeUrl Blade Runner
 *
 * @author Gavin Bunney
 */
class WaitSecondsTest extends GroovyTestCase {

    MadcowTestCase testCase;
    def waitSeconds;

    void setUp() {
        super.setUp();

        testCase = new MadcowTestCase('WaitSecondsTest', new MadcowConfig(), []);
        waitSeconds = new WaitSeconds();
    }

    void testWaitSeconds() {
        StopWatch watch = new StopWatch();
        GrassBlade blade = new GrassBlade('waitSeconds = 2', testCase.grassParser);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        watch.start();
        testCase.stepRunner.execute(step);
        watch.stop();
        assertTrue(step.result.passed());
        assertTrue(watch.time >= 2000);
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('someElement.waitSeconds', testCase.grassParser);
            assertFalse(waitSeconds.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }

    void testParameterMustBeSupplied() {
        try {
            GrassBlade blade = new GrassBlade('waitSeconds =   ', testCase.grassParser);
            assertFalse(waitSeconds.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Parameter must have a value supplied.', e.message);
        }
    }
}
