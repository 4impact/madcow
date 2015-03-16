/*
 * Copyright 2015 4impact, Brisbane, Australia
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

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.internal.Locatable

/**
 * The Click Blade is used to click on an element - such as radio buttons or html links.
 */
class Click extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        WebElement webElement = findElementForClickStep(stepRunner, step);

        // scroll the element into view
        ((Locatable)webElement).getCoordinates().inViewPort()

        webElement.click();

        step.result = MadcowStepResult.PASS();
    }

    protected WebElement findElementForClickStep(WebDriverStepRunner stepRunner, MadcowStep step) {
        // when there is no mapping selector, we need to use the parameters as the click target
        if (StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
            return stepRunner.driver.findElement(By.linkText(step.blade.parameters as String))
        } else {
            return findElement(stepRunner, step);
        }
    }

    protected boolean allowEmptyParameterValue() {
        return true;
    }

    protected boolean allowNullSelectorType() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.STATEMENT, GrassBlade.GrassBladeType.EQUATION];
    }
}
