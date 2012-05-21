package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * Test for the table CheckValue blade runner.
 *
 * @author Gavin Bunney
 */
class ValueTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ValueTest', new MadcowConfig(), []);
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected MadcowStep executeBlade(GrassBlade blade, boolean reloadPage = true) {
        if (reloadPage)
            (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        return step;
    }

    protected MadcowStep verifyTableValue(GrassBlade blade, boolean shouldPass, boolean reloadPage = true) {
        MadcowStep step = executeBlade(blade, reloadPage);
        assertEquals(shouldPass, step.result.passed());
        return step;
    }

    void testSetThenCheckValue() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade, true);

        blade = new GrassBlade("theTable.table.currentRow.value = ['Column Number 1' : 'Tennis']", testCase.grassParser);
        verifyTableValue(blade, true, false);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = [\'Column Number 1\' : \'Tennis\']', testCase.grassParser);
        verifyTableValue(blade, true, false);
    }
}
