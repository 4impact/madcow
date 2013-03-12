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

package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.time.StopWatch

/**
 * This is a Madcow TestCase with an Exception
 *
 * @author Tom Romano
 */
class MadcowTestCaseException extends MadcowTestCase {

    public Exception error;

    public MadcowTestCaseException(String name, MadcowConfig originalMadcowConfig = new MadcowConfig(), ArrayList<String> grassScript = null, Exception error) {
        super(name, grassScript);
        this.error = error;
        this.madcowConfig = originalMadcowConfig;
        //init the stopwatch
        this.stopWatch = new StopWatch();
        this.stopWatch.start();
        //add a error message with a failed first step
        MadcowStep runtimeError = new MadcowStep(this, null, null);
        //MadcowStep runtimeError = new MadcowStep(this, new GrassBlade(grassScript?.get(0), this.grassParser), null);
        runtimeError.result = MadcowStepResult.FAIL(error.toString())
        this.steps.add(runtimeError);
        //set it as the last step run
        this.lastExecutedStep = runtimeError;
        //add a runtime error
        this.reportDetails.put("Runtime Error Occurred",error.toString())
        //end stop watch
        this.stopWatch.stop();
    }

    protected void createStepRunner() {
        //need to override this so that it never attempts to create a step runner the second time...
    }
}
