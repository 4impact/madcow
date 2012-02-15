package au.com.ps4impact.madcow.execution

/**
 * Test for Command Line invocation of Madcow.
 */
class MadcowCLITest extends GroovyTestCase {

    void testTestToRunOption() {
        def options = MadcowCLI.parseArgs(['-t', 'AddressTest.grass'].toArray() as String[]);
        assertEquals('AddressTest.grass', options.ts.first());

        options = MadcowCLI.parseArgs(['-t', 'AddressTest.grass,AddressTest2.grass'].toArray() as String[]);
        assertArrayEquals(['AddressTest.grass', 'AddressTest2.grass'].toArray(), options.ts.toArray());

        options = MadcowCLI.parseArgs(['-t', '"AddressTest.grass,AddressTest2.grass"'].toArray() as String[]);
        assertArrayEquals(['AddressTest.grass', 'AddressTest2.grass'].toArray(), options.ts.toArray());
    }
}
