package au.com.ps4impact.madcow.config

/**
 * Class for MadcowConfig testing.
 */
class MadcowConfigTest extends GroovyTestCase {

    void testConfigCreation() {
        MadcowConfig config = new MadcowConfig();
        assertNotNull(config.execution);

        config = new MadcowConfig("DEV");
        assertNotNull(config.execution);
    }

    void testParseMissingStepRunner() {
        MadcowConfig config = new MadcowConfig();
        try {
            config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                    <madcow>
                                        <execution>
                                            <runner/>
                                        </execution>
                                    </madcow>""", null);
            fail("Should always exception");
        } catch (e) {
            assertEquals("<runner> needs to be specified!", e.message)
        }
    }

    void testParseDefaultEnv() {
        MadcowConfig config = new MadcowConfig();
        config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                <madcow>
                                    <execution>
                                        <runner>some runner</runner>
                                        <env.default>DEV</env.default>
                                    </execution>
                                    <environments>
                                        <environment name="DEV" />
                                        <environment name="TEST" />
                                    </environments>
                                </madcow>""", null);
        assertEquals('Verify default environment found and loaded', 'DEV', config.environment.attribute('name'));

        config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                <madcow>
                                    <execution>
                                        <runner>some runner</runner>
                                    </execution>
                                    <environments>
                                        <environment name="DEV" />
                                        <environment name="TEST" />
                                    </environments>
                                </madcow>""", null);
        assertNull('Check environment is null, since there isnt a default', config.environment);

        try {
            config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                    <madcow>
                                        <execution>
                                            <runner>some runner</runner>
                                            <env.default>UNKNOWN ENV</env.default>
                                        </execution>
                                        <environments>
                                            <environment name="DEV" />
                                            <environment name="TEST" />
                                        </environments>
                                    </madcow>""", null);
            fail("Should always exception");
        } catch (e) {
            assertEquals('Check for exception defaulting to unknown env', "Environment 'UNKNOWN ENV' specified, but not found in config!", e.message);
        }

    }
}