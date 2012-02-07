package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStep

/**
 *
 */
class WebDriverStepRunner extends MadcowStepRunner {

    public void execute(MadcowTestCase testCase, MadcowStep step) {
        step.result = MadcowStep.MadcowStepResult.PASS;
    }
}
