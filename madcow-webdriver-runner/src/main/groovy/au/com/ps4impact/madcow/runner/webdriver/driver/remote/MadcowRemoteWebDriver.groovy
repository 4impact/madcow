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

package au.com.ps4impact.madcow.runner.webdriver.driver.remote

import org.openqa.selenium.Capabilities
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.DriverCommand
import org.openqa.selenium.remote.HttpCommandExecutor
import org.openqa.selenium.remote.RemoteWebDriver

/**
 * Madcow Remote WebDriver
 *
 * @author Tom Romano
 */
public class MadcowRemoteWebDriver extends RemoteWebDriver implements TakesScreenshot {

    public MadcowRemoteWebDriver(){
        super();
    }

    public MadcowRemoteWebDriver(HashMap driverParameters) {
            super(new URL(driverParameters.url as String),
                  driverParameters.desiredCapabilities as Capabilities);
    }

    public <X> X getScreenshotAs(OutputType<X> target) {
        // Get the screenshot as base64.
        String base64 = execute(DriverCommand.SCREENSHOT).getValue().toString();
        // ... and convert it.
        return target.convertFromBase64Png(base64);
    }
}
