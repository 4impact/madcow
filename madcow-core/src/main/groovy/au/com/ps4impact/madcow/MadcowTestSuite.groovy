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

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.StopWatch;

/**
 * A collection of madcow test cases.
 *
 * @author Gavin Bunney
 */
class MadcowTestSuite {

    String name;
    ArrayList<MadcowTestCase> testCases;
    MadcowTestSuite parent;
    ArrayList<MadcowTestSuite> children;
    StopWatch stopWatch;

    MadcowTestSuite(String name, MadcowTestSuite parent = null, ArrayList<MadcowTestCase> testCases = new ArrayList<MadcowTestCase>()) {
        this.name       = name;
        this.testCases  = testCases;
        this.parent     = parent;
        this.children   = new ArrayList<MadcowTestSuite>();
        this.stopWatch  = new StopWatch();
    }

    public int size() {
        return getTestCasesRecusively().size();
    }

    public String fullyQualifiedName() {
        String parentFQN = parent != null ? parent.fullyQualifiedName() : '';
        return parentFQN != '' ? "${parentFQN}.${name}" : name;
    }

    public String toString() {
        return fullyQualifiedName();
    }

    /**
     * Get all children and children's children children children's children...
     */
    public ArrayList<MadcowTestCase> getTestCasesRecusively() {

        ArrayList<MadcowTestCase> allTestCases = new ArrayList<MadcowTestCase>()

        if (testCases.size() > 0)
            allTestCases.addAll(testCases);

        children.each { child ->
            def childTestCases = child.getTestCasesRecusively();
            if (childTestCases.size() > 0)
                allTestCases.addAll(childTestCases);
        }

        return allTestCases;
    }

    /**
     * Locate a suite for the full qualified test name.
     */
    public MadcowTestSuite locateSuite(String fullyQualifiedTestName) {

        if (!fullyQualifiedTestName.contains("."))
            return this;

        // get all the packages, excluding the test name
        String fqSuiteName = StringUtils.left(fullyQualifiedTestName, fullyQualifiedTestName.lastIndexOf("."));

        if (this.name == fqSuiteName)
            return this;

        for (child in children) {
            if (child.fullyQualifiedName() == fqSuiteName)
                return child;

            def childOfChild = child.locateSuite(fullyQualifiedTestName);
            if (childOfChild != null)
                return childOfChild;
        }

        return null;
    }
}