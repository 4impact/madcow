package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.grass.GrassParser

/**
 * Test for the ClickLink BladeRunner.
 */
class ClickLinkTest extends GroovyTestCase {

    void testMappingSelectorInvalidRequired() {

        MadcowTestCase testCase = new MadcowTestCase('testIsValidBladeToExecute', MockMadcowConfig.getMadcowConfig(), []);
        GrassParser grassParser = testCase.grassParser;
        GrassBlade blade = new GrassBlade('addressbook_search_country.clickLink', grassParser);

        // default will make the selector htmlid, so verify it is ok
        def clickLink = new ClickLink();
        assertTrue(clickLink.isValidBladeToExecute(blade));

        try {
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(clickLink.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals(e.message, 'Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, TEXT, NAME, XPATH] are supported.', );
        }
    }

    void testMappingSelectorRequired() {
        MadcowTestCase testCase = new MadcowTestCase('testIsValidBladeToExecute', MockMadcowConfig.getMadcowConfig(), []);
        GrassParser grassParser = testCase.grassParser;
        GrassBlade blade = new GrassBlade('addressbook_search_country.clickLink', grassParser);
        def clickLink = new ClickLink();
        try {
            blade.mappingSelectorType = null;
            assertFalse(clickLink.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals(e.message, 'Mapping selector must be supplied. One of [HTMLID, TEXT, NAME, XPATH] are supported.', );
        }
    }
}
