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

package au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.BrowserVersion
import org.openqa.selenium.WebElement
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider
import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Madcow specific HTML Unit Driver.
 *
 * @author: Gavin Bunney
 */
class MadcowHtmlUnitDriver extends HtmlUnitDriver {

    public MadcowHtmlUnitDriver(BrowserVersion browserVersion) {
        super(browserVersion);

        this.setJavascriptEnabled(true);
    }

    protected WebClient modifyWebClient(WebClient client) {
        super.modifyWebClient(client);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        client.getOptions().setThrowExceptionOnScriptError(false);

        if ((MadcowConfig.SHARED_CONFIG?.environment?.attribute("username") ?: '') != '') {
            DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
            provider.addCredentials(MadcowConfig.SHARED_CONFIG.environment.attribute("username") as String,
                                    MadcowConfig.SHARED_CONFIG.environment.attribute("password") as String);
            client.setCredentialsProvider(provider);
        }

        return client;
    }

    protected WebElement newHtmlUnitWebElement(HtmlElement element) {
        return new MadcowHtmlUnitWebElement(this, element);
    }

    //LOOK AT ADDING TAKESCREENSHOT ABILITY IN FUTURE TO ALLOW FOR FULL HTML SOURCE DOWNLOAD
    //https://selenium-developers.googlegroups.com/attach/ba6b454dab956c93/ScreenCaptureHtmlUnitDriver.java?view=1&part=4
}
