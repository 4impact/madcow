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

/**
 * This class is so that you can Check an elements does NOT have a CSS Class value/s
 *
 * @author Tom Romano
 */

class DoesNotHaveClass extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        //get list of element classes from html (and split on spaces if not null)
        List<String> elementClasses = findElement(stepRunner, step).getAttribute('class')?.split(' ')?.toList();

        //set default to blank string for null comparison to pass
        if (elementClasses==null){
            elementClasses = [""];
        }

        //list of expected classes (may not be in order)
        List<String> expectedNotValues = (StringUtils.trim(step.blade.parameters as String))?.split(' ')?.toList();

        boolean foundMatch = false

        //compare if elementClasses list has all of the expected values list
        if (elementClasses.containsAll( expectedNotValues )){
            foundMatch = true;
        }

        if (foundMatch) {
            step.result = MadcowStepResult.FAIL("Expected To Not Have Classes: '${expectedNotValues.toList()}', Present Classes: '${elementClasses.toList()}'");
        } else {
            step.result = MadcowStepResult.PASS();
        }
    }

    /**
     * Allow verifying the element is empty.
     */
    protected boolean allowEmptyParameterValue() {
        return true;
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
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH];
    }
}

