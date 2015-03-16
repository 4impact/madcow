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
import au.com.ps4impact.madcow.step.BladeRunner
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParseException
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.apache.commons.lang3.StringUtils

/**
 * Base WebDriver plugin class.
 *
 * @author Gavin Bunney
 */
abstract class WebDriverBladeRunner extends BladeRunner {

    /**
     * All WebDriver blade mapping selector types.
     */
    public static enum BLADE_MAPPING_SELECTOR_TYPE { HTMLID, TEXT, NAME, XPATH, CSS }

    /**
     * Called to execute a particular step operation.
     */
    public void execute(MadcowStepRunner stepRunner, MadcowStep step) {
        this.execute(stepRunner as WebDriverStepRunner, step)
    }

    /**
     * Called to execute a particular step operation.
     */
    public abstract void execute(WebDriverStepRunner stepRunner, MadcowStep step);

    /**
     * Validate the MadcowStep parameters and selectors are valid for this blade runner.
     */
    public boolean isValidBladeToExecute(GrassBlade blade) throws GrassParseException {

        if (!this.getSupportedBladeTypes().contains(blade.type))
            throw new GrassParseException("Unsupported grass format. Only grass blades of type '${this.getSupportedBladeTypes()}' are supported.");

        if (!this.allowNullSelectorType() && !this.enforceNullSelectorType() && blade.mappingSelectorType == null)
            throw new GrassParseException("Mapping selector must be supplied. One of ${this.getSupportedSelectorTypes()} are supported.");

        if (this.enforceNullSelectorType() && blade.mappingSelectorType != null)
            throw new GrassParseException("Mapping selector must not be supplied.");

        if (blade.mappingSelectorType != null && !this.getSupportedSelectorTypesAsStringArray().contains(blade.mappingSelectorType))
            throw new GrassParseException("Unsupported mapping selector type '${blade.mappingSelectorType}'. Only ${this.getSupportedSelectorTypes()} are supported.");

        if (this.getSupportedBladeTypes().contains(GrassBlade.GrassBladeType.EQUATION)) {
            if (!this.allowEmptyParameterValue()
                    && (   (blade.parameters == null)
                    || (blade.parameters.toString() == "")))
                throw new GrassParseException("Unsupported grass format. Parameter must have a value supplied.");

            if (blade.parameters != null && this.getSupportedParameterTypes() != null) {
                boolean validParameterClassType = false;

                for (Class paramType in this.getSupportedParameterTypes()) {
                    if (paramType.isInstance(blade.parameters))
                        validParameterClassType = true;
                }

                if (!validParameterClassType) {
                    throw new GrassParseException("Unsupported grass format. Only parameters of type '${this.getSupportedParameterTypes()*.simpleName}' are supported.");
                }
            }
        }

        return true;
    }

    /**
     * Get the collection of blade types (GrassBladeType) supported by this blade runner.
     * By default, all BladeTypes are supported.
     */
    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return GrassBlade.GrassBladeType.values();
    }

    /**
     * Get the list of selector types supported by this blade runner.
     * By default, all selector types are supported.
     */
    protected Collection<BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return BLADE_MAPPING_SELECTOR_TYPE.values();
    }

    /**
     * Get the list of selector types as a string array.
     */
    protected String[] getSupportedSelectorTypesAsStringArray() {
        return convertBladeMappingTypeCollectionToStringArray(this.getSupportedSelectorTypes());
    }

    /**
     * Determines if this blade runner allows null mapping selectors.
     * By default, always expect a non-null selector.
     */
    protected boolean allowNullSelectorType() {
        return false;
    }

    /**
     * Determine if this blade runner must have a null mapping selector.
     * By default, always expect non-null selector.
     */
    protected boolean enforceNullSelectorType() {
        return false;
    }
    
    /**
     * Convert the given collection of blade mapping selector types to a string array.
     */
    private String[] convertBladeMappingTypeCollectionToStringArray(Collection<BLADE_MAPPING_SELECTOR_TYPE> types) {
        ArrayList<String> stringArray = new ArrayList<String>();
        for (BLADE_MAPPING_SELECTOR_TYPE type in types) {
            stringArray.add(StringUtils.lowerCase(type.toString()));
        }
        return stringArray;
    }

    /**
     * Convert a string selector type to the enum BLADE_MAPPING_SELECTOR_TYPE value.
     */
    public static BLADE_MAPPING_SELECTOR_TYPE getSelectorType(String mappingSelectorType) {
        for (BLADE_MAPPING_SELECTOR_TYPE type in BLADE_MAPPING_SELECTOR_TYPE.values()) {
            if (type.toString().compareToIgnoreCase(mappingSelectorType) == 0)
                return type;
        }
        return null
    }

    /**
     * Get the list of supported parameter types.
     * By default only String is supported.
     */
    protected List<Class> getSupportedParameterTypes() {
        return [String.class];
    }

    /**
     * Determine if this blade runner allows an empty parameter value.
     * By default, it doesn't
     */
    protected boolean allowEmptyParameterValue() {
        return false;
    }

    /**
     * Locate an element for the mapped blade.
     */
    protected WebElement findElement(WebDriverStepRunner stepRunner, MadcowStep step) {

        switch (getSelectorType(step.blade.mappingSelectorType)) {
            case BLADE_MAPPING_SELECTOR_TYPE.TEXT:
                return stepRunner.driver.findElement(By.linkText(step.blade.mappingSelectorValue));

            case BLADE_MAPPING_SELECTOR_TYPE.NAME:
                return stepRunner.driver.findElement(By.name(step.blade.mappingSelectorValue));

            case BLADE_MAPPING_SELECTOR_TYPE.CSS:
                return stepRunner.driver.findElement(By.cssSelector(step.blade.mappingSelectorValue));

            case BLADE_MAPPING_SELECTOR_TYPE.XPATH:
                return stepRunner.driver.findElement(By.xpath(step.blade.mappingSelectorValue));

            case BLADE_MAPPING_SELECTOR_TYPE.HTMLID:
            default:
                return stepRunner.driver.findElement(By.id(step.blade.mappingSelectorValue));
        }

        return null;
    }

    /**
     * Get the web element text depending on the tag type - e.g. value for input's etc
     */
    protected String getElementText(WebElement webElement) {
        switch (StringUtils.lowerCase(webElement.tagName)) {
            case 'input':
                return StringUtils.trim(webElement.getAttribute('value'));
            case 'select':
                String selectedOptionText = '';
                webElement.findElements(By.tagName('option')).each { option ->
                    if (option.selected) {
                        selectedOptionText = StringUtils.trim(option.text);
                    }
                }
                return selectedOptionText;
            default:
                return StringUtils.trim(webElement.text);
        }
    }

    protected String getElementTextByExecutingXPath(WebDriverStepRunner stepRunner, String xpath) {
        return stepRunner.driver.executeScript("""
            return document.evaluate(arguments[0], document, null, XPathResult.ANY_TYPE, null).stringValue;
        """, xpath);
    }
}
