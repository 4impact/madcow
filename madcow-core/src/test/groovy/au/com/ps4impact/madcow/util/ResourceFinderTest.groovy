package au.com.ps4impact.madcow.util

import au.com.ps4impact.madcow.MadcowProject

/**
 * Test class for Resource Finder.
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
            assertEquals('Multiple resource matches found for \'**/*.*\'', e.message);
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
