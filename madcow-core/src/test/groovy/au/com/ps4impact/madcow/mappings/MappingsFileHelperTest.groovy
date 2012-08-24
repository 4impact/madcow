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

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * Test for the MappingsFileHelper.
 *
 * @author Gavin Bunney
 */
class MappingsFileHelperTest extends GroovyTestCase {

    protected MadcowTestCase testCase = new MadcowTestCase('MappingsFileHelperTest', MockMadcowConfig.getMadcowConfig(), []);

    public void testMappingNamespace() {
        def resource = ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'menu.grass', 'mappings/testsite')
        Properties properties = new Properties();
        properties.setProperty('nice-key', 'nice-value');

        def fileHelper = new MappingsFileHelper();
        def withNamespace = fileHelper.applyMappingNamespace(resource, properties);
        
        assertEquals('{testsite_menu_nice-key=nice-value}', withNamespace.toString());
    }

    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty('nice-key.text', 'nice-value');

        def fileHelper = new MappingsFileHelper();
        def processed = fileHelper.processProperties(testCase, properties);
        assertEquals('[text:nice-value]', processed['nice-key'].toString());
    }

    public void testProcessPropertiesDefaultSelector() {
        Properties properties = new Properties();
        properties.setProperty('nice-key', 'nice-value');

        def fileHelper = new MappingsFileHelper();
        def processed = fileHelper.processProperties(testCase, properties);
        assertEquals('[htmlid:nice-value]', processed['nice-key'].toString());
    }

    public void testDuplicateProperties() {
        Properties baseProperties = new Properties();
        baseProperties.setProperty('nice-key.text', 'nice-value');
        
        Properties otherProperties = new Properties();
        
        def fileHelper = new MappingsFileHelper();
        def duplicates = fileHelper.duplicateProperties(baseProperties, otherProperties);
        assertEquals(0, duplicates.size());

        otherProperties.setProperty('nice-key.text', 'nice-value');
        duplicates = fileHelper.duplicateProperties(baseProperties, otherProperties);
        assertEquals(1, duplicates.size());
        assertEquals('nice-key.text', duplicates.first());
    }
}