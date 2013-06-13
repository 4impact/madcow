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

import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner

/**
 * Blade runner to wait for a specific element/text to appear
 *
 * @author: Gavin Bunney
 */
class WaitFor extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        final boolean found = (1..30).any {

            try {
                if (StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
                    if (step.blade.parameters ?: '' == '' || stepRunner.driver.pageSource.contains(step.blade.parameters as String)){
                        step.testCase.logInfo("Waiting for: $step.blade.parameters");
                        return true;
                    } else {
                        throw new NoSuchElementException();
                    }
                } else {
                    def element = findElement(stepRunner, step);
                    if (StringUtils.isNotEmpty((step.blade.parameters ?: '')))
                        return element.text == step.blade.parameters as String;
                    else
                        return true;
                }
            } catch (Exception ignored) { }

            Thread.sleep(1000);
            return false;
        }

        if (found)
            step.result = MadcowStepResult.PASS();
        else
            step.result = MadcowStepResult.FAIL('Element didn\'t appear within the timeout');
    }

    /**
     * Allow null selectors.
     */
    protected boolean allowNullSelectorType() {
        return true;
    }

    /**
     * Allow verifying the element is empty.
     */
    protected boolean allowEmptyParameterValue() {
        return true;
    }
}
