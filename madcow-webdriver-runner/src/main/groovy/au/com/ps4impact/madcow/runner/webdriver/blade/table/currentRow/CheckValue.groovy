package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather
import org.openqa.selenium.By

/**
 *
 *
 * @author Gavin Bunney
 */
class CheckValue extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        TableXPather xPather = new TableXPather(step.blade);

        step.blade.parameters.each { String column, String value ->

            String cellXPath = xPather.getCellXPath(step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()], column);
            String cellText = StringUtils.trim(stepRunner.driver.findElement(By.xpath(cellXPath)).text);
            String expectedValue = StringUtils.trim(value);
            if (expectedValue == cellText) {
                step.result = MadcowStepResult.PASS();
            } else {
                step.result = MadcowStepResult.FAIL("Expected: '$expectedValue', Present: '$cellText'");
            }
        }

    }
}
