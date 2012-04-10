package au.com.ps4impact.madcow.mock

import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Mock config for setting the MockMadcowStepRunner.
 *
 * @author Gavin Bunney
 */
class MockMadcowConfig {

    static MadcowConfig getMadcowConfig(boolean alwaysPass = true, String stepRunner = 'au.com.ps4impact.madcow.mock.MockMadcowStepRunner') {
        def config = new MadcowConfig();
        config.stepRunner = stepRunner;
        config.stepRunnerParameters = ['alwaysPass' : alwaysPass ? 'true' : 'false'];
        return config;
    }

}
