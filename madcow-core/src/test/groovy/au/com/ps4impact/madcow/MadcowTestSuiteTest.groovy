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
 * Test class for MadcowTestSuite.
 *
 * @author Gavin Bunney
 */
class MadcowTestSuiteTest extends GroovyTestCase {

    void testSuiteLocator() {
        MadcowTestSuite rootSuite = new MadcowTestSuite('');

        MadcowTestSuite searchSuite = new MadcowTestSuite('search', rootSuite);
        searchSuite.children.add(new MadcowTestSuite('addresses', searchSuite));

        rootSuite.children.add(searchSuite);
        rootSuite.children.add(new MadcowTestSuite('create', rootSuite));

        assertEquals('', rootSuite.locateSuite('SomeTestCase').name);
        assertEquals('addresses', rootSuite.locateSuite('search.addresses.TheTest').name);
        assertEquals('search', rootSuite.locateSuite('search.AddressTest').name);
        assertEquals('create', rootSuite.locateSuite('create.CreateTest').name);
    }

    void testSize() {
        MadcowTestSuite rootSuite = new MadcowTestSuite('');
        MadcowTestSuite searchSuite = new MadcowTestSuite('search', rootSuite);
        searchSuite.children.add(new MadcowTestSuite('addresses', searchSuite));
        rootSuite.children.add(searchSuite);

        assertEquals(0, rootSuite.size());

        rootSuite.testCases.add(new MadcowTestCase('testCaseInRoot', MockMadcowConfig.getMadcowConfig()));
        searchSuite.testCases.add(new MadcowTestCase('testCaseInSubSuite', MockMadcowConfig.getMadcowConfig()));
        assertEquals(2, rootSuite.size());
        assertEquals(1, searchSuite.size());
    }

    void testToString() {
        MadcowTestSuite rootSuite = new MadcowTestSuite('');
        assertToString(rootSuite, '');

        MadcowTestSuite searchSuite = new MadcowTestSuite('search', rootSuite);
        searchSuite.children.add(new MadcowTestSuite('addresses', searchSuite));
        rootSuite.children.add(searchSuite);
        assertToString(searchSuite, 'search');
        assertToString(searchSuite.children.first(), 'search.addresses');
    }

    void testGetTestCasesRecursively() {
        MadcowTestSuite rootSuite = new MadcowTestSuite('');
        MadcowTestSuite searchSuite = new MadcowTestSuite('search', rootSuite);
        searchSuite.children.add(new MadcowTestSuite('addresses', searchSuite));
        rootSuite.children.add(searchSuite);

        assertEquals([], rootSuite.getTestCasesRecusively());

        MadcowTestCase rootTestCase = new MadcowTestCase('testCaseInRoot', MockMadcowConfig.getMadcowConfig());
        MadcowTestCase searchTestCase = new MadcowTestCase('testCaseInSubSuite', MockMadcowConfig.getMadcowConfig())
        rootSuite.testCases.add(rootTestCase);
        searchSuite.testCases.add(searchTestCase);

        assertEquals([rootTestCase, searchTestCase], rootSuite.getTestCasesRecusively());
        assertEquals([searchTestCase], searchSuite.getTestCasesRecusively());
    }
}
