package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.log4j.Logger
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.util.PathFormatter
import fj.Effect
import fj.data.Option
import fj.Unit
import fj.control.parallel.QueueActor
import fj.control.parallel.Strategy
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import au.com.ps4impact.madcow.execution.ParallelTestCaseRunner

import au.com.ps4impact.madcow.report.JUnitMadcowReport
import au.com.ps4impact.madcow.report.MadcowExecutionReport
import au.com.ps4impact.madcow.report.IMadcowReport

/**
 * Madcow Test Coordinator class.
 */
class MadcowTestRunner {

    protected static final Logger LOG = Logger.getLogger(MadcowTestRunner.class);

    protected static List<IMadcowReport> reporters = [new MadcowExecutionReport(), new JUnitMadcowReport()];

    /**
     * Prep the results directory, but removing it
     * and creating folders ready for results!
     */
    protected static void prepareResultsDirectory() {
        
        if (new File(MadcowProject.RESULTS_DIRECTORY).exists())
            new File(MadcowProject.RESULTS_DIRECTORY).deleteDir();

        new File(MadcowProject.RESULTS_DIRECTORY).mkdir();

        reporters.each() { reporter -> reporter.prepareReportDirectory() };
    }

    /**
     * Main entry point to execute all the given tests.
     */
    static void executeTests(ArrayList<String> testNames = [], MadcowConfig madcowConfig) {

        prepareResultsDirectory();

        MadcowTestSuite testSuite = prepareTestSuite(testNames, madcowConfig);

        LOG.info("Found ${testSuite.size()} test cases to run");

        int numThreads = 10;
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        Strategy<Unit> strategy = Strategy.executorStrategy(pool);
        def numberOfTestsRan = 0
        def exceptions = [];

        def callback = QueueActor.queueActor(strategy, {Option<Exception> result ->
            numberOfTestsRan++;
            result.foreach({Exception e -> exceptions.add(e)} as Effect)
            if (numberOfTestsRan >= testSuite.size()) {
                pool.shutdown()
            }
        } as Effect).asActor()

        def allTestCases = testSuite.getTestCasesRecusively();

        allTestCases.each { MadcowTestCase testCase ->
            new ParallelTestCaseRunner(strategy, callback).act(fj.P.p(testCase, reporters));
        }

        while (numberOfTestsRan < testSuite.size()) {
            Thread.sleep(500);
        }

        reporters.each() { reporter -> reporter.createTestSuiteReport(testSuite) };
    }

    /**
     * Create the test suite collection for the given tests.
     */
    public static MadcowTestSuite prepareTestSuite(ArrayList<String> testNames = [], MadcowConfig madcowConfig) {

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

        MadcowTestSuite rootSuite = new MadcowTestSuite('');
        walkDirectoryTree(new File(MadcowProject.TESTS_DIRECTORY), rootSuite);

        testFilesToRun.each { File testFile ->

            String testName = StringUtils.removeEnd(PathFormatter.formatPathToPackage(testFile.canonicalPath, new File(MadcowProject.TESTS_DIRECTORY).canonicalPath), '.grass');
            MadcowTestCase testCase = new MadcowTestCase(testName, madcowConfig, testFile.readLines() as ArrayList<String>);
            MadcowTestSuite suite = rootSuite.locateSuite(testName) ?: rootSuite;
            testCase.testSuite = suite;
            suite.testCases.add(testCase);
        }

        return rootSuite;
    }


    protected static void walkDirectoryTree(File directory, MadcowTestSuite parentSuite) {

        if (!directory.exists())
            return;

        directory.eachDir { subdir ->
            MadcowTestSuite subdirSuite = new MadcowTestSuite(subdir.name, parentSuite);
            parentSuite.children.add(subdirSuite);
            walkDirectoryTree(subdir, subdirSuite);
        }
    }
}
