package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.openqa.selenium.WebDriver
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParseException

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
            WebDriverBladeRunner bladeRunner = getBladeRunner(blade);
            if (bladeRunner == null)
                return false;

            return bladeRunner.isValidBladeToExecute(blade);

        } catch (GrassParseException gpe) {
            throw gpe;
        } catch (e) {
            // swallow the exception as blade runner wasn't found
            return false;
        }
    }

    /**
     * Retrieve the default mappings selector this step runner.
     * This is used as the 'type' of selector when no type is given.
     * For WebDriver, this is 'htmlid'.
     */
    public String getDefaultSelector() {
        return 'htmlid';
    }
}
