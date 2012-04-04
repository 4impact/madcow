package au.com.ps4impact.madcow.report

import au.com.ps4impact.madcow.MadcowProject
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.util.ResourceFinder
import groovy.text.GStringTemplateEngine
import org.apache.log4j.Logger

/**
 * Madcow Execution Report.
 */
class MadcowExecutionReport implements IMadcowReport {

    protected static final Logger LOG = Logger.getLogger(MadcowExecutionReport.class);

    public void prepareReportDirectory() {

        if (new File(MadcowProject.MADCOW_REPORT_DIRECTORY).exists())
            new File(MadcowProject.MADCOW_REPORT_DIRECTORY).delete();

        new File(MadcowProject.MADCOW_REPORT_DIRECTORY).mkdirs();
    }

    /**
     * Create the result files.
     */
    public void createTestCaseReport(MadcowTestCase testCase) {

        def binding = [ 'testName'          : testCase.name,
                        'steps'             : testCase.steps,
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
    public void createTestSuiteReport(ArrayList<MadcowTestCase> testSuite) {

        def binding = [ 'testSuite'          : testSuite,
                      ];

        try {
            def engine = new GStringTemplateEngine();
            def templateEngine = engine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'result-madcow-testsuite.gtemplate').URL);
            def template = templateEngine.make(binding);
            String templateContents = template.toString();
            def result = new File(testSuite.first().getResultDirectory().absolutePath + "/../index.html");
            result.write(templateContents);
        } catch (e) {
            LOG.error("Error creating the Madcow Test Suite Execution Report: $e");
        }
    }
}