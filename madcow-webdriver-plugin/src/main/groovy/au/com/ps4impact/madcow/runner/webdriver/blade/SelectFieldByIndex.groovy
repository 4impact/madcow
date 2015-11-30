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

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.openqa.selenium.By
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The SelectFieldByIndex blade sets a value on a combo box element based on the 0-th based index.
 *
 * @author Tom Romano
 */
class SelectFieldByIndex extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        def element = findElement(stepRunner, step);

        int item = step.blade.parameters as int;
        step.result = makeASingleSelection(stepRunner, step, element, item)
    }


    private MadcowStepResult makeASingleSelection(WebDriverStepRunner stepRunner, MadcowStep step, WebElement element, int selectParam) {
        try {
            boolean result = new WebDriverWait(stepRunner.driver, 10, 1000)
                    .ignoring(StaleElementReferenceException.class)
                    .until(new ExpectedCondition() {
                Boolean apply(WebDriver driver) {
                    try {
                        List<WebElement> options = element.findElements(By.tagName('option'));
                        boolean foundIndex = false;

                        if (selectParam <= options.size()) {
                            foundIndex = true;
                        }

                        if (foundIndex) {
                            new Select(element).selectByIndex(selectParam)
                            return Boolean.valueOf(true)
                        }
                    }
                    catch (StaleElementReferenceException ex) {
                        println('Stale reference caught, retrying...')
                        element = findElement(stepRunner, step)
                        return Boolean.valueOf(false);
                    }
                }
            })
            if (result) {
                return MadcowStepResult.PASS();
            } else {
                return MadcowStepResult.FAIL('Unable to update specified option');
            }
        } catch (Exception e) {
            return MadcowStepResult.FAIL('Unable to find specified option');
        }

    }




    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.ID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.CSS ];
    }

    protected List<Class> getSupportedParameterTypes() {
        return [Integer.class];
    }
}
