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
import au.com.ps4impact.madcow.grass.ParseUtils
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils

/**
 * Store parameter value Blade Runner.
 */
class Store extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        if (StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
            return;
        }

        def storedParamName = step.blade.parameters
        def storedDataParam = "${GrassBlade.DATA_PARAMETER_KEY}${storedParamName}"

        String foundValue = null;
        try {
            foundValue = getElementText(findElement(stepRunner, step))
        } catch (Exception ignored) { }

        // xpath run some JS to try and get the value out
        if ((!foundValue || foundValue.length() == 0) && getSelectorType(step.blade.mappingSelectorType) == WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH) {
            foundValue = getElementTextByExecutingXPath(stepRunner, step.blade.mappingSelectorValue);
        }

        if (!foundValue) {
            step.result = MadcowStepResult.FAIL("Element ${step.blade.mappingSelectorValue} was not found for parameter store.");
            return;
        }

        // set the runtime parameter
        step.testCase.grassParser.setDataParameter(storedDataParam, foundValue)
        this.updateStepParameters(step.testCase.steps, storedParamName, storedDataParam)

        step.result = MadcowStepResult.PASS();
    }

    protected void updateStepParameters(ArrayList<MadcowStep> steps, def storedParamName, def storedDataParam) {

        // TODO - this really isn't the best solution. We should have a pre-eval step on each execute that checks for run-time parameters and sets it that way.
        // replace all the stored param placeholders with the corresponding value from the html element
        steps.each { MadcowStep madStep ->
            final parameters = madStep?.blade?.parameters
            String regexToReplace = "${GrassBlade.DATA_PARAMETER_KEY}\\{${storedParamName}\\}"
            if (parameters instanceof String) {
                String replacement = madStep.testCase.grassParser.getDataParameter(storedDataParam)
                replacement = ParseUtils.unDollarify(replacement) ;
                madStep.blade.parameters = parameters.replaceAll(regexToReplace, replacement)
            }
            if (parameters instanceof Map) {
                parameters?.each { entry ->
                    if (entry.value instanceof String) {
                        String replacement = madStep.testCase.grassParser.getDataParameter(storedDataParam)
                        replacement = ParseUtils.unDollarify(replacement) ;
                        entry.value = (entry.value as String).replaceAll(regexToReplace, replacement)
                    }
                }
            }

            if (!madStep.children?.isEmpty()) {
                this.updateStepParameters(madStep.children, storedParamName, storedDataParam)
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
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.ID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.CSS];
    }

    /**
     * Get the list of supported parameter types, which for table operations is a map
     */
    protected List<Class> getSupportedParameterTypes() {
        return [Map.class, String.class]
    }
}
