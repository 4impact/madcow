package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.log4j.Logger
import org.apache.commons.lang3.StringUtils

/**
 * Madcow Test Coordinator class.
 */
class MadcowTestRunner {

    protected static final Logger LOG = Logger.getLogger(MadcowTestRunner.class);

    /**
     * Main entry point to execute all the given tests.
     */
    static void executeTests(ArrayList<String> testNames = [], MadcowConfig madcowConfig) {

        ArrayList<File> testFilesToRun = new ArrayList<File>();

        // find all the tests if none are specified
        if (testNames == null || testNames.empty) {
            testFilesToRun.addAll(ResourceFinder.locateFilesOnClasspath(this.classLoader, "**/*.grass", MadcowProject.TESTS_DIRECTORY));
        } else {
            testNames.each { String testName ->
                def filename = ResourceFinder.addFileExtensionIfRequired(testName, '.grass');
                testFilesToRun.add(ResourceFinder.locateFileOnClasspath(this.classLoader, "**/${filename}", MadcowProject.TESTS_DIRECTORY));
            }
        }

        if (testFilesToRun.empty) {
            LOG.error("No tests found to execute", new RuntimeException());
            return;
        }
        
        testFilesToRun.each { File testFile ->
            
            String testName = StringUtils.removeEnd(testFile.name, '.grass');
            LOG.info("Running $testName");

            try {
                MadcowTestCase testCase = new MadcowTestCase(testName, madcowConfig, testFile.readLines() as ArrayList<String>);
                testCase.execute();
                LOG.info("Test $testName Passed");
            } catch (e) {
                LOG.error("Test $testName Failed!");
            }
        }
    }

}
