package au.com.ps4impact.madcow.runner.webdriver.blade.table;

import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather

/**
 * The SelectRow table blade runner will save the specified table row.
 *
 * @author Gavin Bunney
 */
class SelectRow extends WebDriverBladeRunner {

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        TableXPather xPather = new TableXPather(step.blade);

        String rowPositionXPath;

        if (step.blade.parameters == "first")
            rowPositionXPath = xPather.getFirstRowPositionXPath()
        else if (step.blade.parameters == "last")
            rowPositionXPath = xPather.getLastRowPositionXPath()
        else if (step.blade.parameters.toString().toLowerCase() ==~ /row\d*/)
            rowPositionXPath = step.blade.parameters.toString().substring(3)
        else
            rowPositionXPath = xPather.getRowPositionXPath(step.blade.parameters)

        step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()] = rowPositionXPath;
        step.result = MadcowStepResult.PASS("Row selected: ${step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()]}");
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    /**
     * Get the list of supported parameter types, which for table operations is a map or string.
     */
    protected List getSupportedParameterTypes() {
        return [String.class, Map.class];
    }

    /**
     * Get the list of selector types supported by this blade runner.
     * HtmlId, Name and XPath are supported.
     */
    protected Collection<WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE> getSupportedSelectorTypes() {
        return [WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.HTMLID,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME,
                WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH];
    }
}
