package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.openqa.selenium.WebElement


class IsSelected extends WebDriverBladeRunner {

    @Override
    void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        WebElement elementToCheck = findElement(stepRunner, step)
        if (!elementToCheck) {
            step.result = MadcowStepResult.FAIL("Unable to find element")
            return
        }
        if (elementToCheck.tagName.equals('input') && elementToCheck.getAttribute('type').equals('radio')) {
            if (elementToCheck.getAttribute('checked')) {
                step.result = MadcowStepResult.PASS()
            } else {
                step.result = MadcowStepResult.FAIL("Expected radio button to be selected")
            }
        } else {
            step.result = MadcowStepResult.FAIL("Element is not a radio button")
        }
    }

    protected boolean allowEmptyParameterValue() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.STATEMENT];
    }

}
