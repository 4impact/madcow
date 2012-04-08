package au.com.ps4impact.madcow.report

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.time.StopWatch
import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * Test for the MadcowExecutionReport class.
 */
class MadcowExecutionReportTest extends GroovyTestCase {

    MadcowExecutionReport madcowReport = new MadcowExecutionReport();

    public void testTestCaseReportForPass() {
        madcowReport.prepareReportDirectory();

        def blade = new GrassBlade();
        blade.line = 'tent';

        MadcowTestCase testCase = new MadcowTestCase('testTestCaseReportForPass', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, blade, null));
        testCase.steps.first().result = MadcowStepResult.PASS();
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        madcowReport.createTestCaseReport(testCase);

        def result = new File(testCase.getResultDirectory().path + "/index.html");
        String resultHtml = result.text;

        assertTrue(resultHtml.contains(testCase.name));
        assertTrue(resultHtml.contains(blade.line));
    }

    public void testTestCaseReportForFailure() {
        madcowReport.prepareReportDirectory();

        def blade = new GrassBlade();
        blade.line = 'tent';

        MadcowTestCase testCase = new MadcowTestCase('testTestCaseReportForFailure', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, blade, null));
        testCase.steps.first().result = MadcowStepResult.FAIL('Some error occured');
        testCase.steps.first().result.detailedMessage = 'This could be a stacktrace or something.... \n\nCould be...'
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        madcowReport.createTestCaseReport(testCase);

        def result = new File(testCase.getResultDirectory().path + "/index.html");
        String resultHtml = result.text;

        assertTrue(resultHtml.contains(testCase.name));
        assertTrue(resultHtml.contains(blade.line));
    }
}
