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

package au.com.ps4impact.madcow.mappings;

import org.apache.log4j.Logger
import org.springframework.core.io.Resource
import au.com.ps4impact.madcow.MadcowProject
import au.com.ps4impact.madcow.grass.AbstractGrassFileHelper
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Helper class for Mappings file reading/mapping;
 *
 * Locates the Mappings files and applies the relevant namespace prefix.
 *
 * @author Gavin Bunney
 */
class MappingsFileHelper extends AbstractGrassFileHelper {

	protected static final Logger LOG = Logger.getLogger(MappingsFileHelper.class);
    
    Logger getLog(){
        LOG
    }
    
    String getPropertiesFilePrettyName() {
        'Mappings'
    }
    
    String getFileLocatorFilePattern(){
        return '**/*.grass';
    }    
    
    String getFileLocatorBasedir() {
        return MadcowProject.MAPPINGS_DIRECTORY;
    }


	/**
	 * Apply the mapping namespace to the list of properties.
	 * The creates the prefixed folder_package_structure_* one the property keys.
	 *
	 * @param resource File resource properties loaded from
	 * @param baseProperties Collection of properties from the resource file
	 */
	def applyMappingNamespace(Resource resource, Properties properties) {
		def fileURLSplit = resource.URL.path.split('/')
		def mappingNamespace = "";
		for (def i = fileURLSplit.length - 1; i > -1; --i) {
			if (fileURLSplit[i] == MadcowProject.MAPPINGS_DIRECTORY)
				break;

			if (mappingNamespace == ''){
				fileURLSplit[i] = fileURLSplit[i].substring(0, fileURLSplit[i].lastIndexOf('.grass'))
			}
            
			mappingNamespace = fileURLSplit[i] + (mappingNamespace != '' ? '_' : '') + mappingNamespace
		}
		LOG.info("Mapping Namespace being applied '${mappingNamespace}'")
        def result = properties.collectEntries( [:] ) { mapKey, mapValue ->
            [(mappingNamespace != '' ? mappingNamespace + '_'+ mapKey : mapKey),mapValue]
        }
        LOG.trace("Returning updated map with mappingNamespace applied "+result)
        return result as Properties
	}

    @Override
	def loadMappingProperties(Resource resource){
		Properties properties = super.loadMappingProperties(resource)
		properties = applyMappingNamespace(resource, properties)
		properties
	}

	Map processProperties(MadcowTestCase testCase, Properties properties) {
		Map mappings = new HashMap<String, Map>();
		properties.each {String key, String value ->

            String id, prop;
            (id, prop) = key.tokenize(".")
			if (prop == null) {
				prop = testCase.stepRunner.defaultSelector;
			}
			Map attr = new HashMap<String, String>()
			attr."$prop" = value?.trim()
			mappings.put(id, attr)
		}

		LOG.debug "Processed mappings : $mappings"

		return mappings;
	}
}
