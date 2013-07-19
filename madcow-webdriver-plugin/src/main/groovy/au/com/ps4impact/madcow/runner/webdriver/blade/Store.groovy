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
 * User: andy.souyave
 * Date: 20/09/12
 * Time: 1:32 PM
 */
class Store extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        if (!StringUtils.isEmpty(step.blade.mappingSelectorValue)) {

            if (findElement(stepRunner, step)) {
                def parser = step.testCase.grassParser
                def storedParamName = step.blade.parameters
                def storedDataParam = "${GrassBlade.DATA_PARAMETER_KEY}${storedParamName}"

                // set the runtime parameter
                parser.setDataParameter(storedDataParam, getElementText(findElement(stepRunner, step)))

                // replace all the stored param placeholders with the corresponding value from the html element
                step.testCase.steps.each { MadcowStep madStep ->

                    if ((madStep?.blade?.parameters as String)?.contains("${GrassBlade.DATA_PARAMETER_KEY}{${storedParamName}}"))
                        madStep.blade.parameters = (madStep?.blade?.parameters as String).replaceAll("${GrassBlade.DATA_PARAMETER_KEY}\\{${storedParamName}\\}", parser.getDataParameter(storedDataParam))
                }

                step.result = MadcowStepResult.PASS();
            } else {
                step.result = MadcowStepResult.FAIL("Element ${step.blade.mappingSelectorValue} was not found for parameter store.");
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
     * Supported types of blades.
     */
    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    /**
     * Types of supported selectors.
     */
    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.HTMLID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.CSS];
    }
}
