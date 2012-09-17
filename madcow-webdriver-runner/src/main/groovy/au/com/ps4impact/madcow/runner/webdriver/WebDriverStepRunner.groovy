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

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.openqa.selenium.WebDriver
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParseException
import org.openqa.selenium.firefox.FirefoxProfile
import au.com.ps4impact.madcow.runner.webdriver.driver.WebDriverType
import au.com.ps4impact.madcow.MadcowTestCase
import org.openqa.selenium.NoSuchElementException
import com.gargoylesoftware.htmlunit.BrowserVersion
import au.com.ps4impact.madcow.config.MadcowConfig;

/**
 * Implementation of the WebDriver step runner.
 *
 * @author Gavin Bunney
 */
class WebDriverStepRunner extends MadcowStepRunner {

    public WebDriver driver;
    public WebDriverType driverType;
    public String lastPageSource;
    public String lastPageTitle;

    WebDriverStepRunner(MadcowTestCase testCase, HashMap<String, String> parameters) {

        this.testCase = testCase;

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

                case WebDriverType.HTMLUNIT:
                    driverParameters = BrowserVersion.FIREFOX_3_6;
                    if ((parameters.emulate ?: '') != '') {
                        switch (StringUtils.upperCase(parameters.emulate)) {
                            case 'IE6':
                                driverParameters = BrowserVersion.INTERNET_EXPLORER_6;
                                break;
                            case 'IE7':
                                driverParameters = BrowserVersion.INTERNET_EXPLORER_7;
                                break;
                            case 'IE8':
                                driverParameters = BrowserVersion.INTERNET_EXPLORER_8;
                                break;
                            case 'FIREFOX':
                            default:
                                driverParameters = BrowserVersion.FIREFOX_3_6;
                                break;
                        }
                    }

                default:
                    break;
            }

            testCase.logInfo("Starting WebDriver browser '${driverType.name}'")

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

            if (!driver.title?.equals(lastPageTitle)) {
                lastPageTitle = driver.title;
                if (lastPageTitle != '') {
                    testCase.logInfo("Current Page: $lastPageTitle");
                }
            }

            if (!driver.pageSource?.equals(lastPageSource)) {
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
