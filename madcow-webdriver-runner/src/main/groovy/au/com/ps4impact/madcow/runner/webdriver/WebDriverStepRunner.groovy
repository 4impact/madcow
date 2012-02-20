package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.openqa.selenium.WebDriver
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * Implementation of the WebDriver step runner.
 */
class WebDriverStepRunner extends MadcowStepRunner {

    public WebDriver driver;

    WebDriverStepRunner(HashMap<String, String> parameters) {

        // default the browser if not specified
        parameters.browser = parameters.browser ?: 'org.openqa.selenium.htmlunit.HtmlUnitDriver';

        try {
            driver = Class.forName(parameters.browser).newInstance() as WebDriver;
        } catch (ClassNotFoundException cnfe) {
            throw new Exception("The specified Browser '${parameters.browser}' cannot be found\n\n$cnfe");
        } catch (ClassCastException cce) {
            throw new Exception("The specified Browser '${parameters.browser}' isn't a WebDriver!\n\n$cce");
        } catch (e) {
            throw new Exception("Unexpected error creating the Browser '${parameters.browser}'\n\n$e");
        }

    }

    /**
     * Get a blade runner for the given GrassBlade.
     */
    protected WebDriverBladeRunner getBladeRunner(GrassBlade blade) {
        return WebDriverBladeRunner.getBladeRunner(WebDriverBladeRunner.BLADE_PACKAGE, StringUtils.capitalize(blade.operation)) as WebDriverBladeRunner;
    }

    /**
     * Execute the madcow step for a given test case.
     */
    public void execute(MadcowStep step) {
        WebDriverBladeRunner bladeRunner = getBladeRunner(step.blade) as WebDriverBladeRunner;
        try {
            bladeRunner.execute(this, step);
        } catch (e) {
            step.result = MadcowStepResult.FAIL("Unexpected Exception: $e");
        }
    }

    /**
     * Determine if the step runner has a blade runner capable of executing the step.
     * This is used during test 'compilation' to see if it can even be done.
     */
    public boolean hasBladeRunner(GrassBlade blade) {
        try {
            // TODO - should we also check the parameters to the blade runner are ok? probably...
            return getBladeRunner(blade) != null;
        } catch (e) {
            // swallow the exception as blade runner wasn't found
            return false;
        }
    }
}
