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

package au.com.ps4impact.madcow.grass;

import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Test for the MadcowTestCase class.
 *
 * @author Gavin Bunney
 */
class GrassParseExceptionStepTest extends GroovyTestCase {

    void testParseExceptionReturnsStep() {
        MadcowTestCase testCase = new MadcowTestCase('testCreateAndParse', MockMadcowConfig.getMadcowConfig(), ['notAValidOperation = will fail']);

        try {
            testCase.parseScript();
            fail('should always exception')
        } catch (RuntimeException re) {
            def parseErrorStep = testCase.steps.first();
            assertTrue(parseErrorStep.result.parseError());
            assertEquals('Unsupported operation \'notAValidOperation\'', parseErrorStep.result.detailedMessage);
        }
    }
}