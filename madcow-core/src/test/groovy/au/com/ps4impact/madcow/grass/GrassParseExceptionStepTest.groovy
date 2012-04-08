package au.com.ps4impact.madcow.grass;

import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Test for the MadcowTestCase class.
 */
class GrassParseExceptionStepTest extends GroovyTestCase {

    void testParseExceptionReturnsStep() {
        MadcowTestCase testCase = new MadcowTestCase('testCreateAndParse', MockMadcowConfig.getMadcowConfig(), ['notAValidOperation = will fail']);

        try {
            testCase.parseScript();
            fail('should always exception')
        } catch (RuntimeException re) {
            def parseErrorStep = testCase.steps.first();
            assertTrue(parseErrorStep.result.parseError());
            assertEquals('Unsupported operation \'notAValidOperation\'', parseErrorStep.result.detailedMessage);
        }
    }
}