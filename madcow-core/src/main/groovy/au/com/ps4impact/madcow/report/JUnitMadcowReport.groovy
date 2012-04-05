package au.com.ps4impact.madcow.report

import groovy.text.GStringTemplateEngine
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.MadcowProject
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * JUnit specific Test Case Report
 */
class JUnitMadcowReport implements IMadcowReport {

    public static final String JUNIT_RESULTS_DIRECTORY = MadcowProject.RESULTS_DIRECTORY + "/junit-results";
    public static final String JUNIT_RESULTS_XML_DIRECTORY = JUNIT_RESULTS_DIRECTORY + "/xml";
    public static final String JUNIT_RESULTS_HTML_DIRECTORY = JUNIT_RESULTS_DIRECTORY + "/html";

    public void prepareReportDirectory() {

        if (new File(JUNIT_RESULTS_DIRECTORY).exists())
            new File(JUNIT_RESULTS_DIRECTORY).delete();

        new File(JUNIT_RESULTS_XML_DIRECTORY).mkdirs();
        new File(JUNIT_RESULTS_HTML_DIRECTORY).mkdirs();
    }

    /**
     * Create the result files.
     */
    public void createTestCaseReport(MadcowTestCase testCase) {

        def testCaseResult = testCase.lastExecutedStep.result;

        def binding = [ 'errorCount'        : '0',
                        'failureCount'      : testCase.lastExecutedStep.result.failed() ? '1' : '0',
                        'hostname'          : InetAddress.localHost.hostName,
                        'testName'          : testCase.name,
                        'time'              : testCase.getTotalTimeInSeconds(),
                        'timestamp'         : new Date(testCase.stopWatch.startTime).format("yyyy-MM-dd'T'HH:mm:ss"),
                        'systemOut'         : testCaseResult.passed() ? "Passed" : '',
                        'systemErr'         : testCaseResult.failed() ? "Failed: " + testCaseResult.message : '',
                        'failure'           : testCaseResult.failed() ? testCaseResult.message : '',
                        'failureDetails'    : testCaseResult.failed() ? (testCaseResult.detailedMessage ?: '') : '',
        ];

        def engine = new GStringTemplateEngine();
        def template = engine.createTemplate(ResourceFinder.locateResourceOnClasspath(testCase.class.classLoader, 'result-junit.gtemplate').URL).make(binding);

        String templateContents = template.toString();
        def result = new File(JUNIT_RESULTS_XML_DIRECTORY + "/TEST-${StringUtils.replace(testCase.name, '/', '_')}.xml");
        result.write(templateContents);
    }

    /**
     * Create a HTML JUnit Report for all the TEST-*.xml files.
     */
    public void createTestSuiteReport(ArrayList<MadcowTestCase> testSuite) {

        def antBuilder = new AntBuilder();

        antBuilder.junitreport(todir: JUNIT_RESULTS_HTML_DIRECTORY) {
            fileset(dir: JUnitMadcowReport.JUNIT_RESULTS_XML_DIRECTORY) {
                include(name: "TEST-*.xml")
            }
            report(todir: JUNIT_RESULTS_HTML_DIRECTORY);
        }
    }
}