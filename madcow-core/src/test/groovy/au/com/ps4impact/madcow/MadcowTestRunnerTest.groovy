package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Test for the Madcow Test Runner class.
 */
class MadcowTestRunnerTest extends GroovyTestCase {

    void testExecuteTest() {

        MadcowTestRunner.executeTests([], new MadcowConfig());
    }
}
