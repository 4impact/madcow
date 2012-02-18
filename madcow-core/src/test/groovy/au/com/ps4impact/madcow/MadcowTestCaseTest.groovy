package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Test for the MadcowTestCase class.
 */
class MadcowTestCaseTest extends GroovyTestCase {

    void testCreateAndParse() {

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

        MadcowTestCase testCase = new MadcowTestCase('testCreateAndParse', new MadcowConfig(), grassScript);
        assertEquals("Verify number of steps, ignoring comments and blank lines", 5, testCase.steps.size());
    }

    void testDefaultedMadcowConfig() {
        MadcowTestCase testCase = new MadcowTestCase('testDefaultedMadcowConfig');
        assertEquals('testDefaultedMadcowConfig', testCase.name);
        assertNotNull(testCase.madcowConfig);
    }
}
