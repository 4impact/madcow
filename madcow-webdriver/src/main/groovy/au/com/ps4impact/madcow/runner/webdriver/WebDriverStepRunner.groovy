/*
 * Copyright 2012 4impact, Brisbane, Australia
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.runner.webdriver.driver.WebDriverType
import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.apache.commons.io.FileUtils
import org.openqa.selenium.Dimension
import org.openqa.selenium.ElementNotVisibleException
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParseException
import org.openqa.selenium.firefox.FirefoxProfile
import au.com.ps4impact.madcow.MadcowTestCase
import org.openqa.selenium.NoSuchElementException
import com.gargoylesoftware.htmlunit.BrowserVersion
import org.openqa.selenium.remote.Augmenter
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.events.EventFiringWebDriver

import java.util.concurrent.TimeUnit

/**
 * Implementation of the WebDriver step runner.
 *
 * @author Gavin Bunney
 */
class WebDriverStepRunner extends MadcowStepRunner {

    public EventFiringWebDriver driver;
    public WebDriverType driverType;
    public String currentPageSource;
    public String lastPageSource;
    public String lastPageTitle;
    public boolean initRemoteTimedOut = false;
    def driverParameters = null;
    private Dimension windowSize = null;
    private Long implicitTimeout;
    private Long scriptTimeout;
    private Long pageLoadTimeout;

    WebDriverStepRunner(MadcowTestCase testCase, HashMap<String, String> parameters) {

        this.testCase = testCase;

        // default the browser if not specified
        parameters.browser = StringUtils.upperCase(parameters.browser ?: "${WebDriverType.HTMLUNIT.toString()}");
        initialiseTimeouts(parameters)
        // check if width or height is supplied, that both are supplied
        if (parameters.windowWidth && parameters.windowHeight) {
            windowSize = new Dimension(parameters.windowWidth as Integer, parameters.windowHeight as Integer)
        } else if ((parameters.windowWidth && !parameters.windowHeight) || (!parameters.windowWidth && parameters.windowHeight)) {
            throw new RuntimeException("You need to specify both 'windowWidth' and 'windowHeight' or neither at all");
        }

        try {
            driverType = WebDriverType.getDriverType(parameters.browser);
            if (driverType == null)
                throw new ClassNotFoundException("Unknown browser '${parameters.browser}'")

            testCase.logInfo("Configuring WebDriver browser '${driverType.name}'")

            switch (driverType) {
                case WebDriverType.REMOTE:

                    driverParameters = [:];
                    if ((parameters.remoteServerUrl ?: '') != '') {
                        driverParameters.url = parameters.remoteServerUrl;
                        if ((parameters.emulate ?: '') != '') {
                            switch (StringUtils.upperCase(parameters.emulate)) {
                                case 'IE':
                                    driverParameters.desiredCapabilities = DesiredCapabilities.internetExplorer();
                                    break;
                                case 'CHROME':
                                    driverParameters.desiredCapabilities = DesiredCapabilities.chrome();
                                    break;
                                case 'SAFARI':
                                    driverParameters.desiredCapabilities = DesiredCapabilities.safari();
                                    break;
                                case 'PHANTOMJS':
                                    driverParameters.desiredCapabilities = DesiredCapabilities.phantomjs();
                                    break;
                                case 'IPAD':
                                    driverParameters.desiredCapabilities = DesiredCapabilities.ipad();
                                    break;
                                case 'IPHONE':
                                    driverParameters.desiredCapabilities = DesiredCapabilities.iphone();
                                    break;
                                case 'FIREFOX':
                                default:
                                    driverParameters.desiredCapabilities = DesiredCapabilities.firefox();
                                    break;
                            }
                        }
                        driverParameters.desiredCapabilities.setCapability("selenium-version", "2.52.0");

                        //set javascript on
                        driverParameters.desiredCapabilities.setJavascriptEnabled(true);
                        driverParameters.requiredCapabilities = null;

                        testCase.logInfo("Test case will attempt to start using remoteServerUrl '${parameters.remoteServerUrl}'")
                    } else {
                        throw new Exception("Cannot start '${driverType.name}' WebDriver without remoteServerUrl config parameter");
                    }
                    break;

                case WebDriverType.CHROME:
                    break;

                case WebDriverType.FIREFOX:
                    driverParameters = new FirefoxProfile()
                    driverParameters.setEnableNativeEvents(true)
                    break;

                case WebDriverType.IE:
                    break;

                case WebDriverType.HTMLUNIT:
                    driverParameters = BrowserVersion.FIREFOX_38;
                    if ((parameters.emulate ?: '') != '') {
                        switch (StringUtils.upperCase(parameters.emulate)) {
                            case 'IE8':
                                driverParameters = BrowserVersion.INTERNET_EXPLORER_8;
                                break;
                            case 'IE11':
                            case 'IE':
                                driverParameters = BrowserVersion.INTERNET_EXPLORER_11;
                                break;
                            case 'CHROME':
                                driverParameters = BrowserVersion.CHROME;
                                break;
                            case 'FF38':
                            case 'FIREFOX':
                            default:
                                driverParameters = BrowserVersion.FIREFOX_38;
                                break;
                        }
                    }
                    testCase.logInfo("Emulating HtmlUnit browser '${driverParameters.getNickname()}'")
                    break;
                default:
                    break;
            }

            initialiseDriver();

        } catch (ClassNotFoundException cnfe) {
            throw new Exception("The specified Browser '${parameters.browser}' cannot be found: $cnfe.message");
        } catch (ClassCastException cce) {
            throw new Exception("The specified Browser '${parameters.browser}' isn't a WebDriver! $cce.message");
        } catch (Exception e) {
            throw new Exception("Unexpected error creating the Browser '${parameters.browser}': $e.message");
        }
    }

