package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Test for the Madcow Test Runner class.
 */
class MadcowTestRunnerTest extends GroovyTestCase {

    void testExecuteTest() {
        def config = new MadcowConfig();
        config.stepRunner = "au.com.ps4impact.madcow.mock.MockMadcowStepRunner";

        MadcowTestRunner.executeTests(['DataParameterTest'], config);
        MadcowTestRunner.executeTests(['DataParameterTest.grass'], config);
    }
}
