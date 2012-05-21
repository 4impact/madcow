package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather
import org.openqa.selenium.By
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * Base class for current row blade runners.
 *
 * @author Gavin Bunney
 */
abstract class CurrentRowBladeRunner extends WebDriverBladeRunner {

    protected boolean validateSelectedRow(TableXPather xPather, MadcowStep step) {

        if ((step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()] ?: '') == '') {
            step.result = MadcowStepResult.FAIL("No row has been selected - call selectRow first");
            return false;
        }

        return true;
    }
}