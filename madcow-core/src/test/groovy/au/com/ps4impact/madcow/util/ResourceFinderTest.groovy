package au.com.ps4impact.madcow.util

import au.com.ps4impact.madcow.MadcowProject

/**
 * Test class for Resource Finder.
 */
class ResourceFinderTest extends GroovyTestCase {

    void testLocateSingleResource() {
        ResourceFinder.locateResourceOnClasspath(this.class.classLoader, MadcowProject.CONFIGURATION_FILE);
    }

    void testLocateMultipleResources() {
        ResourceFinder.locateResourcesOnClasspath(this.class.classLoader, '**/*.xml');
    }

    void testLocateSingleResourceMultipleFound() {
        try {
            ResourceFinder.locateResourceOnClasspath(this.class.classLoader, '**/*.*');
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
    }
}
