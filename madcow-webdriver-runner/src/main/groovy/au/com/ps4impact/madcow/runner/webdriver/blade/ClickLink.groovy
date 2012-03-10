package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * ClickLink.
 */
class ClickLink extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        WebElement link;
        switch (getSelectorType(step.blade.mappingSelectorType)) {
            case BLADE_MAPPING_SELECTOR_TYPE.TEXT:
                link = stepRunner.driver.findElement(By.linkText(step.blade.mappingSelectorValue));
                break;

            case BLADE_MAPPING_SELECTOR_TYPE.HTMLID:
            default:
                link = stepRunner.driver.findElement(By.id(step.blade.mappingSelectorValue));
                break;
        }

        link.click();

        step.result = MadcowStepResult.PASS("URL now ${stepRunner.driver.currentUrl}");
    }
}
