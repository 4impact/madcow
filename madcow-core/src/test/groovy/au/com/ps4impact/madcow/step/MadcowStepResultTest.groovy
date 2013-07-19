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

/**
 * Test class for Madcow Step Result.
 *
 * @author Gavin Bunney
 */
class MadcowStepResultTest extends GroovyTestCase {

    void testManualResult() {
        def result = new MadcowStepResult(MadcowStepResult.StatusType.PASS, 'All good');
        assertEquals(MadcowStepResult.StatusType.PASS, result.status);
        assertEquals('All good', result.message);
        assertToString(result, 'Status: PASS | Message: All good');
    }

    void testManualResultNoMessage() {
        def result = new MadcowStepResult(MadcowStepResult.StatusType.PASS);
        assertEquals(MadcowStepResult.StatusType.PASS, result.status);
        assertEquals(null, result.message);
        assertToString(result, 'Status: PASS | Message: null');
    }
    
    void testHelperPass() {
        def result = MadcowStepResult.PASS('Done!');
        assertEquals(MadcowStepResult.StatusType.PASS, result.status);
        assertTrue(result.passed());
        assertFalse(result.failed());
        assertFalse(result.noOperation());
        assertEquals('Done!', result.message);
        assertToString(result, 'Status: PASS | Message: Done!')
    }

    void testHelperFail() {
        def result = MadcowStepResult.FAIL('Done!');
        assertEquals(MadcowStepResult.StatusType.FAIL, result.status);
        assertTrue(result.failed());
        assertFalse(result.passed());
        assertFalse(result.noOperation());
        assertEquals('Done!', result.message);
        assertToString(result, 'Status: FAIL | Message: Done!')
    }

    void testHelperParseError() {
        def result = MadcowStepResult.PARSE_ERROR('Operation not supported');
        assertEquals(MadcowStepResult.StatusType.PARSE_ERROR, result.status);
        assertTrue(result.failed());
        assertTrue(result.parseError());
        assertFalse(result.passed());
        assertFalse(result.noOperation());
        assertEquals('Operation not supported', result.message);
        assertToString(result, 'Status: PARSE_ERROR | Message: Operation not supported')
    }

    void testHelperNoOperation() {
        def result = MadcowStepResult.NO_OPERATION('Done!');
        assertEquals(MadcowStepResult.StatusType.NO_OPERATION, result.status);
        assertTrue(result.noOperation());
        assertFalse(result.passed());
        assertFalse(result.failed());
        assertEquals('Done!', result.message);
        assertToString(result, 'Status: NO_OPERATION | Message: Done!')
    }

    void testHelperSkipped() {
        def result = MadcowStepResult.NOT_YET_EXECUTED('Skipped!');
        assertEquals(MadcowStepResult.StatusType.NOT_YET_EXECUTED, result.status);
        assertTrue(result.notYetExecuted());
        assertFalse(result.passed());
        assertFalse(result.failed());
        assertEquals('Skipped!',result.message);
        assertToString(result, 'Status: NOT_YET_EXECUTED | Message: Skipped!')
    }
}
