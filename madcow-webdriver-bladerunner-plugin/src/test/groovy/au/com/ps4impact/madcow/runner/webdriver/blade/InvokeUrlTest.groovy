package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.step.MadcowStep

/**
 * Test for InvokeUrl Blade Runner
 *
 * @author Gavin Bunney
 */
class InvokeUrlTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('InvokeUrlTest', new MadcowConfig(), []);
    def invokeUrl = new InvokeUrl();

    void testInvokeUrl() {
        GrassBlade blade = new GrassBlade('invokeUrl = ADDRESSBOOK/test.html', testCase.grassParser);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        testCase.stepRunner.execute(step);
        assertTrue(step.result.passed());
        assertTrue(step.result.message.startsWith("URL now:"));
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('someElement.invokeUrl', testCase.grassParser);
            assertFalse(invokeUrl.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }

    void testParameterMustBeSupplied() {
        try {
            GrassBlade blade = new GrassBlade('invokeUrl =   ', testCase.grassParser);
            assertFalse(invokeUrl.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Parameter must have a value supplied.', e.message);
        }
    }
}
