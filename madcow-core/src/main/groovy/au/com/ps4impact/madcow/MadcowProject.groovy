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
     * The system property used to specify the BASE URL for the madcow project's dir
     */
    public static final String BASEURL_PROPERTY_NAME = "madcow.project.baseUrl"

    /**
     * The Madcow Base URL to use if set
     */
    public static String getMADCOW_BASE_URL() {
        def baseUrl = System.getProperty(BASEURL_PROPERTY_NAME)
        if (!baseUrl) return ''
        return baseUrl.endsWith("/")?baseUrl:baseUrl+"/"
    }

    /**
     * Location and name of the configuration file.
     */
    public static String getCONFIGURATION_FILE() {
        return MADCOW_BASE_URL + 'conf/madcow-config.xml';
    }

    /**
     * Location of tests directory.
     */
    public static String getTESTS_DIRECTORY() {
        return MADCOW_BASE_URL + 'tests';
    }

    /**
     * Location of templates directory.
     */
    public static String getTEMPLATES_DIRECTORY() {
        return MADCOW_BASE_URL + 'templates';
    }

    /**
     * Location of mappings directory.
     */
    public static String getMAPPINGS_DIRECTORY() {
        return MADCOW_BASE_URL + 'mappings';
    }

    /**
     * Location of mappings reference directory.
     */
    public static String getMAPPINGS_REFERENCE_DIRECTORY() {
        return MADCOW_BASE_URL + 'mappings-reference';
    }

    /**
     * Location of the results directory.
     */
    public static String getRESULTS_DIRECTORY() {
        return MADCOW_BASE_URL + 'results'
    };

    /**
     * Location of the madcow report directory.
     */
    public static String getMADCOW_REPORT_DIRECTORY() {
        return RESULTS_DIRECTORY + '/madcow-report'
    };

    /**
     * Location of the madcow test case report directory.
     */
    public static String getMADCOW_TESTCASE_REPORT_DIRECTORY() {
        return MADCOW_REPORT_DIRECTORY + '/results'
    };
}
