package au.com.ps4impact.madcow

/**
 * Test class for MadcowStep.
 */
class MadcowTestSuiteTest extends GroovyTestCase {

    void testSuiteLocator() {
        MadcowTestSuite rootSuite = new MadcowTestSuite('');

        MadcowTestSuite searchSuite = new MadcowTestSuite('search', rootSuite);
        searchSuite.children.add(new MadcowTestSuite('addresses', searchSuite));

        rootSuite.children.add(searchSuite);
        rootSuite.children.add(new MadcowTestSuite('create', rootSuite));

        assertEquals('', rootSuite.locateSuite('SomeTestCase').name);
        assertEquals('addresses', rootSuite.locateSuite('search.addresses.TheTest').name);
        assertEquals('search', rootSuite.locateSuite('search.AddressTest').name);
        assertEquals('create', rootSuite.locateSuite('create.CreateTest').name);
    }
}
