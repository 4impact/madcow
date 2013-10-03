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

import au.com.ps4impact.madcow.mock.MockMadcowConfig

/**
 * Test for the Madcow Test Runner class.
 *
 * @author Gavin Bunney
 */
class MadcowTestRunnerTest extends GroovyTestCase {

    void testExecuteTest() {
        MadcowTestRunner.executeTests(['DataParameterTest'], MockMadcowConfig.getMadcowConfig());
        MadcowTestRunner.executeTests(['DataParameterTest.grass'], MockMadcowConfig.getMadcowConfig());
    }

    void testExecuteTestSuite() {
        MadcowTestRunner.executeTests(MockMadcowConfig.getMadcowConfig());
    }

    void testExecuteTestWithStack() {
        def config = MockMadcowConfig.getMadcowConfig(false, true, 'au.com.ps4impact.madcow.mock.MockMadcowStepRunner')
        MadcowTestRunner.executeTests(['ExceptionTest'], config);
        MadcowTestRunner.executeTests(['ExceptionTest.grass'], config);
    }

    void testExecuteNonFound() {
        try {
            MadcowTestRunner.prepareTestSuite(['InvalidTestName'], MockMadcowConfig.getMadcowConfig());
            fail('should always exception')
        } catch (e) {
            assertTrue(e.message.startsWith('Unable to find matches on the classpath for \'**/InvalidTestName.grass\''))
        }
    }

    void testSuiteBuilder() {
        // even though this is in a package callled 'param' in test resources, due to where the execution takes place
        // the relative 'tests' directory is seen as basedir/tests not madcow-core/main/test/resources/tests
        // so the suite automagicness doesn't remove the front of the package - so it just gets lumped into the root suite
        def suite = MadcowTestRunner.prepareTestSuite(['DataParameterTest'], MockMadcowConfig.getMadcowConfig());
        assertEquals('', suite.name)
        assertEquals(1, suite.testCases.size())
        assertEquals(1, suite.size())
        assertEquals(0, suite.children.size())
        assertTrue(suite.testCases.first().name.endsWith('DataParameterTest'))
    }

    void testStepRunnerNotFound() {
        def suite = MadcowTestRunner.prepareTestSuite(['DataParameterTest'], MockMadcowConfig.getMadcowConfig(true, 'au.com.this.doesnt.exist.StepRunner'));
        assertEquals('', suite.name)
        assertEquals(1, suite.testCases.size())
        assertEquals(1, suite.size())
        assertEquals(0, suite.children.size())
        assertTrue(suite.testCases.first().name.endsWith('DataParameterTest'))
    }
}
