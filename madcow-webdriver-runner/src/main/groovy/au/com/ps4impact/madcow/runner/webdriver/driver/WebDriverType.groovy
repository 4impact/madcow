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

package au.com.ps4impact.madcow.runner.webdriver.driver

/**
 * Enumerated type for the different web driver browsers.
 *
 * @author: Gavin Bunney
 */
public enum WebDriverType {

    HTMLUNIT('HtmlUnit', au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver),
    FIREFOX('Firefox', org.openqa.selenium.firefox.FirefoxDriver),
    CHROME('Chrome', org.openqa.selenium.chrome.ChromeDriver),
    IE('IE', org.openqa.selenium.ie.InternetExplorerDriver),
    PHANTOMJS('PhantomJs', org.openqa.selenium.phantomjs.PhantomJSDriver),
    REMOTE('Remote', au.com.ps4impact.madcow.runner.webdriver.driver.remote.MadcowRemoteWebDriver)

    public String name;
    public Class driverClass;

    WebDriverType(String name, Class driverClass) {
        this.name = name;
        this.driverClass = driverClass;
    }

    public static WebDriverType getDriverType(String type) {
        return WebDriverType.values().find() { WebDriverType driver -> driver.toString().toUpperCase() == type }
    }
}
