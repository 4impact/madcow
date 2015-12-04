/*
 * Copyright 2015 4impact, Brisbane, Australia
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

package au.com.ps4impact.madcow.mappings;

import au.com.ps4impact.madcow.util.ResourceFinder

import static org.junit.Assert.*;

/**
 * Test for the MappingsReferenceWrapper class
 * Created by tromano on 4/12/2015.
 */
public class MappingsReferenceWrapperTest extends GroovyTestCase {

    public void testMappingType() {
        MappingsReferenceWrapper properties = new MappingsReferenceWrapper();
        def prop = properties.setProperty('testsuite_first_AwesomeSauce.xpath', "//a[@id=\'bob\']");
        assertEquals("XPATH", properties.getMappingType(properties.keySet().first()))
    }

    public void testMappingShortName() {
        MappingsReferenceWrapper properties = new MappingsReferenceWrapper();
        def prop = properties.setProperty('testsuite_first_AwesomeSauce.xpath', "//a[@id=\'bob\']");
        assertEquals("AwesomeSauce", properties.getMappingShortName(properties.keySet().first()))
    }

    public void testMappingFullName() {
        MappingsReferenceWrapper properties = new MappingsReferenceWrapper();
        def prop = properties.setProperty('testsuite_first_AwesomeSauce.xpath', "//a[@id=\'bob\']");
        assertEquals("testsuite_first_AwesomeSauce", properties.getMappingFullName(properties.keySet().first()))
    }

    public void testMappingParentName() {
        MappingsReferenceWrapper properties = new MappingsReferenceWrapper();
        def prop = properties.setProperty('testsuite_first_AwesomeSauce.xpath', "//a[@id=\'bob\']");
        assertEquals("testsuite_first", properties.getMappingParents(properties.keySet().first()))
    }

    public void testMappingPrettyName() {
        MappingsReferenceWrapper properties = new MappingsReferenceWrapper();
        def prop = properties.setProperty('testsuite_first_AwesomeSauce.xpath', "//a[@id=\'bob\']");
        assertEquals("Awesome Sauce", properties.getMappingPrettyName(properties.keySet().first()))
    }

    public void testMappingValue() {
        MappingsReferenceWrapper properties = new MappingsReferenceWrapper();
        def prop = properties.setProperty('testsuite_first_AwesomeSauce.xpath', "//a[@id=\'bob\']");
        assertEquals("//a[@id='bob']", properties.get(properties.keySet().first()))
    }

    public void testDeCamelCase() {
        MappingsReferenceWrapper properties = new MappingsReferenceWrapper();
        properties.setProperty('AwesomeSauce', 'funtimes');
        assertEquals("Awesome Sauce", properties.deCamelCase("AwesomeSauce"))
    }

    public void testToJSON() {
        def resource = ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'menu.grass', 'mappings/testsite')
        MappingsReferenceWrapper properties = new MappingsReferenceWrapper();
        properties.setProperty('niceKey', '//a[@id=\'bob\']');

        def fileHelper = new MappingsFileHelper();
        def withNamespace = fileHelper.applyMappingNamespace(resource, properties);
        assertEquals([testsite_menu_niceKey:[value:"//a[@id='bob']", type:"ID", parents:"testsite_menu", shortName:"niceKey", fullName:"testsite_menu_niceKey", prettyName:"Nice Key"]],
                (withNamespace as MappingsReferenceWrapper).toJSON())
    }
}