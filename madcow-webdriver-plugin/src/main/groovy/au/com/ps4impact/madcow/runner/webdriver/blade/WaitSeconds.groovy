package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * WaitSeconds.
 *
 * @author Gavin Bunney
 */
class WaitSeconds extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        int secondsToSleep;
        try {
            secondsToSleep = Integer.parseInt(step.blade.parameters as String);
        } catch (NumberFormatException ignored) {
            step.result = MadcowStepResult.FAIL("Invalid number '${step.blade.parameters}'");
            return;
        }

        step.testCase.logInfo("Waiting for $secondsToSleep seconds");
        sleep(secondsToSleep * 1000);
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
