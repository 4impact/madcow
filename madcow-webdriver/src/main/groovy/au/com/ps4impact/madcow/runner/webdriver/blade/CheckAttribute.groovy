package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils

/**
 * Created on 15/06/2016.
 * @author Danny Stevens
 */
class CheckAttribute  extends WebDriverBladeRunner {

	public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

		def element = findElement(stepRunner, step);
		if (!element) {
			step.result = MadcowStepResult.FAIL("Unable to find element!");
			return;
		}

		step.result = MadcowStepResult.PASS() // Good so far
		step.blade.parameters.each { String attributeName, String expectedValue ->
			expectedValue = StringUtils.trim(expectedValue)
			String actualValue = StringUtils.trim(element.getAttribute(attributeName)?:"")
			if (! (expectedValue == actualValue)) {
				step.result = MadcowStepResult.FAIL("Attribute: '$attributeName' Expected: '$expectedValue', Actual: '$actualValue'");
			}
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

	protected List<Class> getSupportedParameterTypes() {
		return [Map.class]
	}

	/**
	 * Types of supported selectors.
	 */
	protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
		return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.ID,
				WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
				WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH,
				WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.CSS];
	}
}