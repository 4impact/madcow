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
 * Test class for Blade Runner.
 *
 * @author Gavin Bunney
 */
class BladeRunnerTest extends GroovyTestCase {

    public class MockBladeRunner extends BladeRunner {
        void execute(MadcowStepRunner stepRunner, MadcowStep step) { }
    }

    public class MockInvalidBladeRunner extends BladeRunner {
        MockInvalidBladeRunner() {
            throw new Exception("No BladeRunner for you!");
        }
        void execute(MadcowStepRunner stepRunner, MadcowStep step) { }
    }
    
    void testBladeRunnerCreated() {
        def bladeRunner = BladeRunner.getBladeRunner('au.com.ps4impact.madcow.step', 'BladeRunnerTest$MockBladeRunner');
        bladeRunner.execute(null, null);
    }
    
    void testBladeRunnerDoesNotExist() {
        try {
            BladeRunner.getBladeRunner('', 'tent');
            fail('should always exception with ClassNotFoundException');
        } catch (e) {
            assertTrue( e.message.startsWith("The specified BladeRunner '.tent' cannot be found"));
        }
    }

    void testBladeRunnerIsNotABladeRunner() {
        try {
            BladeRunner.getBladeRunner('au.com.ps4impact.madcow.util', 'ResourceFinder');
            fail('should always exception with ClassCastException');
        } catch (e) {
            assertTrue(e.message.startsWith("The specified BladeRunner 'au.com.ps4impact.madcow.util.ResourceFinder' isn't a BladeRunner!"));
        }
    }

    void testBladeRunnerUnknownException() {
        try {
            BladeRunner.getBladeRunner('au.com.ps4impact.madcow.step', 'BladeRunnerTest$MockInvalidBladeRunner');
            fail('should always exception');
        } catch (e) {
            assertTrue(e.message.startsWith("Unexpected error creating the BladeRunner"));
        }
    }
}
