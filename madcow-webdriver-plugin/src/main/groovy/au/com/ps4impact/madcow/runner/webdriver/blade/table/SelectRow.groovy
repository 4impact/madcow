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

package au.com.ps4impact.madcow.runner.webdriver.blade.table;

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather

/**
 * The SelectRow table blade runner will save the specified table row.
 *
 * @author Gavin Bunney
 */
class SelectRow extends WebDriverBladeRunner {

    public TableXPather getTableXPather(MadcowStep step) {
        return new TableXPather(step.blade);
    }

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        TableXPather xPather = getTableXPather(step);

        String rowPositionXPath;

        if (step.blade.parameters == "first")
            rowPositionXPath = xPather.getFirstRowPositionXPath()
        else if (step.blade.parameters == "last")
            rowPositionXPath = xPather.getLastRowPositionXPath()
        else if (step.blade.parameters.toString().toLowerCase() ==~ /row\d*/)
            rowPositionXPath = step.blade.parameters.toString().substring(3)
        else
            rowPositionXPath = xPather.getRowPositionXPath(step.blade.parameters)

        step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()] = rowPositionXPath;
        step.result = MadcowStepResult.PASS("Row selected: ${step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()]}");
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    /**
     * Get the list of supported parameter types, which for table operations is a map or string.
     */
    protected List<Class> getSupportedParameterTypes() {
        return [String.class, Map.class];
    }

    /**
     * Get the list of selector types supported by this blade runner.
     * HtmlId, Name and XPath are supported.
     */
    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.HTMLID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH];
    }
}
