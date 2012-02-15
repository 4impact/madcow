package au.com.ps4impact.madcow.config

/**
 * Class for MadcowConfig testing.
 */
class MadcowConfigTest extends GroovyTestCase {

    void testConfigCreation() {
        MadcowConfig config = new MadcowConfig();
        assertNotNull(config.execution);
    }

}