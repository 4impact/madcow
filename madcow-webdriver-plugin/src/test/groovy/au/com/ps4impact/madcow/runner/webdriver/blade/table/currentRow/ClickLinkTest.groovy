package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * Test for the table ClickLink blade runner.
 *
 * @author Gavin Bunney
 */
class ClickLinkTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ClickLinkTest', new MadcowConfig(), []);
    ClickLink clickLink = new Value();
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

        blade = new GrassBlade("theTable.table.currentRow.clickLink = ['Column Number 1' : 'Tennis']", testCase.grassParser);
        verifyTableValue(blade, true, false);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = [\'Column Number 1\' : \'Tennis\']', testCase.grassParser);
        verifyTableValue(blade, true, false);
    }

    void testSetValueNeedToSelectRowFirst() {
        GrassBlade blade = new GrassBlade("theTable.table.currentRow.clickLink = ['Column Number 1' : 'Tent']", testCase.grassParser);
        MadcowStep step = verifyTableValue(blade, false);
        assertEquals('No row has been selected - call selectRow first', step.result.message);
    }

    void testSetValueMapOnly() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 1\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.clickLink = Tennis', testCase.grassParser);
        verifyTableValue(blade, false);
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.currentRow.clickLink', testCase.grassParser);
            assertFalse(clickLink.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
