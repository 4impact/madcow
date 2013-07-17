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

package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver
import au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitWebElement
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import com.gargoylesoftware.htmlunit.html.HtmlInput
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlTextArea
import org.openqa.selenium.Keys
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * The VerifySelectFieldOptions blade verify's the contents of a select field.
 *
 * @author Gavin Bunney
 */
class VerifySelectFieldOptions extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        def element = findElement(stepRunner, step);

        List<WebElement> options = element.findElements(By.tagName('option'));

        List<String> presentList = options*.text;
        List<String> expectedList = step.blade.parameters as List<String>;

        List<String> missingInPage = presentList.clone() as List<String>;
        List<String> missingInTest = expectedList.clone() as List<String>;

        expectedList.each { expected -> missingInTest.remove(expected)}
        presentList.each { present -> missingInPage.remove(present)}

        if ((missingInPage.size() == 0) && (missingInTest.size() == 0)) {
            step.result = MadcowStepResult.PASS();
            return;
        }

        step.result = MadcowStepResult.FAIL('Select field options do not match');
        step.result.detailedMessage = '';

        if (missingInPage.size() > 0)
            step.result.detailedMessage = "Missing in Page: ${missingInPage}<br/>";

        if (missingInTest.size() > 0)
            step.result.detailedMessage = "Missing in Test: ${missingInTest}<br/>";
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected List<Class> getSupportedParameterTypes() {
        return [List.class];
    }

    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.HTMLID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.CSS];
    }
}
