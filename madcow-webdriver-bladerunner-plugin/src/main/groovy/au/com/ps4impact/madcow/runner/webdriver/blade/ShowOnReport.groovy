package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * ShowOnReport.
 *
 * @author Gavin Bunney
 */
class ShowOnReport extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        String elementText = getElementText(findElement(stepRunner, step));

        // TODO - allow formatting for things like links etc - parameter as Map?
        step.testCase.reportDetails.put(step.blade.parameters as String, elementText);
        step.result = MadcowStepResult.PASS();
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }
}
