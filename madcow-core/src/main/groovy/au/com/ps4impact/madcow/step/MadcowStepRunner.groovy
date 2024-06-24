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

package au.com.ps4impact.madcow.step

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * A Step Runner is the invocation hook for executing an individual MadcowStep.
 *
 * @author Gavin Bunney
 */
abstract class MadcowStepRunner {

    public MadcowTestCase testCase;
    private boolean isFailed = false

    public MadcowStepRunner() {
        this(null);
    }

    MadcowStepRunner(MadcowTestCase testCase) {
        this(testCase, new HashMap<String, String>());
        this.testCase = testCase;
    }
    
    MadcowStepRunner(MadcowTestCase testCase, HashMap<String, String> parameters) {
        // overridden in children
    }

    public boolean isFailed(){
        return isFailed
    }

    public boolean setFailed(){
        isFailed = true
    }
    /**
     * Execute a MadcowStep. This is the main entry point for calling
     * out to a MadcowStepRunner.
     */
    public abstract void execute(MadcowStep step);

    /**
     * Determine if the step runner has a blade runner capable of executing the step.
     * This is used during test 'compilation' to see if it can even be done.
     */
    public abstract boolean hasBladeRunner(GrassBlade blade);

    /**
     * Retrieve the default mappings selector this step runner.
     * This is used as the 'type' of selector when no type is given.
     */
    public abstract String getDefaultSelector();

    /**
     * Called to clean up after finishing a test case.
     */
    public abstract void finishTestCase();
}
