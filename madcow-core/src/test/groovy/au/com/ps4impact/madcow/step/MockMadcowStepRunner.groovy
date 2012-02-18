package au.com.ps4impact.madcow.step

import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Stubbed Madcow Step Runner.
 */
public class MockMadcowStepRunner extends MadcowStepRunner {
    
    MockMadcowStepRunner(HashMap<String, String> parameters) {
        //
    }

    void execute(MadcowTestCase testCase, MadcowStep step) {
        step.result = MadcowStepResult.PASS('Mocked Pass');
    }
}