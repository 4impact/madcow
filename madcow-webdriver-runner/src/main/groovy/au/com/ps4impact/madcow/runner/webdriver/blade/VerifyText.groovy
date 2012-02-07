package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * VerifyText.
 */
class VerifyText extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        if (stepRunner.driver.pageSource.contains(step.blade.parameters as String))
            step.result = MadcowStepResult.PASS();
        else
            step.result = MadcowStepResult.FAIL("Page doesn't contain text '${step.blade.parameters as String}'");
    }

}
