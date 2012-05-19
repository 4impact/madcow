package au.com.ps4impact.madcow.runner.webdriver.blade.table

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.mappings.MadcowMappings

/**
 * 
 *
 * @author: Gavin Bunney
 */
class SelectRowTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ClickLinkTest', new MadcowConfig(), []);
    def selectRow = new SelectRow();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifySelectRow(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
        assertTrue((testCase.runtimeStorage.get("webdriver.blade.table.createTable") as String).length() > 10);
    }

    void testSelectByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('theTable.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        verifySelectRow(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'theTableMapping', ['id': 'createTable']);
        blade = new GrassBlade('theTableMapping.table.selectRow = [\'Column Number 2\' : \'Country\']', testCase.grassParser);
        verifySelectRow(blade, true);
    }
}
