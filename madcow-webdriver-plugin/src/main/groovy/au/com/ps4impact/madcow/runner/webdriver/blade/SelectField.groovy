package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver
import au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitWebElement
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import com.gargoylesoftware.htmlunit.html.HtmlInput
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlTextArea
import org.openqa.selenium.Keys
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * The SelectField blade sets a value on a combo box element.
 *
 * @author Gavin Bunney
 */
class SelectField extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        def element = findElement(stepRunner, step);

        def options = element.findElements(By.tagName('option'));
        WebElement foundOption = null;
        options.each { option ->
            if (option.text == step.blade.parameters as String) {
                foundOption = option;
            }
        }

        if (foundOption != null) {
            foundOption.click();
            step.result = MadcowStepResult.PASS();
        } else {
            step.result = MadcowStepResult.FAIL('Unable to find the specified option');
        }
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.HTMLID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH];
    }
}
