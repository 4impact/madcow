package au.com.ps4impact.madcow.step;

/**
 * Test class for Blade Runner.
 */
class BladeRunnerTest extends GroovyTestCase {

    public class MockBladeRunner extends BladeRunner {
        void execute(MadcowStepRunner stepRunner, MadcowStep step) { }
    }
    
    void testBladeRunnerCreated() {
        def bladeRunner = BladeRunner.getBladeRunner('au.com.ps4impact.madcow.step', 'BladeRunnerTest$MockBladeRunner');
        bladeRunner.execute(null, null);
    }
    
    void testBladeRunnerDoesNotExist() {
        try {
            BladeRunner.getBladeRunner('', 'tent');
            fail('should always exception with ClassNotFoundException');
        } catch (e) {
            assertTrue( e.message.startsWith("The specified BladeRunner '.tent' cannot be found"));
        }
    }

    void testBladeRunnerIsNotABladeRunner() {
        try {
            BladeRunner.getBladeRunner('au.com.ps4impact.madcow.util', 'ResourceFinder');
            fail('should always exception with ClassCastException');
        } catch (e) {
            assertTrue(e.message.startsWith("The specified BladeRunner 'au.com.ps4impact.madcow.util.ResourceFinder' isn't a BladeRunner!"));
        }
    }
}
