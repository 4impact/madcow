package au.com.ps4impact.madcow.runner.webdriver.blade.table.currentRow

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.runner.webdriver.blade.AbstractBladeTestCase
import au.com.ps4impact.madcow.step.MadcowStep
import org.junit.Test


/**
 * Created by bert on 8/24/2016.
 */
class StoreTest extends AbstractBladeTestCase {

    def value = new Value()

    protected MadcowStep executeBlade(GrassBlade blade, boolean reloadPage = true) {
        if (reloadPage) {
            (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        }
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        return step;
    }

    @Test
    void testStoreInDataParameter() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        executeBlade(blade, true);

        blade = new GrassBlade("theTable.table.currentRow.value = ['Column Number 1' : 'Tennis']", testCase.grassParser);
        executeBlade(blade, true);

        blade = new GrassBlade("theTable.table.currentRow.store = ['Column Number 1' : 'Parameter']", testCase.grassParser);
        executeBlade(blade, true);

    }

    @Test
    void testStoredDateInDataParameter() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 1\' : \'2013-01-08\']', testCase.grassParser);
        executeBlade(blade, true);

        blade = new GrassBlade("theTable.table.currentRow.store = ['Column Number 1' : 'Parameter']", testCase.grassParser);
        executeBlade(blade, true);

        blade = new GrassBlade("theTable.table.currentRow.value = ['Column Number 2' : '@Parameter']", testCase.grassParser);
        executeBlade(blade, true);
    }

    @Test
    void testStoredDateTimeInDataParameter() {
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 1\' : \'2013-10-09T15:38:15\']', testCase.grassParser);
        executeBlade(blade, true);

        blade = new GrassBlade("theTable.table.currentRow.store = ['Column Number 1' : 'Parameter']", testCase.grassParser);
        executeBlade(blade, true);

        blade = new GrassBlade("theTable.table.currentRow.value = ['Column Number 2' : '@Parameter']", testCase.grassParser);
        executeBlade(blade, true);
    }

}