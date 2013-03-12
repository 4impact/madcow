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
import org.apache.commons.lang3.StringUtils

/**
 * VerifyNotText exists on a page
 *
 * @author Tom Romano
 */
class VerifyNotText extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        if (StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
            if (!stepRunner.driver.pageSource.contains(step.blade.parameters as String))
                step.result = MadcowStepResult.PASS();
            else
                step.result = MadcowStepResult.FAIL("Page contains text '${step.blade.parameters as String}'");
        } else {
            if (!findElement(stepRunner, step).text.equals(step.blade.parameters as String))
                step.result = MadcowStepResult.PASS();
            else
                step.result = MadcowStepResult.FAIL("Element contains text '${step.blade.parameters as String}'");
        }
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
