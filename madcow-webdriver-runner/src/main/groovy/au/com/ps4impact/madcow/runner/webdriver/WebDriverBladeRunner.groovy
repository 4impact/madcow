package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.IBladeRunner

/**
 * Base WebDriver plugin class.
 */
abstract class WebDriverBladeRunner implements IBladeRunner {

    protected static final String BLADE_PACKAGE = 'au.com.ps4impact.madcow.runner.webdriver.blade';

    /**
     * Retrieve an instance of the specified BladeRunner.
     */
    public static WebDriverBladeRunner getBladeRunner(String bladeClassName) {
        WebDriverBladeRunner bladeRunner;

        try {
            return Class.forName("$BLADE_PACKAGE.$bladeClassName").newInstance() as WebDriverBladeRunner;
        } catch (ClassNotFoundException cnfe) {
            throw new Exception("The specified WebDriverBladeRunner '${bladeClassName}' cannot be found\n\n$cnfe");
        } catch (ClassCastException cce) {
            throw new Exception("The specified WebDriverBladeRunner '${bladeClassName}' isn't a WebDriverBladeRunner!\n\n$cce");
        } catch (e) {
            throw new Exception("Unexpected error creating the WebDriverBladeRunner '${bladeClassName}'\n\n$e");
        }
    }

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
