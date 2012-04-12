package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner

/**
 * Blade runner to wait for a specific element/text to appear
 *
 * @author: Gavin Bunney
 */
class WaitFor extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        // TODO - configurable timeout?
        final boolean found = (1..30).any {

            try {
                if (StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
                    if (step.blade.parameters ?: '' == '' || stepRunner.driver.pageSource.contains(step.blade.parameters as String))
                        return true;
                    else
                        throw new NoSuchElementException();
                } else {
                    def element = findElement(stepRunner, step);
                    if (StringUtils.isNotEmpty((step.blade.parameters ?: '')))
                        return element.text == step.blade.parameters as String;
                    else
                        return true;
                }
            } catch (Exception ignored) { }

            Thread.sleep(1000);
            return false;
        }

        if (found)
            step.result = MadcowStepResult.PASS();
        else
            step.result = MadcowStepResult.FAIL('Element didn\'t appear within the timeout');
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
