package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * ClickLink.
 *
 * @author Gavin Bunney
 */
class ClickLink extends CurrentRowBladeRunner {

    protected static final String XPATH_POSTFIX = "//*[local-name() = 'a' and position() = 1]";

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        TableXPather xPather = new TableXPather(step.blade);

        if (!super.validateSelectedRow(xPather, step))
            return;

        GrassBlade originalBlade = step.blade;

        String cellXPath = xPather.getCellXPath(step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()], step.blade.parameters as String) + XPATH_POSTFIX;
        try {
            GrassBlade clickLinkBlade = step.blade.clone() as GrassBlade;
            clickLinkBlade.mappingSelectorType = WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH;
            clickLinkBlade.mappingSelectorValue = cellXPath;
            clickLinkBlade.operation = 'clickLink';
            clickLinkBlade.parameters = null;
            step.blade = clickLinkBlade;
            au.com.ps4impact.madcow.runner.webdriver.blade.ClickLink clickLinkBladeRunner = new au.com.ps4impact.madcow.runner.webdriver.blade.ClickLink();
            clickLinkBladeRunner.execute(stepRunner, step);
        } catch (e) {
            step.result = MadcowStepResult.FAIL("Unexpected exception: $e");
            step.result.detailedMessage = "Cell XPath: ${cellXPath}";
        }

        step.blade = originalBlade;
    }

    /**
     * List of supported blade types.
     */
    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    /**
     * Get the list of supported parameter types, which for table operations is a map
     */
    protected List<Class> getSupportedParameterTypes() {
        return [String.class];
    }
}