package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * InvokeUrl.
 */
class InvokeUrl extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        def found = step.env.invokeUrl."${step.blade.parameters}".text();
        if (found!=null)
        {
            stepRunner.driver.get(found);
        }else{
            stepRunner.driver.get(step.blade.parameters as String);
        }

        step.result = MadcowStepResult.PASS("Page title: ${stepRunner.driver.title}");
    }

    /**
     * Allow null selectors.
     */
    protected boolean allowNullSelectorType() {
        return true;
    }
}
