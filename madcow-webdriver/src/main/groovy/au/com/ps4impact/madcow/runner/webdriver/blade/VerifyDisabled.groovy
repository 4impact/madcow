package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.openqa.selenium.WebElement

class VerifyDisabled extends WebDriverBladeRunner {

    @Override
    void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        WebElement elementToCheck = findElement(stepRunner, step)
        if(elementToCheck.isEnabled()) {
            step.result = MadcowStepResult.FAIL("Expected element to be enabled, but it was not.")
        } else {
            step.result = MadcowStepResult.PASS()
        }
    }

    protected boolean allowEmptyParameterValue() {
        return true;
    }


    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.STATEMENT];
    }

}
