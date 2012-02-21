package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.MadcowTestRunner
import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Test for running WebDriver grass
 */
class WebDriverStepRunnerTest extends GroovyTestCase {

    public void testRunIt() {
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
        println("Results!!!! : " + testCase.steps.result);
    }

    public void testDoIt() {
        MadcowTestRunner.executeTests(['AddressTest'], new MadcowConfig())
    }
}
