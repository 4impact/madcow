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

package au.com.ps4impact.madcow.mappings

import au.com.ps4impact.madcow.MadcowTestCase;

/**
 * Representation of Mappings files, providing static
 * loading of the mappings, and retrieval of a mapping for a particular
 * html element reference.
 *
 * @author Gavin Bunney
 */
class MadcowMappings {

    protected static Map<String, Map<String, String>> mappings;

    /**
     * Initialise the mappings if it hasn't already been.
     */
    protected static initMappings(MadcowTestCase testCase) {
        if (mappings == null) {
            mappings = new MappingsFileHelper().initProcessProperties(testCase);
        }
    }

    /**
     * Get a selector Map from a mapping.
     */
    static Map<String, String> getSelectorFromMapping(MadcowTestCase testCase, String mapping) {

        initMappings(testCase);

        if (mappings.containsKey(mapping)) {
            return mappings.get(mapping).clone() as Map;
        } else {
            return ["${testCase.stepRunner.defaultSelector}" : mapping];
        }
    }

    /**
     * Add a mapping into the mappings collection.
     */
    static addMapping(MadcowTestCase testCase, String key, Map value) {
        initMappings(testCase);
        mappings.put(key, value);
    }
}
