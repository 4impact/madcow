package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.mappings.MadcowMappings
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import org.junit.Test

import static groovy.test.GroovyAssert.*

class VerifyDisabledTest extends AbstractBladeTestCase {

    def inputIdAndNameSuffixes = ['TextInput','CheckboxInput','ButtonInput','PasswordInput','RadioInput', 'SubmitInput','TextArea','Button']

    protected executeBladeAndCheckResult(GrassBlade blade, boolean shouldPass) {
        (testCase.stepRunner as WebDriverStepRunner).driver.get("file://${testHtmlFilePath}");
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertEquals(shouldPass, step.result.passed());
    }

    @Test
    void testVerifyEnabledByHtmlId() {
        inputIdAndNameSuffixes.each {
            GrassBlade blade = new GrassBlade("enabled${it}.verifyDisabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, false)
        }
    }

    @Test
    void testVerifyEnabledByName() {
        inputIdAndNameSuffixes.each {
            MadcowMappings.addMapping(testCase, 'mapping', ['name': "enabled${it}"]);
            GrassBlade blade = new GrassBlade("mapping.verifyDisabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, false)
        }
    }

    @Test
    void testVerifyEnabledByXpath() {
        inputIdAndNameSuffixes.each {
            println "$it"
            MadcowMappings.addMapping(testCase, 'mapping', ['xpath': "//*[@id='enabled${it}']"]);
            GrassBlade blade = new GrassBlade("mapping.verifyDisabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, false)
        }
    }

    @Test
    void testVerifyEnabledFailingByHtmlId() {
        inputIdAndNameSuffixes.each {
            GrassBlade blade = new GrassBlade("disabled${it}.verifyDisabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, true)
        }
    }

    @Test
    void testVerifyEnabledFailingByName() {
        inputIdAndNameSuffixes.each {
            MadcowMappings.addMapping(testCase, 'mapping', ['name': "disabled${it}"]);
            GrassBlade blade = new GrassBlade("mapping.verifyDisabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, true)
        }
    }

    @Test
    void testVerifyEnabledFailingByXpath() {
        inputIdAndNameSuffixes.each {
            MadcowMappings.addMapping(testCase, 'mapping', ['xpath': "//*[@id='disabled${it}']"]);
            GrassBlade blade = new GrassBlade("mapping.verifyDisabled",testCase.grassParser)
            executeBladeAndCheckResult(blade, true)
        }
    }


}
