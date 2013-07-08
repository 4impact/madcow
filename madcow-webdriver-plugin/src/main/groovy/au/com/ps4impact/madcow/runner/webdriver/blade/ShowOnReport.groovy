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
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * ShowOnReport.
 *
 * @author Gavin Bunney
 */
class ShowOnReport extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        String elementText = getElementText(findElement(stepRunner, step));

        if (Map.class.isInstance(step.blade.parameters)) {

            Map paramMap = step.blade.parameters as Map;
            if (((paramMap.name ?: '') == '') || ((paramMap.format ?: '') == '')) {
                step.result = MadcowStepResult.FAIL("Both 'name' and 'format' are required parameters when specifying an advanced showOnReport operation");
                return;
            }

            step.testCase.reportDetails.put(paramMap.name, paramMap.format.replaceAll('%s', elementText));
        } else {
            step.testCase.reportDetails.put(step.blade.parameters as String, elementText);
        }

        step.result = MadcowStepResult.PASS();
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected List<Class> getSupportedParameterTypes() {
        return [Map.class, String.class];
    }
}
