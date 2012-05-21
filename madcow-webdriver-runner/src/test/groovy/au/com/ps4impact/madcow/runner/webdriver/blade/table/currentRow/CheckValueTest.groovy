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
class CheckValueTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('CheckValueTest', new MadcowConfig(), []);
    CheckValue checkValue = new CheckValue();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected MadcowStep executeBlade(GrassBlade blade) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        return step;
    }

    protected MadcowStep verifyTableCheckValue(GrassBlade blade, boolean shouldPass) {
        MadcowStep step = executeBlade(blade);
        assertEquals(shouldPass, step.result.passed());
        return step;
    }

    void testCheckValue() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        verifyTableCheckValue(blade, true);
    }

    void testCheckValueInputField() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = [\'Column Number 1\' : \'Input Value\']', testCase.grassParser);
        verifyTableCheckValue(blade, true);
    }

    void testCheckValueFail() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = [\'Column Number 2\' : \'This will fail\']', testCase.grassParser);
        MadcowStep step = verifyTableCheckValue(blade, false);
        assertEquals("Expected: 'This will fail', Present: 'Country'", step.result.message);
    }

    void testCheckValueMultiples() {
        GrassBlade blade = new GrassBlade("theTable.table.selectRow = ['Column Number 2' : 'Country']", testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade("theTable.table.currentRow.checkValue = ['Column Number 1' : 'Input Value', 'Column Number 2' : 'Country']", testCase.grassParser);
        verifyTableCheckValue(blade, true);
    }

    void testCheckValueNeedToSelectRowFirst() {
        GrassBlade blade = new GrassBlade("theTable.table.currentRow.checkValue = ['Column Number 1' : 'Input Value']", testCase.grassParser);
        MadcowStep step = verifyTableCheckValue(blade, false);
        assertEquals('No row has been selected - call selectRow first', step.result.message);
    }

    void testCheckValueMapOnly() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade);

        blade = new GrassBlade('theTable.table.currentRow.checkValue = Country', testCase.grassParser);
        verifyTableCheckValue(blade, false);
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('theTable.table.currentRow.checkValue', testCase.grassParser);
            assertFalse(checkValue.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
