package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.mappings.MadcowMappings
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder
import java.util.concurrent.TimeUnit

/**
 * Test for the ClickLink BladeRunner.
 *
 * @author Gavin Bunney
 */
class ValueTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ValueTest', new MadcowConfig(), []);
    def value = new Value();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifyValueExecution(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        (testCase.stepRunner as WebDriverStepRunner).driver.manage().timeouts().implicitlyWait(1, TimeUnit.MICROSECONDS);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testValueByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aInputId.value = Tennis', testCase.grassParser);
        verifyValueExecution(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aInputId', ['id': 'aInputId']);
        blade = new GrassBlade('aInputId.value = Tennis', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testValueByName() {
        MadcowMappings.addMapping(testCase, 'aInputName', ['name': 'aInputName']);
        GrassBlade blade = new GrassBlade('aInputName.value = Tennis', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testValueByXPath() {
        MadcowMappings.addMapping(testCase, 'aInputXPath', ['xpath': '//input[@id=\'aInputId\']']);
        GrassBlade blade = new GrassBlade('aInputXPath.value = Tennis', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testValueDoesNotExist() {
        GrassBlade blade = new GrassBlade('aInputThatDoesntExist.value = Tennis', testCase.grassParser);
        verifyValueExecution(blade, false);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.value = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(value.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.value = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(value.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.value', testCase.grassParser);
            assertFalse(value.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
