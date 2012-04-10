package au.com.ps4impact.madcow.mappings

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig

/**
 * Test for the MadcowMappings.
 *
 * @author Gavin Bunney
 */
class MadcowMappingsTest extends GroovyTestCase {

    protected MadcowTestCase testCase = new MadcowTestCase('MadcowMappingsTest', MockMadcowConfig.getMadcowConfig(), []);

    public void testMappingsProcessed() {
        assertEquals('[text:Create Address]', MadcowMappings.getSelectorFromMapping(testCase, 'testsite_menu_createAddress').toString());
        assertEquals('[htmlid:testsite_menu_createAddressXXX]', MadcowMappings.getSelectorFromMapping(testCase, 'testsite_menu_createAddressXXX').toString());
    }
    
    public void testDefaultSelector() {
        assertEquals('[htmlid:barry]', MadcowMappings.getSelectorFromMapping(testCase, 'barry').toString());
    }
}