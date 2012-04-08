package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.step.MadcowStep
import org.apache.commons.lang3.time.StopWatch

/**
 * Test for InvokeUrl Blade Runner
 */
class WaitSecondsTest extends GroovyTestCase {

    MadcowTestCase testCase = new MadcowTestCase('WaitSecondsTest', new MadcowConfig(), []);
    def waitSeconds = new WaitSeconds();

    void testWaitSeconds() {
        StopWatch watch = new StopWatch();
        GrassBlade blade = new GrassBlade('waitSeconds = 2', testCase.grassParser);
        MadcowStep step = new MadcowStep(testCase, blade, null);
        watch.start();
        testCase.stepRunner.execute(step);
        watch.stop();
        assertTrue(step.result.passed());
        assertTrue(watch.time >= 2000);
    }

    void testStatementNotSupported() {
        try {
            GrassBlade blade = new GrassBlade('someElement.waitSeconds', testCase.grassParser);
            assertFalse(waitSeconds.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Only grass blades of type \'[EQUATION]\' are supported.', e.message);
        }
    }

    void testParameterMustBeSupplied() {
        try {
            GrassBlade blade = new GrassBlade('waitSeconds =   ', testCase.grassParser);
            assertFalse(waitSeconds.isValidBladeToExecute(blade));
            fail('should always exception');
        } catch (e) {
            assertEquals('Unsupported grass format. Parameter must have a value supplied.', e.message);
        }
    }
}
