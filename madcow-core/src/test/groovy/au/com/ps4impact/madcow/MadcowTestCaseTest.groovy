package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Test for the MadcowTestCase class.
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
        MadcowTestCase testCase = new MadcowTestCase('testCreateAndParse', new MadcowConfig(), getGrassScript());
        assertEquals("Verify number of steps, ignoring comments and blank lines", 5, testCase.steps.size());
    }

    void testMadcowConfigConstructorParams() {
        MadcowTestCase testCase = new MadcowTestCase('CheckDefaultedMadcowConfig');
        assertEquals('CheckDefaultedMadcowConfig', testCase.name);
        assertNotNull(testCase.madcowConfig);

        def someConfig = new MadcowConfig();
        testCase = new MadcowTestCase('testDefaultedMadcowConfig', someConfig);
        assertEquals(someConfig, testCase.madcowConfig);
    }

    void testExecuteMockedRunner() {
        MadcowTestCase testCase = new MadcowTestCase('testExecuteMockedRunner', new MadcowConfig(), getGrassScript());
        testCase.madcowConfig.stepRunner = 'au.com.ps4impact.madcow.step.MockMadcowStepRunner';
        testCase.execute();
    }

    void testExecuteMockedRunnerFailure() {
        MadcowTestCase testCase = new MadcowTestCase('testExecuteMockedRunner', new MadcowConfig(), getGrassScript());
        testCase.madcowConfig.stepRunner = 'au.com.ps4impact.madcow.step.MockMadcowStepRunner';
        testCase.madcowConfig.stepRunnerParameters = ['alwaysPass' : 'false'];
        try {
            testCase.execute();
            fail('should always exception due to step failure')
        } catch (e) {
            assertEquals('Step failed - Status: FAIL | Message: Mocked Fail', e.message)
        }
    }

    void testExecuteRunnerDoesNotExist() {
        try {
            MadcowTestCase testCase = new MadcowTestCase('testExecuteRunnerDoesNotExist', new MadcowConfig(), getGrassScript());
            testCase.madcowConfig.stepRunner = '.tent';
            testCase.execute();
            fail('should always exception with ClassNotFoundException');
        } catch (e) {
            assertTrue( e.message.startsWith("The specified MadcowStepRunner '.tent' cannot be found"));
        }
    }

    void testExecuteRunnerUnknownException() {
        try {
            MadcowTestCase testCase = new MadcowTestCase('testExecuteRunnerUnknownException', new MadcowConfig(), getGrassScript());
            testCase.madcowConfig.stepRunner = 'au.com.ps4impact.madcow.util.ResourceFinder';
            testCase.execute();
            fail('should always exception with Exception');
        } catch (e) {
            assertTrue( e.message.startsWith("Unexpected error creating the MadcowStepRunner"));
        }
    }
}
