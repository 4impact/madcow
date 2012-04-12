package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.BladeRunner
import org.apache.commons.lang.StringUtils
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParseException
import org.openqa.selenium.WebElement
import org.openqa.selenium.By

/**
 * Base WebDriver plugin class.
 *
 * @author Gavin Bunney
 */
abstract class WebDriverBladeRunner extends BladeRunner {

    public static final String BLADE_PACKAGE = 'au.com.ps4impact.madcow.runner.webdriver.blade';

    /**
     * All WebDriver blade mapping selector types.
     */
    public static enum BLADE_MAPPING_SELECTOR_TYPE { HTMLID, TEXT, NAME, XPATH }

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

            if (blade.parameters != null && this.getSupportedParameterTypes() != null && !this.getSupportedParameterTypes().contains(blade.parameters.class))
                throw new GrassParseException("Unsupported grass format. Only parameters of type '${this.getSupportedParameterTypes()}' are supported.");
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
    protected BLADE_MAPPING_SELECTOR_TYPE getSelectorType(String mappingSelectorType) {
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
    protected List getSupportedParameterTypes() {
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

            case BLADE_MAPPING_SELECTOR_TYPE.XPATH:
                return stepRunner.driver.findElement(By.xpath(step.blade.mappingSelectorValue));

            case BLADE_MAPPING_SELECTOR_TYPE.HTMLID:
            default:
                return stepRunner.driver.findElement(By.id(step.blade.mappingSelectorValue));
        }

        return null;
    }
}
