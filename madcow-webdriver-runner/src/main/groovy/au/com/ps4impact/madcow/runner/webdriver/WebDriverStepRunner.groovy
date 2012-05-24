package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.openqa.selenium.WebDriver
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParseException
import org.apache.log4j.Logger
import org.openqa.selenium.firefox.FirefoxProfile
import au.com.ps4impact.madcow.runner.webdriver.driver.WebDriverType

/**
 * Implementation of the WebDriver step runner.
 *
 * @author Gavin Bunney
 */
class WebDriverStepRunner extends MadcowStepRunner {

    protected static final Logger LOG = Logger.getLogger(WebDriverStepRunner.class);

    public WebDriver driver;
    public WebDriverType driverType;
    public String lastPageSource;

    WebDriverStepRunner(HashMap<String, String> parameters) {

        // default the browser if not specified
        parameters.browser = StringUtils.upperCase(parameters.browser ?: "${WebDriverType.HTMLUNIT.toString()}");

        try {
            driverType = WebDriverType.getDriverType(parameters.browser);
            if (driverType == null)
                throw new ClassNotFoundException("Unknown browser '${parameters.browser}'")

            def driverParameters = null;
            switch (driverType) {
                case WebDriverType.FIREFOX:
                    driverParameters = new FirefoxProfile();
                    driverParameters.setEnableNativeEvents(true);
                    break;

                default:
                    break;
            }

            LOG.info("Starting WebDriver browser '${driverType.toString()}'")

            if (driverParameters != null)
                driver = driverType.driverClass.newInstance(driverParameters) as WebDriver;
            else
                driver = driverType.driverClass.newInstance() as WebDriver;

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
        String operation = blade.operation;

        if (StringUtils.contains(operation, '.')) {
            operation = String.format("%s.%s", StringUtils.substringBeforeLast(operation, '.'),
                                               StringUtils.capitalize(StringUtils.substringAfterLast(operation, '.')));
        } else {
            operation = StringUtils.capitalize(operation);
        }

        return WebDriverBladeRunner.getBladeRunner(operation) as WebDriverBladeRunner;
    }

    /**
     * Execute the madcow step for a given test case.
     */
    public void execute(MadcowStep step) {
        WebDriverBladeRunner bladeRunner = getBladeRunner(step.blade) as WebDriverBladeRunner;
        try {
            bladeRunner.execute(this, step);
            if (!driver.pageSource.equals(lastPageSource)) {
                captureResults(step);
            }
        } catch (NoSuchElementException ignored) {
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
     * Capture the html result file.
     */
    public void captureResults(MadcowStep step) {
        new File("${step.testCase.resultDirectory.path}/${step.sequenceNumberString}.html") << driver.pageSource;
        lastPageSource = driver.pageSource;
        step.result.hasResultFile = true;
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
