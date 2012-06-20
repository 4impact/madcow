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
 * Test for the SelectField BladeRunner.
 *
 * @author Gavin Bunney
 */
class SelectFieldTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ValueTest', new MadcowConfig(), []);
    def selectField = new SelectField();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifyValueExecution(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        (testCase.stepRunner as WebDriverStepRunner).driver.manage().timeouts().implicitlyWait(1, TimeUnit.MICROSECONDS);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testSelectFieldByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aSelectId.selectField = New Zealand', testCase.grassParser);
        verifyValueExecution(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aSelectId', ['id': 'aSelectId']);
        blade = new GrassBlade('aSelectId.selectField = United States', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testSelectFieldByName() {
        MadcowMappings.addMapping(testCase, 'aSelectName', ['name': 'aSelectName']);
        GrassBlade blade = new GrassBlade('aSelectName.selectField = New Zealand', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testSelectFieldByXPath() {
        MadcowMappings.addMapping(testCase, 'aSelectXPath', ['xpath': '//select[@id=\'aSelectId\']']);
        GrassBlade blade = new GrassBlade('aSelectXPath.selectField = New Zealand', testCase.grassParser);
        verifyValueExecution(blade, true);
    }

    void testSelectFieldDoesNotExist() {
        GrassBlade blade = new GrassBlade('aSelectThatDoesntExist.selectField = Tennis', testCase.grassParser);
        verifyValueExecution(blade, false);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectField = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(selectField.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectField = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(selectField.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, NAME, XPATH] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.selectField', testCase.grassParser);
            assertFalse(selectField.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
