package au.com.ps4impact.madcow.mappings

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * Test for the MappingsFileHelper.
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
}