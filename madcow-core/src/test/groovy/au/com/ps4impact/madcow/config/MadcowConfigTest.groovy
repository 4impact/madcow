package au.com.ps4impact.madcow.config

/**
 * Class for MadcowConfig testing.
 */
class MadcowConfigTest extends GroovyTestCase {

    void testConfigCreation() {
        MadcowConfig config = new MadcowConfig();
        assertNotNull(config.execution);

        config = new MadcowConfig('DEV');
        assertNotNull(config.execution);

        try {
            config = new MadcowConfig('UNKNOWN ENV');
            fail('should always exception');
        } catch (e)
        {
            assertEquals('Check for exception defaulting to unknown env', "Environment 'UNKNOWN ENV' specified, but not found in config!", e.message);
        }
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

    void testParseRunnerParameters() {
        MadcowConfig config = new MadcowConfig();
        config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                <madcow>
                                    <execution>
                                        <runner>
                                            <type>some runner</type>
                                        </runner>
                                    </execution>
                                </madcow>""", null);
        assertTrue('Verify no parameters defined at all is ok', config.stepRunnerParameters.isEmpty());

        config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                <madcow>
                                    <execution>
                                        <runner>
                                            <type>some runner</type>
                                            <parameters/>
                                        </runner>
                                    </execution>
                                </madcow>""", null);
        assertTrue('Verify parameters defined but none is ok', config.stepRunnerParameters.isEmpty());

        config = new MadcowConfig();
        try {
            config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                    <madcow>
                                        <execution>
                                            <runner>
                                                <type>some runner</type>
                                                <parameters>
                                                    <browser/>
                                                </parameters>
                                            </runner>
                                        </execution>
                                    </madcow>""", null);
            fail("Should always exception");
        } catch (e) {
            assertEquals("Runner parameter 'browser' defined without content!", e.message)
        }
    }

    void testParseEnv() {
        MadcowConfig config = new MadcowConfig();
        config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                <madcow>
                                    <execution>
                                        <runner><type>some runner</type></runner>
                                        <env.default>DEV</env.default>
                                    </execution>
                                    <environments>
                                        <environment name="DEV" />
                                        <environment name="TEST" />
                                    </environments>
                                </madcow>""", 'TEST');
        assertEquals('Verify environment found and loaded', 'TEST', config.environment.attribute('name'));

        try {
            config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                    <madcow>
                                        <execution>
                                            <runner><type>some runner</type></runner>
                                        </execution>
                                        <environments>
                                            <environment name="DEV" />
                                            <environment name="TEST" />
                                        </environments>
                                    </madcow>""", 'UNKNOWN ENV');
            fail("Should always exception");
        } catch (e) {
            assertEquals('Check for exception defaulting to unknown env', "Environment 'UNKNOWN ENV' specified, but not found in config!", e.message);
        }

    }

    void testParseDefaultEnv() {
        MadcowConfig config = new MadcowConfig();
        config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                <madcow>
                                    <execution>
                                        <runner><type>some runner</type></runner>
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
                                        <runner><type>some runner</type></runner>
                                        <env.default>DEV</env.default>
                                    </execution>
                                    <environments>
                                        <environment name="DEV" />
                                        <environment name="TEST" />
                                    </environments>
                                </madcow>""", 'TEST');
        assertEquals('Verify default environment not used, as env specified', 'TEST', config.environment.attribute('name'));

        config.parseConfig("""<?xml version="1.0" encoding="UTF-8"?>
                                <madcow>
                                    <execution>
                                        <runner><type>some runner</type></runner>
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
                                            <runner><type>some runner</type></runner>
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