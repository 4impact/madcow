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

import org.openqa.selenium.htmlunit.HtmlUnitWebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import com.gargoylesoftware.htmlunit.html.HtmlElement

/**
 * Madcow specific version of the HtmlUnitWebElement class.
 *
 * This allows us to expose protected methods on the parent class
 * that are needed to drive correctly.
 *
 * @author: Gavin Bunney
 */
class MadcowHtmlUnitWebElement extends HtmlUnitWebElement {

    public MadcowHtmlUnitWebElement(HtmlUnitDriver parent, HtmlElement element) {
        super(parent, element);
    }

    public HtmlElement getElement() {
        super.getElement();
    }

    /**
     * Override the toString method to not return anything so we dont have
     *  crazy htmlUnit element spam in the console as groovy will attempt to
     *  evaluate it
     *
     * @return
     */
    @Override
    public String toString(){

    }
}
