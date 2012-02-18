package au.com.ps4impact.madcow.step;

/**
 * Test class for Madcow Step Result.
 */
class MadcowStepResultTest extends GroovyTestCase {

    void testManualResult() {
        def result = new MadcowStepResult(MadcowStepResult.StatusType.PASS, 'All good');
        assertEquals(MadcowStepResult.StatusType.PASS, result.status);
        assertEquals('All good', result.message);
        assertToString(result, 'Status: PASS | Message: All good');
    }

    void testManualResultNoMessage() {
        def result = new MadcowStepResult(MadcowStepResult.StatusType.PASS);
        assertEquals(MadcowStepResult.StatusType.PASS, result.status);
        assertEquals(null, result.message);
        assertToString(result, 'Status: PASS | Message: null');
    }
    
    void testHelperPass() {
        def result = MadcowStepResult.PASS('Done!');
        assertEquals(MadcowStepResult.StatusType.PASS, result.status);
        assertTrue(result.passed());
        assertFalse(result.failed());
        assertFalse(result.noOperation());
        assertEquals('Done!', result.message);
        assertToString(result, 'Status: PASS | Message: Done!')
    }

    void testHelperFail() {
        def result = MadcowStepResult.FAIL('Done!');
        assertEquals(MadcowStepResult.StatusType.FAIL, result.status);
        assertTrue(result.failed());
        assertFalse(result.passed());
        assertFalse(result.noOperation());
        assertEquals('Done!', result.message);
        assertToString(result, 'Status: FAIL | Message: Done!')
    }

    void testHelperNoOperation() {
        def result = MadcowStepResult.NO_OPERATION('Done!');
        assertEquals(MadcowStepResult.StatusType.NO_OPERATION, result.status);
        assertTrue(result.noOperation());
        assertFalse(result.passed());
        assertFalse(result.failed());
        assertEquals('Done!', result.message);
        assertToString(result, 'Status: NO_OPERATION | Message: Done!')
    }
}
