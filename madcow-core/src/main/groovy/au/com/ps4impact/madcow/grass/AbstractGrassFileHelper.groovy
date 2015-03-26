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

package au.com.ps4impact.madcow.grass;

import org.apache.log4j.Logger
import org.springframework.core.io.Resource
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Abstract class for properties file parsing, for global data parameters and mappings files.
 *
 * @author Gavin Bunney
 */
abstract class AbstractGrassFileHelper {

    abstract Logger getLog()

    abstract String getPropertiesFilePrettyName()

    abstract String getFileLocatorFilePattern();
    abstract String getFileLocatorBasedir();

    abstract Map processProperties(MadcowTestCase testCase, Properties properties)

    Map initProcessProperties(MadcowTestCase testCase) {
        Properties localProperties = new Properties();

        Resource[] grassFiles;
        try {
            grassFiles = ResourceFinder.locateResourcesOnClasspath(ClassLoader.getSystemClassLoader(), getFileLocatorFilePattern(), getFileLocatorBasedir());
        } catch (FileNotFoundException ignored) {
            getLog().info('No files found to process');
            return [:];
        }

        getLog().info("Processing ${grassFiles.length} mappings files");

        grassFiles.each { Resource r ->
            def properties = loadMappingProperties(r)
            getLog().trace "Parsing ${getPropertiesFilePrettyName()} File [${r.URL}] "

            List duplicateProperties = duplicateProperties(localProperties, properties)
            if (duplicateProperties.size() > 0){
                def e = new RuntimeException("${getPropertiesFilePrettyName()} File [${r.URL}] contains duplicates [$duplicateProperties] found in a previous ${getPropertiesFilePrettyName()} file")
                getLog().error(e)
                throw e
            }

            localProperties.putAll(properties)
        }
        return processProperties(testCase, localProperties)
    }

    Resource[] getAllMappingsFromClasspath() {
        Resource[] grassFiles;
        try {
            return ResourceFinder.locateResourcesOnClasspath(ClassLoader.getSystemClassLoader(), getFileLocatorFilePattern(), getFileLocatorBasedir());
        } catch (FileNotFoundException ignored) {
            getLog().info('No files found to process');
            return new Resource[0];
        }
    }

    List<String> duplicateProperties(Properties baseProperties, Properties propertiesToCompare) {
        def duplicateProperties = []
        propertiesToCompare.each { key, value ->
            if (baseProperties.getProperty(key as String)){
                duplicateProperties.add key
            }
        }

        duplicateProperties
    }

    def loadMappingProperties(Resource resource) {
        Properties properties = new Properties()
        properties.load(resource.getInputStream())
        properties
    }
}
