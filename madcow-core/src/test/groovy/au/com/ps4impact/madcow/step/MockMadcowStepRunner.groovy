package au.com.ps4impact.madcow.step

import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Stubbed Madcow Step Runner.
 */
public class MockMadcowStepRunner extends MadcowStepRunner {

    boolean alwaysPass = true;

    MockMadcowStepRunner(HashMap<String, String> parameters) {
        alwaysPass = (parameters.alwaysPass != 'false');
    }

    void execute(MadcowTestCase testCase, MadcowStep step) {
        step.result = alwaysPass ? MadcowStepResult.PASS('Mocked Pass') : MadcowStepResult.FAIL('Mocked Fail');
    }
}