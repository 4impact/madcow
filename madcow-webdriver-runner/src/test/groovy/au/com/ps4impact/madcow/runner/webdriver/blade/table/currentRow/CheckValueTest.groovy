package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * 
 *
 * @author: Gavin Bunney
 */
class CheckValueTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('CheckValueTest', new MadcowConfig(), []);
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected MadcowStep executeBlade(GrassBlade blade) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        return step;
    }

    protected verifyTableCheckValue(GrassBlade blade, boolean shouldPass) {
        MadcowStep step = executeBlade(blade);
        assertEquals(shouldPass, step.result.passed());
    }

    void testCheckValue() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        verifyTableCheckValue(blade, true);
    }

    void testCheckValueFail() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = [\'Column Number 2\' : \'This will fail\']', testCase.grassParser);
        verifyTableCheckValue(blade, false);
    }
}
