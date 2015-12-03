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
import org.openqa.selenium.WebElement

/**
 * The VerifySelectFieldContains blade verify's the contents of a select field.
 *
 * @author Paul Bevis
 */
class VerifySelectFieldNotContains extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        def element = findElement(stepRunner, step);

        if (String.class.isInstance(step.blade.parameters)) {
            step.blade.parameters = [step.blade.parameters];
        }

        List<WebElement> options = element.findElements(By.tagName('option'));

        List<String> presentList = options*.text;
        List<String> expectedList = step.blade.parameters as List<String>;
        boolean bNotMatched = true
        List<String> notMatchList = []

        expectedList.each {
            if (!presentList.contains(it)) {
                bNotMatched = false
                notMatchList.add(it)
            }
        }
        if (bNotMatched) {
            step.result = MadcowStepResult.FAIL('Select field does contain (some of) the specified data');
            step.result.detailedMessage = "Select contains ${presentList}, matched data ${notMatchList}";
        } else {
            step.result = MadcowStepResult.PASS();
        }
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected List<Class> getSupportedParameterTypes() {
        return [String.class, List.class];
    }

    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.ID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.CSS];
    }
}
