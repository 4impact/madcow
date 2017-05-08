package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils

/**
 * Usage:
 * selectWindow = "titleOfTheWindow"
 *
 * @author Oliver Dela Cruz
 */
class SelectWindow extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        String title = step.blade.parameters as String
        String currentWindow = stepRunner.driver.windowHandle
        Set<String> windowHandles = stepRunner.driver.windowHandles
        for(String handle : windowHandles) {
            if(stepRunner.driver.switchTo().window(handle).getTitle() == title) {
                break
            } else {
                stepRunner.driver.switchTo().window(currentWindow)
            }
        }
    }

    protected List<Class> getSupportedParameterTypes() {
        return [String.class];
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
        return false;
    }


}
