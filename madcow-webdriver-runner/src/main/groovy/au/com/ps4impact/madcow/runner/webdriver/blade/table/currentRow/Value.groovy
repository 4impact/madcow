package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.runner.webdriver.blade.table.util.TableXPather
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner

/**
 * Value.
 *
 * @author Gavin Bunney
 */
class Value extends CurrentRowBladeRunner {

    protected static final String XPATH_POSTFIX = "//*[(local-name() = 'input' or local-name() = 'textarea') and position() = 1]";

    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {

        TableXPather xPather = new TableXPather(step.blade);

        if (!super.validateSelectedRow(xPather, step))
            return;

        GrassBlade originalBlade = step.blade;

        step.blade.parameters.each { String column, String value ->

            try {
                GrassBlade valueBlade = step.blade.clone() as GrassBlade;
                valueBlade.mappingSelectorType = WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH;
                valueBlade.mappingSelectorValue = xPather.getCellXPath(step.testCase.runtimeStorage[xPather.getRuntimeStorageKey()], column) + XPATH_POSTFIX;
                valueBlade.operation = 'value';
                valueBlade.parameters = value;
                step.blade = valueBlade;
                au.com.ps4impact.madcow.runner.webdriver.blade.Value valueBladeRunner = new au.com.ps4impact.madcow.runner.webdriver.blade.Value();
                valueBladeRunner.execute(stepRunner, step);
            } catch (e) {
                step.result = MadcowStepResult.FAIL("Unexpected exception: $e");
            }
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
    protected List getSupportedParameterTypes() {
        return [Map.class];
    }
}