package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.mappings.MadcowMappings
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * Test for the ShowOnReport BladeRunner.
 *
 * @author Gavin Bunney
 */
class ShowOnReportTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('ShowOnReportTest', new MadcowConfig(), []);
    def showOnReport = new ShowOnReport();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifyShowOnReport(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testShowOnReportByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.showOnReport = The link name', testCase.grassParser);
        verifyShowOnReport(blade, true);
        assertEquals('A link', testCase.reportDetails['The link name']);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        blade = new GrassBlade('aLinkId.showOnReport = The link name', testCase.grassParser);
        verifyShowOnReport(blade, true);

        assertEquals('A link', testCase.reportDetails['The link name']);
    }

    void testShowOnReportByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.showOnReport = The link name', testCase.grassParser);
        verifyShowOnReport(blade, true);
        assertEquals('A link', testCase.reportDetails['The link name']);
    }

    void testShowOnReportByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.showOnReport = The link name', testCase.grassParser);
        verifyShowOnReport(blade, true);
        assertEquals('A link', testCase.reportDetails['The link name']);
    }

    void testShowOnReportByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.showOnReport = The link name', testCase.grassParser);
        verifyShowOnReport(blade, true);
        assertEquals('A link', testCase.reportDetails['The link name']);
    }

    void testMappingSelectorInvalidRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.showOnReport = Tennis', testCase.grassParser);
            blade.mappingSelectorType = 'invalidOne';
            assertFalse(showOnReport.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported mapping selector type \'invalidOne\'. Only [HTMLID, TEXT, NAME, XPATH] are supported.', e.message);
        }
    }

    void testMappingSelectorRequired() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.showOnReport = Tennis', testCase.grassParser);
            blade.mappingSelectorType = null;
            assertFalse(showOnReport.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Mapping selector must be supplied. One of [HTMLID, TEXT, NAME, XPATH] are supported.', e.message);
        }
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('testsite_menu_createAddress.showOnReport', testCase.grassParser);
            assertFalse(showOnReport.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }
}
