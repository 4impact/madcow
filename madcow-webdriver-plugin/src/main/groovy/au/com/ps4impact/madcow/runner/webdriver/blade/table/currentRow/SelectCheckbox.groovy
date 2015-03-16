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

package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * User: andy.souyave
 * Date: 24/09/12
 * Time: 10:35 AM
 */
class SelectCheckbox extends CurrentRowBladeRunner {

    protected static final String XPATH_POSTFIX = "//*[local-name() = 'input' and @type = 'checkbox']";

    public TableXPather getTableXPather(MadcowStep step) {
        return new TableXPather(step.blade);
    }

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        TableXPather xPather = getTableXPather(step);

        if (!super.validateSelectedRow(xPather, step))
            return;

        GrassBlade originalBlade = step.blade;

        String cellXPath = xPather.getCellXPath(step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()], step.blade.parameters as String) + XPATH_POSTFIX;
        try {
            GrassBlade selectCheckboxBlade = step.blade.clone() as GrassBlade;
            selectCheckboxBlade.mappingSelectorType = WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH;
            selectCheckboxBlade.mappingSelectorValue = cellXPath;
            selectCheckboxBlade.operation = 'selectCheckbox';
            selectCheckboxBlade.parameters = null;
            step.blade = selectCheckboxBlade;
            au.com.ps4impact.madcow.runner.webdriver.blade.SelectCheckbox selectCheckboxBladeRunner = new au.com.ps4impact.madcow.runner.webdriver.blade.SelectCheckbox();
            selectCheckboxBladeRunner.execute(stepRunner, step);
        } catch (e) {
            step.result = MadcowStepResult.FAIL("Unexpected exception: $e");
            step.result.detailedMessage = "Cell XPath: ${cellXPath}";
        }

        step.blade = originalBlade;
    }

    /**
     * List of supported blade types.
     */
    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    /**
     * Get the list of supported parameter types, which for table operations is a map
     */
    protected List<Class> getSupportedParameterTypes() {
        return [String.class];
    }
}
