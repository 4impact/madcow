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

package au.com.ps4impact.madcow.logging

import org.apache.log4j.FileAppender
import org.apache.log4j.PatternLayout
import org.apache.log4j.Logger
import org.apache.log4j.Level
import au.com.ps4impact.madcow.MadcowTestCase
import org.apache.log4j.ConsoleAppender
import org.apache.log4j.PropertyConfigurator
import au.com.ps4impact.madcow.util.ResourceFinder
import org.springframework.core.io.Resource

/**
 * Madcow Logging to create a test case log file.
 *
 * @author: Gavin Bunney
 */
public class MadcowLog {

    protected static final String LOGFILE_NAME = "madcow.log";

    static {
        Resource log4jProperties = ResourceFinder.locateResourcesOnClasspath(MadcowLog.getClassLoader(), 'log4j.properties').first();
        PropertyConfigurator.configure(log4jProperties.getURL());
    }

    public static void error(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).error(message);
    }

    public static void warn(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).warn(message);
    }

    public static void info(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).info(message);
    }

    public static void debug(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).debug(message);
    }

    public static void trace(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).trace(message);
    }

    /**
     * Initialise logging for the given test case.
     */
    public static void initialiseLogging(MadcowTestCase testCase) {

        Logger logger = Logger.getLogger(testCase.name);
        logger.setLevel(Level.INFO);
        logger.setAdditivity(false);
        logger.removeAllAppenders();

        FileAppender fileAppender = new FileAppender(new PatternLayout("%d %p [%c] - <%m>%n"),
                                                 "${testCase.getResultDirectory().getAbsolutePath()}/${LOGFILE_NAME}",
                                                 true);
        fileAppender.setName(testCase.name);
        fileAppender.setThreshold(Level.DEBUG);
        fileAppender.activateOptions();
        logger.addAppender(fileAppender);

        ColorConsoleAppender consoleAppender = new ColorConsoleAppender(new PatternLayout("%d [%c] - %m%n"));
        consoleAppender.setInfoColour(ColorConsoleAppender.GREEN)
        consoleAppender.setDebugColour(ColorConsoleAppender.CYAN)
        consoleAppender.setTraceColour(ColorConsoleAppender.CYAN)
        consoleAppender.setName(testCase.name);
        consoleAppender.setThreshold(Level.INFO);
        consoleAppender.activateOptions();
        logger.addAppender(consoleAppender);
    }

    /**
     * Shutdown the test case logging.
     */
    public static void shutdownLogging(MadcowTestCase testCase) {
        Logger.getLogger(testCase.name).removeAppender(testCase.name);
    }
}
