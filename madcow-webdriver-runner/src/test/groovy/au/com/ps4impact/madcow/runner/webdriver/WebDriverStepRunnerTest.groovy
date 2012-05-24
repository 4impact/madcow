package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver

/**
 * Test for running WebDriver grass
 *
 * @author Gavin Bunney
 */
class WebDriverStepRunnerTest extends GroovyTestCase {

    public void testDefaultSelector() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner([:]);
        assertEquals('htmlid', stepRunner.defaultSelector);
    }

    public void testDefaultBrowser() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner([:]);
        assertEquals(MadcowHtmlUnitDriver.class, stepRunner.driver.class);
    }

    public void testBrowserNotFound() {
        try {
            WebDriverStepRunner stepRunner = new WebDriverStepRunner(['browser':'tent.tent.tennis.tent']);
            fail('should always exception with ClassNotFoundException');
        } catch (e) {
            assertTrue( e.message.startsWith("The specified Browser 'TENT.TENT.TENNIS.TENT' cannot be found"));
        }
    }
}
