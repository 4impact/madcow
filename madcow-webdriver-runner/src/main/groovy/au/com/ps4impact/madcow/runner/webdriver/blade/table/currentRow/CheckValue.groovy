package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather
import org.openqa.selenium.By
import au.com.ps4impact.madcow.grass.GrassBlade
import org.openqa.selenium.WebElement

/**
 * CheckValue.
 *
 * @author Gavin Bunney
 */
class CheckValue extends CurrentRowBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        TableXPather xPather = new TableXPather(step.blade);

        if (!super.validateSelectedRow(xPather, step))
            return;

        step.blade.parameters.each { String column, String value ->

            String cellXPath = xPather.getCellXPath(step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()], column);

            WebElement element
            try {
                element = stepRunner.driver.findElements(By.xpath(cellXPath + '//*')).first();
            } catch (ignored) {
                element = stepRunner.driver.findElements(By.xpath(cellXPath + '/self::node()')).first();
            }

            String cellText;
            switch (StringUtils.lowerCase(element.tagName)) {
                case 'input':
                    cellText = StringUtils.trim(element.getAttribute('value'));
                    break;
                default:
                    cellText = StringUtils.trim(element.text);
                    break;
            }

            String expectedValue = StringUtils.trim(value);
            if (expectedValue == cellText) {
                step.result = MadcowStepResult.PASS();
            } else {
                step.result = MadcowStepResult.FAIL("Expected: '$expectedValue', Present: '$cellText'");
            }
        }
    }

    /**
     * List of supported blade types.
     */
    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    /**
     * Get the list of supported parameter types, which for table operations is a map
     */
    protected List<Class> getSupportedParameterTypes() {
        return [Map.class];
    }
}