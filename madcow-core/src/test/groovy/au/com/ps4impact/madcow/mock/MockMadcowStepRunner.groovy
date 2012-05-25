package au.com.ps4impact.madcow.mock

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Stubbed Madcow Step Runner.
 *
 * @author Gavin Bunney
 */
class MockMadcowStepRunner extends MadcowStepRunner {

    boolean alwaysPass = true;

    MockMadcowStepRunner(MadcowTestCase testCase, HashMap<String, String> parameters) {
        this.testCase = testCase;
        alwaysPass = (parameters.alwaysPass != 'false');
    }

    void execute(MadcowStep step) {
        step.result = alwaysPass ? MadcowStepResult.PASS('Mocked Pass') : MadcowStepResult.FAIL('Mocked Fail');
    }

    boolean hasBladeRunner(GrassBlade blade) {
        switch (blade.operation) {
            case 'notAValidOperation':
                return false
            default:
                return true;
        }
    }

    public String getDefaultSelector() {
        return 'htmlid';
    }

    public void finishTestCase() {
        //
    }
}