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

import au.com.ps4impact.madcow.MadcowProject
import au.com.ps4impact.madcow.MadcowTestSuite
import au.com.ps4impact.madcow.MadcowTestSuiteResult
import groovy.io.FileType;
import groovyjarjarcommonscli.ParseException;
import groovyjarjarcommonscli.Option
import au.com.ps4impact.madcow.MadcowTestRunner
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.VersionUtil

import java.awt.Desktop

/**
 * Run Madcow from the Command Line.
 *
 * @author Gavin Bunney
 */
class MadcowCLI {

    public static def parseArgs(String[] incomingArgs) throws ParseException {
        def cli = new CliBuilder(usage: 'runMadcow [options]', header: 'Options:')

        cli.with {
            h(longOpt: 'help', 'Show usage information')
            e(longOpt: 'env', args: 1, argName: 'env-name', 'Environment to load from the madcow-config.xml')
            c(longOpt: 'conf', args: 1, argName: 'conf-file', 'Name of the configuration file to use, defaults to madcow-config.xml')
            s(longOpt: 'suite', args: 1, argName: 'suite-dir', 'Name of the top level directory')
            t(longOpt: 'test', args: Option.UNLIMITED_VALUES, valueSeparator: ',', argName: 'testname', 'Comma separated list of test names')
            a(longOpt: 'all', 'Run all tests')
            v(longOpt: 'version', 'Show the current version of Madcow')
        }

        def options = cli.parse(incomingArgs);

        if (options.help) {
            cli.usage();
        }

        return options;
    }

    /**
     * Entry point.
     */
    static main(def args) {

        def javaVersion = Integer.parseInt(System.getProperty("java.version").split("\\.")[1])
        if (javaVersion < 8) {
            println("Madcow currently requires at least Java JDK 1.8+, please update your JAVA_HOME accordingly and retry");
            return;
        }

        args = args ?: new String()[];
        def options = parseArgs(args);
        if (options.help) {
            return;
        }

        if (options.version) {
            println("-------------------------------------------")
            println(VersionUtil.getFullVersionString());
            println("-------------------------------------------")
            return;
        }

        if (options.conf) {
            MadcowConfig.SHARED_CONFIG = new MadcowConfig(options.env ?: null, '' + options.conf)
        } else {
            MadcowConfig.SHARED_CONFIG = new MadcowConfig(options.env ?: null);
        }

        try {
            MadcowTestSuite executedSuite;

            if (options.test) {
                println('tests: ' + options.tests*.trim())
                executedSuite = MadcowTestRunner.executeTests(options.tests*.trim() as ArrayList<String>, MadcowConfig.SHARED_CONFIG);
            } else if (options.suite)    {
                def list=[]
                new File(options.suite as String).eachFileRecurse(FileType.FILES) {
                    if(it.name.endsWith('.grass')) {
                        list << it.getName()
                    }
                }
                println(list)
                executedSuite = MadcowTestRunner.executeTests(list, MadcowConfig.SHARED_CONFIG);
            } else {
                executedSuite = MadcowTestRunner.executeTests(MadcowConfig.SHARED_CONFIG);
            }

            try {
                // try and open the report
                File reportFile = new File(MadcowProject.MADCOW_REPORT_DIRECTORY + '/index.html');
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(reportFile.toURI());
                }
            } catch (Exception ignored) {
                // failed to open report... oh well
            }

            MadcowTestSuiteResult results = executedSuite.buildResults();
            int testsFailed = (results.testsError + results.testsFailed);
            System.exit(testsFailed);

        } catch (Exception e) {
            println("There was an error running Madcow: ${e.message}");
            System.exit(9999);
        }
    }

}