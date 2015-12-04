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

package au.com.ps4impact.madcow.mappings

import au.com.ps4impact.madcow.MadcowProject
import au.com.ps4impact.madcow.util.ResourceFinder
import groovy.json.JsonOutput
import groovy.text.GStringTemplateEngine
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.report.IJSONSerializable
import org.apache.log4j.Logger

/**
 * Mappings Reference for output to JSON and use in reference html
 *
 * @author Tom Romano
 */
class MappingsReferenceWrapper extends Properties implements IJSONSerializable {

    protected static final Logger LOG = Logger.getLogger(MappingsReferenceWrapper.class);

    def getMappingType(String key){
        return StringUtils.substringAfter(key, '.').toUpperCase()?:"ID";
    }

    def getMappingFullName(String key){
        return StringUtils.substringBefore(key, '.');
    }

    def getMappingShortName(String key){
        def fullName = getMappingFullName(key)
        return StringUtils.substringAfterLast(fullName, '_');
    }

    def getMappingParents(String key){
        def fullName = getMappingFullName(key)
        return StringUtils.substringBeforeLast(fullName, '_');
    }

    def getMappingPrettyName(String key){
        def shortName = getMappingShortName(key)
        return deCamelCase(shortName);
    }

    static String deCamelCase(String s) {
        String[] camelCaseSplit = StringUtils.splitByCharacterTypeCamelCase(s)
        String deCamel = ''
        camelCaseSplit.eachWithIndex { word, idx ->
            deCamel += idx == 0 ? StringUtils.capitalize(word) : " $word"
        }
        return deCamel
    }

    @Override
    Map toJSON() {
        def referenceJSON = [:];
        this.each { key, value ->
            referenceJSON.put(key,
                [
                        value: value,
                        type: getMappingType(key as String)?:"ID",
                        parents: getMappingParents(key as String),
                        shortName: getMappingShortName(key as String),
                        fullName: getMappingFullName(key as String),
                        prettyName: getMappingPrettyName(key as String)
                ]
            );
        }

        return referenceJSON
    }

}