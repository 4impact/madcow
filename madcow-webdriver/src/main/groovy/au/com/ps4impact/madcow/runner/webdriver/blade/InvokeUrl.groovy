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

import au.com.ps4impact.madcow.grass.GrassParseException
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.config.MadcowConfig
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider

/**
 * InvokeUrl.
 *
 * @author Gavin Bunney
 */
class InvokeUrl extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        String urlToInvoke = step.blade.parameters as String;

        if (urlToInvoke.equals("REFRESH")){
           //check if driver already has url
           if (stepRunner.driver!=null
               && stepRunner.driver.currentUrl!=null
               && !stepRunner.driver.currentUrl.equals("")){
               //attempt current driver page refresh

               step.testCase.logInfo("Refreshing Current Page: $stepRunner.driver.currentUrl")
               stepRunner.driver.navigate().refresh();

               step.result = MadcowStepResult.PASS("Page Refreshed. URL now: <a href=\"${stepRunner.driver.currentUrl}\">${stepRunner.driver.currentUrl}</a>");
               return
           }
        }

        NodeList replacementUrls = step.env.invokeUrl as NodeList;
        if (replacementUrls != null && !replacementUrls.empty) {
            replacementUrls.first().children()?.each { child ->
                Node url = child as Node;
                urlToInvoke = StringUtils.replace(urlToInvoke, url.name().toString(), url.text());
            }
        }

        if ((MadcowConfig.SHARED_CONFIG?.environment?.attribute("username") ?: '') != '') {
            urlToInvoke = StringUtils.replaceOnce(urlToInvoke, '://',
                    "://${MadcowConfig.SHARED_CONFIG.environment.attribute("username") as String}:${MadcowConfig.SHARED_CONFIG.environment.attribute("password") as String}@");
        }

        step.testCase.logInfo("Opening Page: $urlToInvoke")
        stepRunner.driver.navigate().to(urlToInvoke);

        step.result = MadcowStepResult.PASS("URL now: <a href=\"${stepRunner.driver.currentUrl}\">${stepRunner.driver.currentUrl}</a>");
    }

    protected boolean enforceNullSelectorType() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }
}
