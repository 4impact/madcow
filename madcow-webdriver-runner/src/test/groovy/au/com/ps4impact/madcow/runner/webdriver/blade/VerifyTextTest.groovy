package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.mappings.MadcowMappings

/**
 * Test for the VerifyTextTest BladeRunner.
 */
class VerifyTextTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('VerifyTextTest', new MadcowConfig(), []);
    def verifyText = new VerifyText();
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath;

    protected verifyTextContents(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testVerifyTextByHtmlId() {
        // defaults to html id
        GrassBlade blade = new GrassBlade('aLinkId.verifyText = A link', testCase.parseScript());
        verifyTextContents(blade, true);

        // explicit htmlid
        MadcowMappings.addMapping(testCase, 'aLinkId', ['id': 'aLinkId']);
        blade = new GrassBlade('aLinkId.clickLink', testCase.parseScript());
        verifyTextContents(blade, true);
    }

    void testVerifyTextIncorrect() {
        GrassBlade blade = new GrassBlade('aLinkId.verifyText = A link that isn\'t a link is still a link', testCase.parseScript());
        verifyTextContents(blade, false);
    }

    void testVerifyTextByName() {
        MadcowMappings.addMapping(testCase, 'aLinkName', ['name': 'aLinkName']);
        GrassBlade blade = new GrassBlade('aLinkName.verifyText = A link', testCase.parseScript());
        verifyTextContents(blade, true);
    }

    void testVerifyTextByXPath() {
        MadcowMappings.addMapping(testCase, 'aLinkXPath', ['xpath': '//a[@id=\'aLinkId\']']);
        GrassBlade blade = new GrassBlade('aLinkXPath.verifyText = A link', testCase.parseScript());
        verifyTextContents(blade, true);
    }

    void testVerifyTextByText() {
        MadcowMappings.addMapping(testCase, 'aLinkText', ['text': 'A link']);
        GrassBlade blade = new GrassBlade('aLinkText.verifyText = A link', testCase.parseScript());
        verifyTextContents(blade, true);
    }

    void testVerifyTextEmpty() {
        GrassBlade blade = new GrassBlade('anEmptyParagraphId.verifyText = ', testCase.parseScript());
        verifyTextContents(blade, true);
    }
}
