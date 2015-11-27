/*
 * Copyright 2013 4impact, Brisbane, Australia
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

package au.com.ps4impact.madcow.runner.webdriver.blade.table.countRows

import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * Checks that an element has greater than or equals the specified number of rows
 *
 * @author Paul Bevis
 */
class GreaterThanOrEquals extends CountRows {

    @Override
    void comparison(WebDriverStepRunner stepRunner, MadcowStep step) {
        def rowNumberToCompare = step.blade.parameters.toInteger()
        if (rows >= rowNumberToCompare) {
            step.result = MadcowStepResult.PASS();
        } else {
            step.result = MadcowStepResult.FAIL("Number of rows found (${rows}) is not greater than or equals the supplied value ($rowNumberToCompare)");
        }
    }
}
