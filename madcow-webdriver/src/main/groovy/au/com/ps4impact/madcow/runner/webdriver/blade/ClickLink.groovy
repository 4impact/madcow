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

import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.internal.Locatable

/**
 * The ClickLink Blade is used to click on an elements link.
 *
 * @author Gavin Bunney
 */
class ClickLink extends Click {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        try {
            WebElement webElement = findElementForClickStep(stepRunner, step);

            try {
                // scroll the element into view
                ((Locatable)webElement).getCoordinates()?.inViewPort();
            } catch (ignored) {
                // this can exception when there is _no_ viewport.. ignore it
            }

            webElement.findElement(By.tagName('a')).click();
        } catch (ignored) {
            // failed to click, try again with the element directly
            super.execute(stepRunner, step);
        }

        step.result = MadcowStepResult.PASS("URL now: <a href=\"${stepRunner.driver.currentUrl}\">${stepRunner.driver.currentUrl}</a>");
    }
}
