package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.grass.GrassParser
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Test for the ClickLink BladeRunner.
 */
class ClickLinkTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ClickLinkTest', new MadcowConfig(), []);
    GrassParser grassParser = testCase.grassParser;
    def clickLink = new ClickLink();

    protected verifyLinkExecution(GrassBlade blade, boolean shouldPass) {
        testCase.stepRunner.driver.get('http://test-site.projectmadcow.com:8080/madcow-test-site-2');
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testLinkByMapping() {
        GrassBlade blade = new GrassBlade('testsite_menu_createAddress.clickLink', grassParser);
        verifyLinkExecution(blade, true);
    }

    void testLinkDoesNotExist() {
        GrassBlade blade = new GrassBlade('testsite_menu_createAddress_DoesntExist.clickLink', grassParser);
        verifyLinkExecution(blade, false);
    }

    void testDefaultMappingSelector() {
        GrassBlade blade = new GrassBlade('testsite_menu_createAddress.clickLink', grassParser);
        assertTrue(clickLink.isValidBladeToExecute(blade));
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.clickLink', grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(clickLink.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals(e.message, 'Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, TEXT, NAME, XPATH] are supported.', );
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.clickLink', grassParser);
            blade.mappingSelectorType = null;
            assertFalse(clickLink.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals(e.message, 'Mapping selector must be supplied. One of [HTMLID, TEXT, NAME, XPATH] are supported.', );
        }
    }
}
