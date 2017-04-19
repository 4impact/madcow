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
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Dimension
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * Resizes the current Browser window which is important for responsive design websites.
 *
 * @author Tom Romano
 */
class ResizeBrowser extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        if (!StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
            step.result = MadcowStepResult.FAIL("You cannot specify a selector for the ResizeBrowser madcow operation");
            return;
        }

        if (Map.class.isInstance(step.blade.parameters)) {

            Map paramMap = step.blade.parameters as Map;
            if (((paramMap.width ?: '') == '') || ((paramMap.height ?: '') == '')) {
                step.result = MadcowStepResult.FAIL("Both 'width' and 'height' are required parameters when specifying an advanced ResizeBrowser operation");
                return;
            }

            //width x height
            try{
                //width x height
                Dimension dimension = new Dimension(paramMap.width,paramMap.height);
                stepRunner.driver.manage().window().setSize(dimension);
                step.result = MadcowStepResult.PASS();
            }catch (Exception e){
                MadcowStepResult sr = MadcowStepResult.FAIL("Both 'width' and 'height' are required parameters when specifying an advanced ResizeBrowser operation not ${step.blade.parameters}")
                sr.detailedMessage = e.getMessage();
                step.result = sr;
            }

        } else {
            List<String> expectedList = step.blade.parameters as List<String>;
            if (expectedList.size()==2){
                try{
                    //width x height
                    Dimension dimension = new Dimension(Integer.valueOf(expectedList[0]),Integer.valueOf(expectedList[1]));
                    stepRunner.driver.manage().window().setSize(dimension);
                    step.result = MadcowStepResult.PASS();
                }catch (Exception e){
                    MadcowStepResult sr = MadcowStepResult.FAIL("ResizeBrowser operation requires two numeric parameters of [width,height] not ${step.blade.parameters}")
                    sr.detailedMessage = e.getMessage();
                    step.result = sr;
                }
            }else{
                step.result = MadcowStepResult.FAIL("ResizeBrowser operation requires two numeric parameters of [width,height] not ${step.blade.parameters}");
            }
        }
    }

    protected List<Class> getSupportedParameterTypes() {
        return [Map.class, String.class, List.class];
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
        return false;
    }

}
