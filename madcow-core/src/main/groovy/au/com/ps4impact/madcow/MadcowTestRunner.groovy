package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.log4j.Logger

/**
 * Madcow Test Coordinator class.
 */
class MadcowTestRunner {

    protected static final Logger LOG = Logger.getLogger(MadcowTestRunner.class);

    /**
     * Main entry point to execute all the given tests.
     */
    static void executeTests(ArrayList<String> testNames = [], MadcowConfig madcowConfig) {

        testNames.each { String testName ->

            def filename = ResourceFinder.addFileExtensionIfRequired(testName, '.grass');

            LOG.info("Running $filename");

            def testScript = ResourceFinder.locateFileOnClasspath(this.classLoader, "**/${filename}", MadcowProject.TESTS_DIRECTORY);
            MadcowTestCase testCase = new MadcowTestCase(testName, madcowConfig, testScript.readLines() as ArrayList<String>);
            testCase.execute();

            LOG.info("Test $filename completed");
        }
    }

}
