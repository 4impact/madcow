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

package au.com.ps4impact.madcow.execution;

import groovyjarjarcommonscli.ParseException;
import groovyjarjarcommonscli.Option
import au.com.ps4impact.madcow.MadcowTestRunner
import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Run Madcow from the Command Line.
 *
 * @author Gavin Bunney
 */
class MadcowCLI {

    public static def parseArgs(String[] incomingArgs) throws ParseException {
        def cli = new CliBuilder(usage:'runMadcow [options]', header:'Options:')

        cli.with {
            h(longOpt : 'help', 'Show usage information')
            e(longOpt : 'env',  args: 1, argName: 'env-name', 'Environment to load from the madcow-config.xml')
            t(longOpt : 'test', args: Option.UNLIMITED_VALUES, valueSeparator: ',', argName : 'testname', 'Comma seperated list of test names')
            a(longOpt : 'all',  'Run all tests')
        }

        def options = cli.parse(incomingArgs);

        if (options.help || incomingArgs.size() == 0) {
            cli.usage();
        }

        return options;
    }

    /**
     * Entry point.
     */
    static main(def args)
    {
        args = args ?: new String()[];
        def options = parseArgs(args);
        if (options.help || args.size() == 0)
            return;

        MadcowConfig config = new MadcowConfig(options.env ?: null);

        if (options.test)
            MadcowTestRunner.executeTests(options.tests as ArrayList<String>, config);
        else
            MadcowTestRunner.executeTests(config);
    }

}