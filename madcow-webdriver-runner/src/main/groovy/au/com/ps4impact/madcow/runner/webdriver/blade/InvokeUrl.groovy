package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner

/**
 * InvokeUrl.
 */
class InvokeUrl extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        stepRunner.driver.get(step.blade.parameters as String);
        println("PAGE: ${stepRunner.driver.currentUrl} | TITLE: ${stepRunner.driver.title}");
        step.result = MadcowStep.MadcowStepResult.PASS;
    }

}
