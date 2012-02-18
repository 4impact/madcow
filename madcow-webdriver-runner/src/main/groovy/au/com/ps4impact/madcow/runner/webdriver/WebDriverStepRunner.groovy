package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStep
import org.openqa.selenium.WebDriver
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult

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

    public void execute(MadcowTestCase testCase, MadcowStep step) {

        WebDriverBladeRunner bladeRunner = WebDriverBladeRunner.getBladeRunner(WebDriverBladeRunner.BLADE_PACKAGE, StringUtils.capitalize(step.blade.operation)) as WebDriverBladeRunner;
        try {
            bladeRunner.execute(this, step);
        } catch (e) {
            step.result = MadcowStepResult.FAIL("Unexpected Exception: $e");
        }
    }
}
