package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.StringUtils


class VerifyRegexText extends WebDriverBladeRunner {

    @Override
    void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        String regexToSearchFor = step.blade.parameters as String
        if (StringUtils.isEmpty(step.blade.mappingSelectorValue)) {
            checkForRegexMatchOnPage(stepRunner, regexToSearchFor, step)
        } else {
            checkForRegexMatchInField(stepRunner, step, regexToSearchFor)
        }
    }

    private void checkForRegexMatchInField(WebDriverStepRunner stepRunner, MadcowStep step, String regexToSearchFor) {
        String textToCheck = findElement(stepRunner, step).text
        if (textToCheck ==~ regexToSearchFor) {
            step.result = MadcowStepResult.PASS();
        } else {
            step.result = MadcowStepResult.FAIL("Element doesn't contain text matching regex '${step.blade.parameters as String}'. Actual value '${textToCheck}'");
        }
    }

    private void checkForRegexMatchOnPage(WebDriverStepRunner stepRunner, String regexToSearchFor, MadcowStep step) {
        if (stepRunner.driver.pageSource.find(regexToSearchFor)) {
            step.result = MadcowStepResult.PASS()
        } else {
            step.result = MadcowStepResult.FAIL("Page doesn't contain any text matching the regex pattern '${step.blade.parameters as String}'")
        }
    }

    protected boolean allowNullSelectorType() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected boolean allowEmptyParameterValue() {
        return true;
    }


}
