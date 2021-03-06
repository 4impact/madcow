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
import org.apache.log4j.Logger

/**
 * Blade runner to wait for a specific element/text to appear.
 * Time out after 30 seconds.
 *
 * @author: Gavin Bunney
 */
class WaitFor extends WebDriverBladeRunner {

    private static final int maxSeconds = 30;


    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        boolean found = false;
        for (count in 1..maxSeconds) {
            found = isDesiredConditionFound(count, step, stepRunner, found)
            if (found) break;
            Thread.sleep(1000);
        }

        if (found)
            step.result = MadcowStepResult.PASS();
        else
            step.result = MadcowStepResult.FAIL('Element didn\'t appear within the timeout');

    }

    private boolean isDesiredConditionFound(int count, MadcowStep step, WebDriverStepRunner stepRunner, boolean found) {
        try {
            step.testCase.logInfo("Waiting ("+count+") for: $step.blade.parameters");
            if (StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
                if (StringUtils.isBlank(step.blade.parameters)) {
                    found = true; // request has no pattern to match
                } else {
                    def pageSource = stepRunner.getDriver().getPageSource();
                    if (StringUtils.isNotBlank(pageSource)) {
                        pageSource = trimWhiteSpace(pageSource);
                        def targetString = trimWhiteSpace(step.blade.parameters as String);
                        if (pageSource.contains(targetString)) {
                            found = true;
                        }
                    }
                }
            } else {
                def element = findElement(stepRunner, step);
                if (StringUtils.isNotEmpty((step.blade.parameters ?: '')))
                    found = element.text == step.blade.parameters as String || element.getAttribute('value') == step.blade.parameters as String;
                else
                    found = true;
            }
        } catch (Exception ignored) {
        }
        found
    }

    private String trimWhiteSpace(String aStr) {
        aStr = aStr.trim();
        aStr = StringUtils.remove(aStr,"  ");
        aStr = StringUtils.remove(aStr,"\n");
        aStr = StringUtils.remove(aStr,"\r");
        aStr = StringUtils.remove(aStr,"\t");
        return aStr;
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
