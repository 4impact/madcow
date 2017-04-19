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

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult


class SelectField extends CurrentRowBladeRunner {

    protected static final String XPATH_POSTFIX = "//*[local-name() = 'select' and position() = 1]";

    public TableXPather getTableXPather(MadcowStep step) {
        return new TableXPather(step.blade);
    }

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        TableXPather xPather = getTableXPather(step);

        if (!super.validateSelectedRow(xPather, step))
            return;

        GrassBlade originalBlade = step.blade;
        step.blade.parameters.each { String column, String value ->
            println('col/value: '+column+', '+value)

            String cellXPath = xPather.getCellXPath(step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()], column)+ XPATH_POSTFIX;
//            String cellXPath = xPather.getCellXPath(step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()], step.blade.parameters as String) + XPATH_POSTFIX;
            try {
                GrassBlade selectFieldBlade = step.blade.clone() as GrassBlade;
                selectFieldBlade.mappingSelectorType = WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH;
                selectFieldBlade.mappingSelectorValue = cellXPath;
                selectFieldBlade.operation = 'selectField';
                selectFieldBlade.parameters = value;
                step.blade = selectFieldBlade;
                au.com.ps4impact.madcow.runner.webdriver.blade.SelectField selectFieldBladeRunner = new au.com.ps4impact.madcow.runner.webdriver.blade.SelectField();
                selectFieldBladeRunner.execute(stepRunner, step);
            } catch (e) {
                step.result = MadcowStepResult.FAIL("Unexpected exception: $e");
                step.result.detailedMessage = "Cell XPath: ${cellXPath}";
            }

            step.blade = originalBlade;
        }
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected List<Class> getSupportedParameterTypes() {
        return [Map.class];
    }
}
