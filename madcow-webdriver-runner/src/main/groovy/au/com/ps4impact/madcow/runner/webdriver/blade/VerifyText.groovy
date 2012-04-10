package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils

/**
 * VerifyText.
 *
 * @author Gavin Bunney
 */
class VerifyText extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        if (StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
            if (stepRunner.driver.pageSource.contains(step.blade.parameters as String))
                step.result = MadcowStepResult.PASS();
            else
                step.result = MadcowStepResult.FAIL("Page doesn't contain text '${step.blade.parameters as String}'");
        } else {
            if (findElement(stepRunner, step).text.equals(step.blade.parameters as String))
                step.result = MadcowStepResult.PASS();
            else
                step.result = MadcowStepResult.FAIL("Element doesn't contain text '${step.blade.parameters as String}'");
        }
    }

    /**
     * Allow null selectors.
     */
    protected boolean allowNullSelectorType() {
        return true;
    }

    /**
     * Allow verifying the element is empty.
     */
    protected boolean allowEmptyParameterValue() {
        return true;
    }
}
