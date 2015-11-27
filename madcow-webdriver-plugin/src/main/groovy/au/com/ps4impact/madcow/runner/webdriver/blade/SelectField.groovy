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
 * The SelectField blade sets a value on a combo box element.
 *
 * @author Gavin Bunney
 */
class SelectField extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        def element = findElement(stepRunner, step);

        if (List.class.isInstance(step.blade.parameters)) {
            //check if it is multi select
            def selectField = new Select(element)
            if (selectField.isMultiple()) {
                List<String> paramMap = step.blade.parameters as List;
                def tempResult = [:]
                paramMap.each { selection ->
                    //try to find it and select it
                    tempResult.put(selection, makeASingleSelection(stepRunner, step, element, selection.trim()));
                }
                def failedItems = tempResult.findAll { entry ->
                    if (!(entry.value as MadcowStepResult).passed()) {
                        return entry.key
                    }
                }.keySet()
                if (!failedItems.isEmpty()) {
                    step.result = MadcowStepResult.FAIL('Cannot find a valid option/s for item ' + failedItems.toString());
                    return
                }
                step.result = MadcowStepResult.PASS()
                return
            } else {
                step.result = MadcowStepResult.FAIL('Cannot specify list when select element doesn\'t have multiple attribute');
                return
            }
        } else {
            String text = step.blade.parameters as String;
            step.result = makeASingleSelection(stepRunner, step, element, text.trim())
        }

    }

    private MadcowStepResult makeASingleSelection(WebElement element, String selectParam) {
        List<WebElement> options = element.findElements(By.tagName('option'));
        WebElement foundText = null;
        WebElement foundValue = null;

                    options.each { option ->
                        //if already found option then skip checking rest of options
                        if (foundText || foundValue)
                            return;

                        //first attempt to match on text value
                        if (option.text.trim() == selectParam) {
                            foundText = option;
                        }
                        //then attempt to match on value value
                        if (option.getAttribute("value").trim() == selectParam) {
                            foundValue = option;
                        }
                    }

                    if (foundText != null) {
                        new Select(element).selectByVisibleText(selectParam)
                        return Boolean.valueOf(true)
                    } else if (foundValue != null) {
                        new Select(element).selectByValue(selectParam)
                        return Boolean.valueOf(true)
                    } else {
                        return Boolean.valueOf(false)
                    }
                } catch (StaleElementReferenceException ex) {
                    println('Stale reference caught, retrying...')
                    element = findElement(stepRunner, step)
                    return Boolean.valueOf(false)
                }
            }
        });
        if (result) {
            return MadcowStepResult.PASS();
        } else {
            return MadcowStepResult.FAIL('Unable to update specified option');
        }
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.ID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.CSS];
    }

    protected List<Class> getSupportedParameterTypes() {
        return [List.class, String.class, Integer.class];
    }
}