    private void initialiseTimeouts(HashMap<String, String> parameters) {
        if (parameters.implicitTimeout && parameters.implicitTimeout.isLong()) {
            implicitTimeout = parameters.implicitTimeout.toLong()
        }
        if (parameters.scriptTimeout) {
            scriptTimeout = parameters.scriptTimeout as Long;
        }
        if (parameters.pageLoadTimeout) {
            pageLoadTimeout = parameters.pageLoadTimeout as Long;
        }

    }

    /**
     * Hook to allow subclasses to override something after the step runner driver has been initialised.
     */
    protected void afterDriverInitialised() {
        // ready for overriding
    }

    /**
     * Attempts to initialise the drive, retrying as required.
     */
    private initialiseDriver() {
        def retryCount = 0;
        while (retryCount <= 3 && this.driver == null) {

            retryCount++;
            try {
                try {
                    testCase.logDebug("Instantiating Driver instance")

                    WebDriver browserDriver;
                    if (this.driverParameters != null) {
                        browserDriver = this.driverType.driverClass.newInstance(this.driverParameters) as WebDriver
                    } else {
                        browserDriver = this.driverType.driverClass.newInstance() as WebDriver
                    }

                    if (browserDriver != null) {
                        this.driver = new EventFiringWebDriver(browserDriver);

                        testCase.logDebug("Setting up timeouts...")
                        setupDriverTimeouts();

                        if (windowSize) {
                            testCase.logInfo("Resizing default browser size to ${windowSize.width} x ${windowSize.height}")
                            driver.manage().window().setSize(windowSize);
                        }

                        this.afterDriverInitialised();
                    }
                } catch (ClassNotFoundException cnfe) {
                    throw new Exception("The specified Browser '${driverType.name}' cannot be found: $cnfe.message");
                } catch (ClassCastException cce) {
                    throw new Exception("The specified Browser '${driverType.name}' isn't a WebDriver! $cce.message");
                } catch (Exception e) {
                    throw new Exception("Unexpected error creating the Browser '${driverType.name}': $e.message");
                }
            } catch (Exception ex) {
                testCase.logWarn("Failed to initialise driver! Retry number ${retryCount}... ")
                if (retryCount >= 3) {
                    throw ex
                }
                testCase.logDebug("Exception was ${ex}")
            }
        }
    }

