package au.com.ps4impact.madcow.report

import au.com.ps4impact.madcow.MadcowProject
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.log4j.Logger
import org.apache.commons.io.FileUtils
import java.text.DecimalFormat
import au.com.ps4impact.madcow.MadcowTestSuite
import groovy.text.GStringTemplateEngine

/**
 * Madcow Execution Report.
 *
 * @author Gavin Bunney
 */
class MadcowExecutionReport implements IMadcowReport {

    protected static final Logger LOG = Logger.getLogger(MadcowExecutionReport.class);
    protected static final DecimalFormat TIME_SECONDS_FORMAT = new DecimalFormat("########.###");

    public void prepareReportDirectory() {

        if (new File(MadcowProject.MADCOW_REPORT_DIRECTORY).exists())
            new File(MadcowProject.MADCOW_REPORT_DIRECTORY).deleteDir();

        new File(MadcowProject.MADCOW_REPORT_DIRECTORY).mkdirs();
    }

    /**
     * Create the result files.
     */
    public void createTestCaseReport(MadcowTestCase testCase) {

        boolean isParseError = false;
        boolean isFailure = false;
        testCase.steps.each { step ->
            if (step.result == null)
                return;

            if (step.result.parseError())
                isParseError = true;
            else if (step.result.failed())
                isFailure = true;
        }

        def binding = [ 'testCase'          : testCase,
                        'testName'          : testCase.name,
                        'steps'             : testCase.steps,
                        'isParseError'      : isParseError,
                        'isFailure'         : isFailure,
                        'totalSteps'        : testCase.steps.size(),
                        'totalTime'         : testCase.getTotalTimeInSeconds() + 's',
                        'lastExecutedStep'  : testCase.lastExecutedStep,
                      ];

        try {
            def engine = new GStringTemplateEngine();
            def templateEngine = engine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'result-madcow-testcase.gtemplate').URL);
            def template = templateEngine.make(binding);
            String templateContents = template.toString();
            def result = new File(testCase.getResultDirectory().path + "/index.html");
            result.write(templateContents);
        } catch (e) {
            LOG.error("Error creating the Madcow Test Case Execution Report: $e");
        }
    }

    /**
     * Create Test Suite level report.
     */
    public void createTestSuiteReport(MadcowTestSuite testSuite) {

        def allTestCases = testSuite.getTestCasesRecusively();

        int passedCount = 0;
        int failedCount = 0;
        Long totalTime = 0L;
        allTestCases.each { testCase ->
            if (testCase.lastExecutedStep.result.failed()) {
                failedCount++;
            } else {
                passedCount++;
            }

            if (!testCase.lastExecutedStep.result.parseError())
                totalTime += testCase.stopWatch.time;
        }

        def binding = [ 'testSuite'         : testSuite,
                        'passedCount'       : passedCount,
                        'failedCount'       : failedCount,
                        'totalTime'         : TIME_SECONDS_FORMAT.format(totalTime / 1000) + 's',
                        'totalTimeExec'     : TIME_SECONDS_FORMAT.format(totalTime > 0 ? testSuite.stopWatch.time / 1000 : 0) + 's',
                      ];

        try {
            def engine = new GStringTemplateEngine();
            def templateEngine = engine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'result-madcow-testsuite.gtemplate').URL);
            def template = templateEngine.make(binding);
            String templateContents = template.toString();
            def result = new File("${MadcowProject.MADCOW_REPORT_DIRECTORY}/index.html");
            result.write(templateContents);
        } catch (e) {
            LOG.error("Error creating the Madcow Test Suite Execution Report: $e");
        }

        // copy the assets if they are available
        File assetsDir = new File('./.madcow/assets');
        if (assetsDir.exists()) {
            LOG.info("Copying assets for Madcow Report...");
            FileUtils.copyDirectory(assetsDir, new File("${MadcowProject.MADCOW_REPORT_DIRECTORY}/.assets"));
        } else {
            LOG.warn("No assets found for Madcow Report...");
        }
    }
}