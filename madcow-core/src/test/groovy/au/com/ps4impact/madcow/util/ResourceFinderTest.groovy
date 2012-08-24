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

package au.com.ps4impact.madcow.util

import au.com.ps4impact.madcow.MadcowProject

/**
 * Test class for Resource Finder.
 *
 * @author Gavin Bunney
 */
class ResourceFinderTest extends GroovyTestCase {

    void testLocateSingleResource() {
        assertNotNull(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, MadcowProject.CONFIGURATION_FILE));
        assertNotNull(ResourceFinder.locateFileOnClasspath(this.class.classLoader, MadcowProject.CONFIGURATION_FILE));
    }

    void testLocateSingleResourceWithBasedir() {
        assertNotNull(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, MadcowProject.CONFIGURATION_FILE.split('/', 2)[1], MadcowProject.CONFIGURATION_FILE.split('/', 2)[0]));
        assertNotNull(ResourceFinder.locateFileOnClasspath(this.class.classLoader, MadcowProject.CONFIGURATION_FILE.split('/', 2)[1], MadcowProject.CONFIGURATION_FILE.split('/', 2)[0]));
    }

    void testLocateMultipleResources() {
        assertNotNull(ResourceFinder.locateResourcesOnClasspath(this.class.classLoader, '**/*.xml'));
        assertNotNull(ResourceFinder.locateFilesOnClasspath(this.class.classLoader, '**/*.xml'));
    }

    void testLocateSingleResourceMultipleFound() {
        try {
            ResourceFinder.locateResourceOnClasspath(this.class.classLoader, '**/*.*');
            fail('Should always exception');
        } catch (e) {
            assertEquals('Multiple resource matches found for \'**/*.*\'', e.message);
        }

        try {
            ResourceFinder.locateFileOnClasspath(this.class.classLoader, '**/*.*');
            fail('Should always exception');
        } catch (e) {
            assertEquals('Multiple resource file matches found for \'**/*.*\'', e.message);
        }
    }

    void testLocateResourceNotOnPath() {
        try {
            ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'spanish-tennis.tent');
            fail('Should always exception');
        } catch (e) {
            assertEquals('Unable to find matches on the classpath for \'spanish-tennis.tent\'', e.message);
        }

        try {
            ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'spanish-tennis.tent');
            fail('Should always exception');
        } catch (e) {
            assertEquals('Unable to find matches on the classpath for \'spanish-tennis.tent\'', e.message);
        }
    }
    
    void testAddFileExtensionNotRequired() {
        assertEquals('spanish.tennis', ResourceFinder.addFileExtensionIfRequired('spanish.tennis', 'tennis'));
        assertEquals('spanish.tennis', ResourceFinder.addFileExtensionIfRequired('spanish.tennis', '.tennis'));
    }

    void testAddFileExtensionRequired() {
        assertEquals('spanish.tennis', ResourceFinder.addFileExtensionIfRequired('spanish', 'tennis'));
        assertEquals('spanish.tennis', ResourceFinder.addFileExtensionIfRequired('spanish', '.tennis'));
    }
}
