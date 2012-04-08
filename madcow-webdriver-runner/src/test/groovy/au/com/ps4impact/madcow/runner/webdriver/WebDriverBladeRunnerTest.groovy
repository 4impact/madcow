package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStep

/**
 * Test for the WebDriver Blade Runner.
 */
class WebDriverBladeRunnerTest extends GroovyTestCase {

    class MockWebDriverBladeRunner extends WebDriverBladeRunner {
        void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
            //
        }
    }

    WebDriverBladeRunner bladeRunner = new MockWebDriverBladeRunner();

    void testSupportedTypesAsStringArray() {
        assertEquals(['htmlid', 'text', 'name', 'xpath'], bladeRunner.getSupportedSelectorTypesAsStringArray())
    }
}
