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

package au.com.ps4impact.madcow.mock

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Stubbed Madcow Step Runner.
 *
 * @author Gavin Bunney
 */
class MockMadcowStepRunner extends MadcowStepRunner {

    boolean alwaysPass = true;

    MockMadcowStepRunner(MadcowTestCase testCase, HashMap<String, String> parameters) {
        this.testCase = testCase;
        alwaysPass = (parameters.alwaysPass != 'false');
    }

    void execute(MadcowStep step) {
        step.result = alwaysPass ? MadcowStepResult.PASS('Mocked Pass') : MadcowStepResult.FAIL('Mocked Fail');
    }

    boolean hasBladeRunner(GrassBlade blade) {
        switch (blade.operation) {
            case 'notAValidOperation':
                return false
            default:
                return true;
        }
    }

    public String getDefaultSelector() {
        return 'htmlid';
    }

    public void finishTestCase() {
        //
    }
}