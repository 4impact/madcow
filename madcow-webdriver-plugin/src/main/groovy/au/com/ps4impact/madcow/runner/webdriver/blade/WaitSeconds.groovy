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

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * WaitSeconds.
 *
 * @author Gavin Bunney
 */
class WaitSeconds extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        int secondsToSleep;
        try {
            secondsToSleep = Integer.parseInt(step.blade.parameters as String);
        } catch (NumberFormatException ignored) {
            step.result = MadcowStepResult.FAIL("Invalid number '${step.blade.parameters}'");
            return;
        }

        // waiting it not really recommended... so log it as warning!
        step.testCase.logWarn("Waiting for $secondsToSleep seconds");

        sleep(secondsToSleep * 1000);
        step.result = MadcowStepResult.PASS();
    }

    protected boolean allowNullSelectorType() {
        return true;
    }

    protected boolean enforceNullSelectorType() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }
}
