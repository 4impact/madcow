package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.mock.MockMadcowConfig

/**
 * Test for the MadcowTestCase class.
 *
 * @author Gavin Bunney
 */
class MadcowTestCaseTest extends GroovyTestCase {

    protected ArrayList<String> getGrassScript() {
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
        return grassScript;
    }

    void testCreateAndParse() {
        MadcowTestCase testCase = new MadcowTestCase('testCreateAndParse', MockMadcowConfig.getMadcowConfig(), getGrassScript());
        testCase.parseScript();
        assertEquals("Verify number of steps, ignoring comments and blank lines", 5, testCase.steps.size());
    }

    void testExecuteMockedRunner() {
        MadcowTestCase testCase = new MadcowTestCase('testExecuteMockedRunner', MockMadcowConfig.getMadcowConfig(), getGrassScript());
        testCase.madcowConfig.stepRunner = 'au.com.ps4impact.madcow.mock.MockMadcowStepRunner';
        testCase.execute();
    }

    void testExecuteMockedRunnerFailure() {
        MadcowTestCase testCase = new MadcowTestCase('testExecuteMockedRunner', MockMadcowConfig.getMadcowConfig(false), getGrassScript());
        try {
            testCase.execute();
            fail('should always exception due to step failure')
        } catch (e) {
            assertEquals('Step failed - Status: FAIL | Message: Mocked Fail', e.message)
        }
    }

    void testExecuteRunnerDoesNotExist() {
        try {
            MadcowTestCase testCase = new MadcowTestCase('testExecuteRunnerDoesNotExist', MockMadcowConfig.getMadcowConfig(true, '.tent'), getGrassScript());
            testCase.execute();
            fail('should always exception with ClassNotFoundException');
        } catch (e) {
            assertTrue( e.message.startsWith("The specified MadcowStepRunner '.tent' cannot be found"));
        }
    }

    void testExecuteRunnerUnknownException() {
        try {
            MadcowTestCase testCase = new MadcowTestCase('testExecuteRunnerUnknownException', MockMadcowConfig.getMadcowConfig(true, 'au.com.ps4impact.madcow.util.ResourceFinder'), getGrassScript());
            testCase.execute();
            fail('should always exception with Exception');
        } catch (e) {
            assertTrue( e.message.startsWith("Unexpected error creating the MadcowStepRunner"));
        }
    }
}
