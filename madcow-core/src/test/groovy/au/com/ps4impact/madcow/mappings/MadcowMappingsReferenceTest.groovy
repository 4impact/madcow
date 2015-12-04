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

import au.com.ps4impact.madcow.util.ResourceFinder;
import groovy.util.GroovyTestCase;
import org.junit.Test;
import au.com.ps4impact.madcow.mappings.MadcowMappingsReference

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by tromano on 4/12/2015.
 */
public class MadcowMappingsReferenceTest extends GroovyTestCase {

    public void testMappingType() {

    }

    public void testMappingShortName() {

    }

    public void testMappingPrettyName() {

    }

    public void testDeCamelCase() {

    }

    public void testToJSON() {
        def resource = ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'menu.grass', 'mappings/testsite')
        MadcowMappingsReference properties = new MadcowMappingsReference();
        properties.setProperty('nice-Key', 'nice-Value');

        def fileHelper = new MappingsFileHelper();
        def withNamespace = fileHelper.applyMappingNamespace(resource, properties);
        assertEquals("{testsite_menu_nice-Key={value=nice-Value, type=ID, parents=testsite_menu, shortName=nice-Key, fullName=testsite_menu_nice-Key, prettyName=Nice - Key}}",
                (withNamespace as MadcowMappingsReference).toJSON())
    }
}