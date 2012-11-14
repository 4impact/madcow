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

package au.com.ps4impact.madcow.execution

/**
 * Test for Command Line invocation of Madcow.
 *
 * @author Gavin Bunney
 */
class MadcowCLITest extends GroovyTestCase {

    void testTestToRunOption() {
        def options = MadcowCLI.parseArgs(['-t', 'AddressTest.grass'].toArray() as String[]);
        assertEquals('AddressTest.grass', options.ts.first());
        assertEquals('AddressTest.grass', options.tests.first());

        options = MadcowCLI.parseArgs(['--test', 'AddressTest.grass'].toArray() as String[]);
        assertEquals('AddressTest.grass', options.ts.first());
        assertEquals('AddressTest.grass', options.tests.first());

        options = MadcowCLI.parseArgs(['-t', 'AddressTest.grass,AddressTest2.grass'].toArray() as String[]);
        assertArrayEquals(['AddressTest.grass', 'AddressTest2.grass'].toArray(), options.ts.toArray());
        assertArrayEquals(['AddressTest.grass', 'AddressTest2.grass'].toArray(), options.tests.toArray());

        options = MadcowCLI.parseArgs(['-t', '"AddressTest.grass,AddressTest2.grass"'].toArray() as String[]);
        assertArrayEquals(['AddressTest.grass', 'AddressTest2.grass'].toArray(), options.ts.toArray());
        assertArrayEquals(['AddressTest.grass', 'AddressTest2.grass'].toArray(), options.tests.toArray());
    }
    
    void testEnvironment() {
        def options = MadcowCLI.parseArgs(['-e', 'DEV'].toArray() as String[]);
        assertEquals('DEV', options.e);
        assertEquals('DEV', options.env);

        options = MadcowCLI.parseArgs(['--env', 'DEV'].toArray() as String[]);
        assertEquals('DEV', options.e);
        assertEquals('DEV', options.env);
    }

    void testMappingsReference() {
        def options = MadcowCLI.parseArgs(['-m'].toArray() as String[]);
        assertNotNull(options.mappings);
    }

    protected void checkHelpOutput(Closure functionCall) {

        // capture the system output stream so we can look at the help printed stuff
        ByteArrayOutputStream systemOutOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutOutputStream));

        functionCall.call();

        String systemOutput = systemOutOutputStream.toString();

        def expectedHelpMessage = """usage: runMadcow [options]
Options:
 -a,--all               Run all tests
 -e,--env <env-name>    Environment to load from the madcow-config.xml
 -h,--help              Show usage information
 -m,--mappings          Generate the Mappings Reference files
 -t,--test <testname>   Comma seperated list of test names
 -v,--version           Show the current version of Madcow
""";

        assertEquals(expectedHelpMessage, systemOutput);

        MadcowCLI.main(['-h'] as String[])
        assertEquals(expectedHelpMessage, systemOutput);

        System.setOut(null);
    }

    void testHelp() {
        checkHelpOutput({MadcowCLI.parseArgs(['-h'].toArray() as String[])});
        checkHelpOutput({MadcowCLI.main(['-h'] as String[])});
        checkHelpOutput({MadcowCLI.main([] as String[])});
    }
}
