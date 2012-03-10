package au.com.ps4impact.madcow.runner.webdriver

import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.BladeRunner
import org.apache.commons.lang.StringUtils
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.grass.GrassParseException

/**
 * Base WebDriver plugin class.
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

        if (!this.allowNullSelectorType() && blade.mappingSelectorType == null)
            throw new GrassParseException("Mapping selector must be supplied. One of ${this.getSupportedSelectorTypes()} are supported.");

        if (blade.mappingSelectorType != null && !this.getSupportedSelectorTypesAsStringArray().contains(blade.mappingSelectorType))
            throw new GrassParseException("Unsupported mapping selector type '${blade.mappingSelectorType}'. Only ${this.getSupportedSelectorTypes()} are supported.");

        if (!this.getSupportedBladeTypes().contains(blade.type))
            throw new GrassParseException("Unsupported grass format. Only grass blades of type '${this.getSupportedBladeTypes()}' are supported.");

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
}
