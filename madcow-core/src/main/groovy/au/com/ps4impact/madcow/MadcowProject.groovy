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

/**
 * Project structure for a Madcow project.
 * This class holds the directory names and other happening
 * things for how a Madcow project is layed out.
 *
 * @author Gavin Bunney
 */
class MadcowProject {

    /**
     * Location and name of the configuration file.
     */
    public static final String CONFIGURATION_FILE = 'conf/madcow-config.xml';

    /**
     * Location of tests directory.
     */
    public static final String TESTS_DIRECTORY = 'tests';

    /**
     * Location of templates directory.
     */
    public static final String TEMPLATES_DIRECTORY = 'templates';

    /**
     * Location of mappings directory.
     */
    public static final String MAPPINGS_DIRECTORY = 'mappings';

    /**
     * Location of mappings reference directory.
     */
    public static final String MAPPINGS_REFERENCE_DIRECTORY = 'mappings-reference';

    /**
     * Location of the results directory.
     */
    public static final String RESULTS_DIRECTORY = 'results';

    /**
     * Location of the madcow report directory.
     */
    public static final String MADCOW_REPORT_DIRECTORY = RESULTS_DIRECTORY + '/madcow-report';
}
