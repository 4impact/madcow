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

package au.com.ps4impact.madcow.step;
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestCase
import java.text.DecimalFormat
import org.apache.commons.lang3.time.StopWatch;

/**
 * A step of an individual madcow test case.
 *
 * @author Gavin Bunney
 */
class MadcowStep {

    GrassBlade blade;
    MadcowTestCase testCase;
    MadcowStep parent;
    ArrayList<MadcowStep> children;
    MadcowStepResult result;
    Node env;
    int sequenceNumber;

    public StopWatch stopWatch;

    protected static DecimalFormat SEQUENCE_NUMBER_FORMAT = new DecimalFormat('0000000');
    protected static final DecimalFormat TIME_SECONDS_FORMAT = new DecimalFormat("########.###");

    /**
     * Create a new Madcow Step.
     */
    MadcowStep(MadcowTestCase testCase, GrassBlade blade, MadcowStep parent) {
        this.testCase   = testCase;
        this.env        = testCase.madcowConfig.environment;
        this.blade      = blade;
        this.parent     = parent;
        this.children   = new ArrayList<MadcowStep>();
        this.sequenceNumber = testCase.steps.size() + 1;
        this.stopWatch  = new StopWatch();

        this.result = MadcowStepResult.NOT_YET_EXECUTED();
    }

    String getSequenceNumberString() {
        return SEQUENCE_NUMBER_FORMAT.format(sequenceNumber);
    }

    /**
     * Overridden default groovy setResult method to allow us
     * to throw upto the parent steps that the child has failed
     */
    public void setResult(MadcowStepResult madcowStepResult) {
        if (this.parent != null)
            this.parent.setResult(madcowStepResult);

        this.result = madcowStepResult;
    }

    public String getStepTimeInSeconds() {
        try {
            return TIME_SECONDS_FORMAT.format(stopWatch.time / 1000);
        } catch (ignored) {
            return TIME_SECONDS_FORMAT.format(0);
        }
    }

    String toString() {
        return "[testCase: $testCase, blade: $blade, parent: $parent, children: $children]";
    }
}