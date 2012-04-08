package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * WaitSeconds.
 */
class WaitSeconds extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        sleep(Integer.parseInt(step.blade.parameters as String) * 1000);
        step.result = MadcowStepResult.PASS();
    }

    protected boolean allowNullSelectorType() {
        return true;
    }

    protected boolean enforceNullSelectorType() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }
}
