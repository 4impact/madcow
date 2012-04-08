package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.mock.MockMadcowConfig

/**
 * Test for the Madcow Test Runner class.
 */
class MadcowTestRunnerTest extends GroovyTestCase {

    void testExecuteTest() {
        MadcowTestRunner.executeTests(['DataParameterTest'], MockMadcowConfig.getMadcowConfig());
        MadcowTestRunner.executeTests(['DataParameterTest.grass'], MockMadcowConfig.getMadcowConfig());
    }

    void testExecuteNonFound() {
        try {
            MadcowTestRunner.prepareTestSuite(['InvalidTestName'], MockMadcowConfig.getMadcowConfig());
            fail('should always exception')
        } catch (e) {
            assertTrue(e.message.startsWith('Unable to find matches on the classpath for \'**/InvalidTestName.grass\''))
        }
    }

    void testSuiteBuilder() {
        // even though this is in a package callled 'param' in test resources, due to where the execution takes place
        // the relative 'tests' directory is seen as basedir/tests not madcow-core/main/test/resources/tests
        // so the suite automagicness doesn't remove the front of the package - so it just gets lumped into the root suite
        def suite = MadcowTestRunner.prepareTestSuite(['DataParameterTest'], MockMadcowConfig.getMadcowConfig());
        assertEquals('', suite.name)
        assertEquals(1, suite.testCases.size())
        assertEquals(1, suite.size())
        assertEquals(0, suite.children.size())
        assertTrue(suite.testCases.first().name.endsWith('DataParameterTest'))
    }
}
