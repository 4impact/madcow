package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import org.apache.commons.lang3.StringUtils

/**
 * InvokeUrl.
 *
 * @author Gavin Bunney
 */
class InvokeUrl extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        String urlToInvoke = step.blade.parameters as String;
        
        NodeList replacementUrls = step.env.invokeUrl as NodeList;
        if (replacementUrls != null && !replacementUrls.empty) {
            replacementUrls.first().children()?.each { child ->
                Node url = child as Node;
                urlToInvoke = StringUtils.replace(urlToInvoke, url.name().toString(), url.text());
            }
        }

        step.testCase.logInfo("Opening Page: $urlToInvoke")
        stepRunner.driver.navigate().to(urlToInvoke);

        step.result = MadcowStepResult.PASS("URL now: <a href=\"${stepRunner.driver.currentUrl}\">${stepRunner.driver.currentUrl}</a>");
    }

    protected boolean enforceNullSelectorType() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }
}
