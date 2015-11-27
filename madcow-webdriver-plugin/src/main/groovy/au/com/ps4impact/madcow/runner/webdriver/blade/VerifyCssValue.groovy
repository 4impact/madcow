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
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.WebElement

/**
 * This class is so that you can Check an elements actual css values
 *
 * @author Tom Romano
 */

class VerifyCssValue extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        WebElement element = findElement(stepRunner, step);

        boolean foundMatch = false
        int matches = 0;
        String errorMessage = "";

        if (element!=null){
            //if a map provided
            if (Map.class.isInstance(step.blade.parameters)) {

                Map<String, String> expected = step.blade.parameters as Map;

                expected.each { key, value ->

                    String elementCssValue = element.getCssValue(key as String);

                    if (elementCssValue.equals(value)){
                         matches++;
                    }else{
                        errorMessage+="Expected CSS Value $key : $value, Actual Value $key : $elementCssValue. "
                    }
                }

                if (matches==expected.keySet().size()){
                    foundMatch = true;
                }
            }

            //if a string is provided
            if (String.class.isInstance(step.blade.parameters)){
                List<String> expectedValues = (StringUtils.trim(step.blade.parameters as String))?.split(';')?.toList();

                //split then on the :
                expectedValues.each { cssToMatch ->

                    String[] keyValuePair = StringUtils.trim(cssToMatch).split(":");

                    String key = StringUtils.trim(keyValuePair[0])
                    String value = StringUtils.trim(keyValuePair[1]);

                    String elementCssValue = element.getCssValue(key);

                    if (elementCssValue.equals(value)){
                        matches++;
                    }else{
                        errorMessage+="Expected CSS Value $key : $value, Actual Value $key : $elementCssValue. "
                    }
                }

                if (matches==expectedValues.size()){
                    foundMatch = true;
                }
            }

            if (foundMatch) {
                step.result = MadcowStepResult.PASS();
            } else {
                step.result = MadcowStepResult.FAIL(errorMessage);
            }
        }
    }

    /**
     * Allow verifying the element is empty.
     */
    protected boolean allowEmptyParameterValue() {
        return true;
    }

    /**
     * Allow only these items to be passed in as params
     * @return map string and list
     */
    protected List<Class> getSupportedParameterTypes() {
        return [Map.class, String.class];
    }

    /**
     * Supported types of blades.
     */
    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    /**
     * Types of supported selectors.
     */
    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.ID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH,
                ];
    }
}

