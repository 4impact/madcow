package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.MadcowTestRunner
import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Test for running WebDriver grass
 */
class WebDriverStepRunnerTest extends GroovyTestCase {

    public void testRunInlineScript() {
        String grassScriptString = """
            invokeUrl = ADDRESSBOOK
            @expectedValue = Search Address

            # verify the text exists on the page
            verifyText = @expectedValue

            testsite_menu_createAddress.clickLink
            verifyText = Check For Duplicates
        """;
        ArrayList<String> grassScript = new ArrayList<String>();
        grassScriptString.eachLine { line -> grassScript.add(line) }

        MadcowTestCase testCase = new MadcowTestCase('WebDriverStepRunnerTest-testRunIt', grassScript);
        testCase.execute();
    }
    
    public void testDefaultSelector() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner([:]);
        assertEquals('htmlid', stepRunner.defaultSelector);
    }

    public void testDefaultBrowser() {
        WebDriverStepRunner stepRunner = new WebDriverStepRunner([:]);
        assertEquals(org.openqa.selenium.htmlunit.HtmlUnitDriver.class, stepRunner.driver.class);
    }

    public void testBrowserNotFound() {
        try {
            WebDriverStepRunner stepRunner = new WebDriverStepRunner(['browser':'tent.tent.tennis.tent']);
            fail('should always exception with ClassNotFoundException');
        } catch (e) {
            assertTrue( e.message.startsWith("The specified Browser 'tent.tent.tennis.tent' cannot be found"));
        }
    }
}
