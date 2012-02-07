package au.com.ps4impact.madcow.step

/**
 * Test class for MadcowStep.
 */
class MadcowStepTest extends GroovyTestCase {

    void testToString() {
        assertToString(new MadcowStep(null, null), "[blade: null, parent: null, childen: []]") ;
    }
}
