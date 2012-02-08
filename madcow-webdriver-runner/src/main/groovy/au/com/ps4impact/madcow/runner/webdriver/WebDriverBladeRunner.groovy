package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.BladeRunner

/**
 * Base WebDriver plugin class.
 */
abstract class WebDriverBladeRunner extends BladeRunner {

    public static final String BLADE_PACKAGE = 'au.com.ps4impact.madcow.runner.webdriver.blade';

    /**
     * Called to execute a particular step operation.
     */
    public void execute(MadcowStepRunner stepRunner, MadcowStep step) {
        this.execute(stepRunner as WebDriverStepRunner, step)
    }

    /**
     * Called to execute a particular step operation.
     */
    public abstract void execute(WebDriverStepRunner stepRunner, MadcowStep step);
}
