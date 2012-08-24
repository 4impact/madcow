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

package au.com.ps4impact.madcow.grass

import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * GrassParse Exception Step wrapper for a GrassParseException.
 *
 * @author Gavin Bunney
 */
class GrassParseExceptionStep extends MadcowStep {

    /**
     * Create a grass parse exception step for reporting.
     */
    GrassParseExceptionStep(GrassParseException grassParseException, MadcowTestCase testCase) {

        super(testCase, null, null);

        this.blade = new GrassBlade();
        this.blade.line = grassParseException.failingGrass;
        result = MadcowStepResult.PARSE_ERROR("Failed to parse grass: ${grassParseException.failingGrass}");
        result.detailedMessage = grassParseException.message;
    }
}
