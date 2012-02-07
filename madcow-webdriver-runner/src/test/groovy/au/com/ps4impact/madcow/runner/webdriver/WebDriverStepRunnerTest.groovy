package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.MadcowTestCase

/**
 * User: gbunney
 * Date: 7/02/12 10:53 AM
 */
class WebDriverStepRunnerTest extends GroovyTestCase {

    public void testRunIt() {
        String grassScriptString = """
            @expectedValue = Australia

            # verify the expected country
            addressbook_search_country.verifyText = @expectedValue
            addressbook_search_country.verifySelectFieldOptions = ['One', 'Two']

            # perform a search and check the field options
            addressbook_search_button.clickLink
            addressbook_search_country.verifySelectFieldOptions = [options : ['@expectedValue', 'New Zealand']]
        """;
        ArrayList<String> grassScript = new ArrayList<String>();
        grassScriptString.eachLine { line -> grassScript.add(line) }

        MadcowTestCase testCase = new MadcowTestCase(grassScript);
        testCase.execute();
        println("Results!!!! : " + testCase.steps.result);
    }

}
