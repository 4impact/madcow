package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.log4j.Logger
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.util.PathFormatter
import au.com.ps4impact.madcow.report.JUnitTestCaseReport

/**
 * Madcow Test Coordinator class.
 */
class MadcowTestRunner {

    protected static final Logger LOG = Logger.getLogger(MadcowTestRunner.class);

    /**
     * Prep the results directory, but removing it
     * and creating folders ready for results!
     */
    static void prepareResultsDirectory() {
        
        if (new File(MadcowProject.RESULTS_DIRECTORY).exists())
            new File(MadcowProject.RESULTS_DIRECTORY).delete();

        new File(MadcowProject.RESULTS_DIRECTORY).mkdir();

        JUnitTestCaseReport.prepareResultsDirectory();
    }

    /**
     * Main entry point to execute all the given tests.
     */
    static void executeTests(ArrayList<String> testNames = [], MadcowConfig madcowConfig) {

        prepareResultsDirectory();

        ArrayList<File> testFilesToRun = new ArrayList<File>();

        if (testNames == null || testNames.empty) {
            testFilesToRun.addAll(ResourceFinder.locateFilesOnClasspath(this.classLoader, "**/*.grass", MadcowProject.TESTS_DIRECTORY));
        } else {
            testNames.each { String testName ->
                def filename = ResourceFinder.addFileExtensionIfRequired(testName, '.grass');
                testFilesToRun.add(ResourceFinder.locateFileOnClasspath(this.classLoader, "**/${filename}", MadcowProject.TESTS_DIRECTORY));
            }
        }

        if (testFilesToRun.empty) {
            LOG.error('No tests found to execute');
            throw new RuntimeException('No tests found to execute');
        }

        ArrayList<MadcowTestCase> testSuite = new ArrayList<MadcowTestCase>();
        testFilesToRun.each { File testFile ->
            String testName = StringUtils.removeEnd(PathFormatter.formatPathToPackage(testFile.canonicalPath, new File(MadcowProject.TESTS_DIRECTORY).canonicalPath), '.grass');
            testSuite.add(new MadcowTestCase(testName, madcowConfig, testFile.readLines() as ArrayList<String>));
        }
        
        // TODO - paraleleleleleleleise
        testSuite.each { MadcowTestCase testCase ->

            LOG.info("Running ${testCase.name}");

            try {
                testCase.execute();
                LOG.info("Test ${testCase.name} Passed");
            } catch (e) {
                LOG.error("Test ${testCase.name} Failed!");
            }

            JUnitTestCaseReport.createTestCaseResult(testCase);
        }
    }

}
