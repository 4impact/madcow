package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.mappings.MadcowMappings

class VerifyEnabledTest extends GroovyTestCase {

    def inputIdAndNameSuffixes = ['TextInput','CheckboxInput','ButtonInput','PasswordInput','RadioInput', 'SubmitInput','TextArea','Button']

    MadcowTestCase testCase = new MadcowTestCase('VerifyEnabledTest', new MadcowConfig(),[])
    String testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html').absolutePath

    protected executeBladeAndCheckResult(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).initialiseDriverWithRetriesIfRequired();
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    void testVerifyEnabledByHtmlId() {
        inputIdAndNameSuffixes.each {
            GrassBlade blade = new GrassBlade("enabled${it}.verifyEnabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, true)
        }
    }

    void testVerifyEnabledByName() {
        inputIdAndNameSuffixes.each {
            MadcowMappings.addMapping(testCase, 'inputName', ['name': "enabled${it}"]);
            GrassBlade blade = new GrassBlade("inputName.verifyEnabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, true)
        }
    }

    void testVerifyEnabledByXpath() {
        inputIdAndNameSuffixes.each {
            println "$it"
            MadcowMappings.addMapping(testCase, 'inputByXpath', ['xpath': "//*[@id='enabled${it}']"]);
            GrassBlade blade = new GrassBlade("inputByXpath.verifyEnabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, true)
        }
    }

    void testVerifyEnabledFailingByHtmlId() {
        inputIdAndNameSuffixes.each {
            GrassBlade blade = new GrassBlade("disabled${it}.verifyEnabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, false)
        }
    }

    void testVerifyEnabledFailingByName() {
        inputIdAndNameSuffixes.each {
            MadcowMappings.addMapping(testCase, 'inputName', ['name': "disabled${it}"]);
            GrassBlade blade = new GrassBlade("inputName.verifyEnabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, false)
        }
    }

    void testVerifyEnabledFailingByXpath() {
        inputIdAndNameSuffixes.each {
            MadcowMappings.addMapping(testCase, 'inputByXpath', ['xpath': "//*[@id='disabled${it}']"]);
            GrassBlade blade = new GrassBlade("inputByXpath.verifyEnabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, false)
        }
    }


}
