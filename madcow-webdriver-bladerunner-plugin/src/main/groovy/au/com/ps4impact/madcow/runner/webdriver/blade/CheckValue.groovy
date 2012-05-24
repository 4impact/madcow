package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * CheckValue.
 *
 * @author Gavin Bunney
 */
class CheckValue extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        String elementText = getElementText(findElement(stepRunner, step));

        String expectedValue = StringUtils.trim(step.blade.parameters as String);
        if (expectedValue == elementText) {
            step.result = MadcowStepResult.PASS();
        } else {
            step.result = MadcowStepResult.FAIL("Expected: '$expectedValue', Present: '$elementText'");
        }
    }

    /**
     * Allow verifying the element is empty.
     */
    protected boolean allowEmptyParameterValue() {
        return true;
    }

    /**
     * Supported types of blades.
     */
    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    /**
     * Types of supported selectors.
     */
    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.HTMLID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH];
    }
}
