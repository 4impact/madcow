package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.openqa.selenium.WebDriver
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParseException
import java.util.concurrent.TimeUnit
import org.apache.log4j.Logger
import org.openqa.selenium.firefox.FirefoxProfile

/**
 * Implementation of the WebDriver step runner.
 *
 * @author Gavin Bunney
 */
class WebDriverStepRunner extends MadcowStepRunner {

    protected static final Logger LOG = Logger.getLogger(WebDriverStepRunner.class);

    protected static final String SELENIUM_HTMLUNIT_DRIVER = org.openqa.selenium.htmlunit.HtmlUnitDriver.canonicalName;
    protected static final String MADCOW_HTMLUNIT_DRIVER = au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver.canonicalName;
    protected static final String FIREFOX_DRIVER = org.openqa.selenium.firefox.FirefoxDriver.canonicalName;

    public WebDriver driver;
    public String lastPageSource;

    WebDriverStepRunner(HashMap<String, String> parameters) {

        // default the browser if not specified
        parameters.browser = parameters.browser ?: MADCOW_HTMLUNIT_DRIVER;

        try {
            def driverParameters = null;
            switch (parameters.browser) {
                case FIREFOX_DRIVER:
                    driverParameters = new FirefoxProfile();
                    driverParameters.setEnableNativeEvents(true);
                    break;

                case SELENIUM_HTMLUNIT_DRIVER: // replace the selenium one with the madcow html unit driver
                    parameters.browser = MADCOW_HTMLUNIT_DRIVER;
                    break;

                default:
                    break;
            }

            LOG.info("Starting WebDriver browser '${parameters.browser}'")

            if (driverParameters != null)
                driver = Class.forName(parameters.browser).newInstance(driverParameters) as WebDriver;
            else
                driver = Class.forName(parameters.browser).newInstance() as WebDriver;

        } catch (ClassNotFoundException cnfe) {
            throw new Exception("The specified Browser '${parameters.browser}' cannot be found\n\n$cnfe");
        } catch (ClassCastException cce) {
            throw new Exception("The specified Browser '${parameters.browser}' isn't a WebDriver!\n\n$cce");
        } catch (e) {
            throw new Exception("Unexpected error creating the Browser '${parameters.browser}'\n\n$e");
        }

        // implicitly wait for things to appear if they don't
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
            if (!driver.pageSource.equals(lastPageSource)) {
                new File("${step.testCase.resultDirectory.path}/${step.sequenceNumberString}.html") << driver.pageSource;
                lastPageSource = driver.pageSource;
                step.result.hasResultFile = true;
            }
        } catch (NoSuchElementException nsee) {
            step.result = MadcowStepResult.FAIL("Element '${step.blade.mappingSelectorType} : ${step.blade.mappingSelectorValue}' not found on the page!");
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
            if (bladeRunner == null) {
                LOG.error("Blade Runner not found for ${blade.toString()}");
                return false;
            }

            return bladeRunner.isValidBladeToExecute(blade);

        } catch (GrassParseException gpe) {
            throw gpe;
        } catch (e) {
            LOG.error("Blade Runner not found for ${blade.toString()}\n\nException: $e");
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

    public void finishTestCase() {
        driver.close();
    }
}
