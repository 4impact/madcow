package au.com.ps4impact.madcow.step

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig

/**
 * Test class for MadcowStep.
 */
class MadcowStepTest extends GroovyTestCase {

    void testSequenceNumberFormat() {
        def step = new MadcowStep(new MadcowTestCase('StepTest', MockMadcowConfig.getMadcowConfig()), null, null);
        step.sequenceNumber = 377;
        assertEquals('0000377', step.getSequenceNumberString());
    }

    void testToString() {
        assertToString(new MadcowStep(new MadcowTestCase('StepTest', MockMadcowConfig.getMadcowConfig()), null, null), "[testCase: StepTest, blade: null, parent: null, children: []]") ;
    }
}
