package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Test for running WebDriver grass
 *
 * @author Gavin Bunney
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
}