    /**
     * Setup the required driver timeout values as provided in config
     *
     * @param madcowDriverParams driver params
     */
    private setupDriverTimeouts() {

        try {
            if (implicitTimeout) {
                //attempt to set the provided timeout value
                this.driver.manage().timeouts().implicitlyWait(implicitTimeout, TimeUnit.SECONDS);
            }
            if (scriptTimeout) {
                //attempt to set the javascript timeout value
                this.driver.manage().timeouts().setScriptTimeout(scriptTimeout, TimeUnit.SECONDS);
            }

            if (pageLoadTimeout) {
                //attempt to set the page load timeout value
                this.driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
            }
        } catch (ignored) {
            // ignore any errors setting up timeouts - e.g. html unit doesn't support them all!
        }
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
     * Hook to allow subclasses to override before a step is executed.
     */
    protected void beforeExecuteStep(MadcowStep step, String pageSource) {
        // ready for overriding
    }

    /**
     * Hook to allow subclasses to override after a step is executed.
     */
    protected void afterExecuteStep(MadcowStep step, String pageSource) {
        // ready for overriding
    }

    /**
     * Execute the madcow step for a given test case.
     */
    public void execute(MadcowStep step) {

        // only execute if this is not a skipped test
        if (step.testCase.ignoreTestCase) {
            step.result = MadcowStepResult.NOT_YET_EXECUTED('Skipped!');
            return;
        }

        this.beforeExecuteStep(step, driver?.pageSource);

        WebDriverBladeRunner bladeRunner = getBladeRunner(step.blade) as WebDriverBladeRunner;
        try {
            bladeRunner.execute(this, step);

            if (!driver.title?.equals(lastPageTitle)) {
                lastPageTitle = driver.title;
                if (lastPageTitle != '') {
                    testCase.logInfo("Current Page: $lastPageTitle");
                }
            }

            currentPageSource = driver?.pageSource;
            if (currentPageSource && !currentPageSource.equals(lastPageSource)) {
                captureHtmlResults(step);
                lastPageSource = currentPageSource;
            }

        } catch (NoSuchElementException nsee) {
            step.result = MadcowStepResult.FAIL("Element '${step.blade.mappingSelectorType} : ${step.blade.mappingSelectorValue}' not found on the page!\nDetails: ${nsee.message}");
        } catch (ElementNotVisibleException ignored) {
            step.result = MadcowStepResult.FAIL("Element '${step.blade.mappingSelectorType} : ${step.blade.mappingSelectorValue}' can't be seen - be sure to click on anything required to display it!");
        } catch (e) {
            step.result = MadcowStepResult.FAIL("Element '${step.blade.mappingSelectorType} : ${step.blade.mappingSelectorValue}' has an unknown error:\n$e");
        }

        this.afterExecuteStep(step, currentPageSource);
    }

    /**
     * Determine if the step runner has a blade runner capable of executing the step.
     * This is used during test 'compilation' to see if it can even be done.
     */
    public boolean hasBladeRunner(GrassBlade blade) {
        try {
            WebDriverBladeRunner bladeRunner = getBladeRunner(blade);
            if (bladeRunner == null) {
                testCase.logError("Blade Runner not found for ${blade.toString()}");
                return false;
            }

            return bladeRunner.isValidBladeToExecute(blade);

        } catch (GrassParseException gpe) {
            throw gpe;
        } catch (e) {
            testCase.logError("Blade Runner not found for ${blade.toString()}\n\nException: $e");
            return false;
        }
    }

    /**
     * Capture the html result file.
     */
    public void captureHtmlResults(MadcowStep step) {
        new File("${step.testCase.resultDirectory.path}/${step.sequenceNumberString}.html") << addBaseMetaTagToPageSource(currentPageSource);
        capturePNGScreenShot(step);
        step.result.hasResultFile = true;
    }

    /**
     * Alter the retrieved page source to use the fully-qualified domain names in href and src links
     *
     * @param pageSource the page source as retrieved by WebDriver
     * @return an altered version of the pageSource with fully-qualified domain names
     */
    private String addBaseMetaTagToPageSource(String pageSource) {
        try {
            // not already a base element
            if (!pageSource.contains("<base") &&
                    driver?.currentUrl != null &&
                    !(driver.currentUrl.equals("about:blank"))) {
                def baseURL = new URL(driver.currentUrl); // may need to use different url here
                return pageSource.replace("<head>", '<head><base href="' + baseURL + '"/>')
            }

            return pageSource
        } catch (Exception ignored) {
            return pageSource
        }
    }

    private void capturePNGScreenShot(MadcowStep step) {

        if (!(driver.wrappedDriver instanceof RemoteWebDriver)) {
            return;
        }

        try {
            File screenShot
            if (!(driver.wrappedDriver instanceof TakesScreenshot)) {
                WebDriver augmentedDriver = new Augmenter().augment(driver.wrappedDriver as RemoteWebDriver)
                screenShot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE)
            } else {
                screenShot = ((TakesScreenshot) driver.wrappedDriver as RemoteWebDriver).getScreenshotAs(OutputType.FILE)
            }

            File destinationFile = new File("${step.testCase.resultDirectory.path}/${step.sequenceNumberString}.png");
            FileUtils.deleteQuietly(destinationFile);
            FileUtils.moveFile(screenShot, destinationFile);
            step.result.hasScreenshot = true;
        } catch (Exception e) {
            step.testCase.logWarn("Failed to take screenshot: ${e}");
        }
    }

    /**
     * Retrieve the default mappings selector this step runner.
     * This is used as the 'type' of selector when no type is given.
     * For WebDriver, this is 'id'.
     */
    public String getDefaultSelector() {
        return 'id';
    }

    public void finishTestCase() {
        if (driver != null) {
            if (driverType == WebDriverType.CHROME) {
                driver.quit();
            } else {
                driver.close();
            }
        }
    }
}
