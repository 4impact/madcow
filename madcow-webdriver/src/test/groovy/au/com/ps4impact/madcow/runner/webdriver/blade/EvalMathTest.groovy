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
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.mappings.MadcowMappings
import org.openqa.selenium.Dimension

import org.junit.Test

import static groovy.test.GroovyAssert.*


class EvalMathTest extends AbstractBladeTestCase {

    ResizeBrowser resizeBrowser = new ResizeBrowser()

    protected verifyExecution(GrassBlade blade, boolean shouldPass, String resultingMessage = null) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        String sum = step.testCase.grassParser.getDataParameter("@sum")
        assertEquals("3.00",sum)
    }

    @Test
    void testEvalMath() {

        GrassBlade blade = new GrassBlade("evalMath = [name : 'sum' , value : '1 + 2']", testCase.grassParser);
        verifyExecution(blade, true);

    }


}
