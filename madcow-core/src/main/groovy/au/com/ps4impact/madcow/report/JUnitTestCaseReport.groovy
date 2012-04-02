package au.com.ps4impact.madcow.report

import groovy.text.GStringTemplateEngine
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.MadcowProject
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * JUnit specific Test Case Report
 */
class JUnitTestCaseReport extends MadcowTestCaseReport {

    protected static final String JUNIT_RESULTS_DIRECTORY = MadcowProject.RESULTS_DIRECTORY + "/junit-results";

    public static void prepareResultsDirectory() {

        if (new File(JUNIT_RESULTS_DIRECTORY).exists())
            new File(JUNIT_RESULTS_DIRECTORY).delete();

        new File(JUNIT_RESULTS_DIRECTORY).mkdir();
    }

    /**
     * Create the result files.
     */
    public static void createTestCaseResult(MadcowTestCase testCase) {

        def binding = [ 'errorCount'   : '0',
                        'failureCount' : testCase.lastExecutedStep.result.failed() ? '1' : '0',
                        'hostname'     : InetAddress.localHost.hostName,
                        'testName'     : testCase.name,
                        'time'         : (testCase.endTime.time - testCase.startTime.time) / (1000 * 60 * 60),
                        'timestamp'    : testCase.endTime.format("yyyy-MM-dd'T'HH:mm:ss"),
        ];

        def engine = new GStringTemplateEngine();
        def template = engine.createTemplate(ResourceFinder.locateResourceOnClasspath(testCase.class.classLoader, 'result-junit.gtemplate').URL).make(binding);

        String templateContents = template.toString();
        def result = new File(JUNIT_RESULTS_DIRECTORY + "/TEST-${StringUtils.replace(testCase.name, '/', '_')}.xml");
        result << templateContents;
    }
}