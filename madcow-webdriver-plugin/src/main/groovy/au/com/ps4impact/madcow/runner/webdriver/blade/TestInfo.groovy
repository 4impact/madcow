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
 * TestInfo Blade Runner.
 * Dummy Item really, sole use is to display test
 * description information on to output report
 *
 * @author Tom Romano
 */
class TestInfo extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        def existing = step.testCase.reportDetails.get("Test Summary")
        if (existing){
            step.testCase.reportDetails.put("Test Summary", existing+"\n"+step.sequenceNumberString+": "+step.blade.parameters as String);
        }else{
            step.testCase.reportDetails.put("Test Summary", step.sequenceNumberString+": "+step.blade.parameters as String);
        }

        step.result = MadcowStepResult.NO_OPERATION(step.blade.parameters);
    }

    protected boolean allowNullSelectorType() {
        return true;
    }

    protected boolean enforceNullSelectorType() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected List<Class> getSupportedParameterTypes() {
        return [Map.class, String.class];
    }
}
