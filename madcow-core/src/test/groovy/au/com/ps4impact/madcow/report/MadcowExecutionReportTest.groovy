package au.com.ps4impact.madcow.report

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.lang3.time.StopWatch
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestSuite
import au.com.ps4impact.madcow.MadcowProject

/**
 * Test for the MadcowExecutionReport class.
 */
class MadcowExecutionReportTest extends GroovyTestCase {

    MadcowExecutionReport madcowReport = new MadcowExecutionReport();

    protected void validateMadcowTestCaseReport(boolean passed, boolean failed, boolean parseError) {
        madcowReport.prepareReportDirectory();

        def blade = new GrassBlade();
        blade.line = 'tent';

        MadcowTestCase testCase = new MadcowTestCase('testTestCaseReportForPass', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, blade, null));
        testCase.steps.first().result = passed ? MadcowStepResult.PASS() : failed ? MadcowStepResult.FAIL('Some error') : MadcowStepResult.PARSE_ERROR('Unsupported operation');
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        madcowReport.createTestCaseReport(testCase);

        def result = new File(testCase.getResultDirectory().path + "/index.html");
        String resultHtml = result.text;

        assertTrue(resultHtml.contains(testCase.name));
        assertTrue(resultHtml.contains(blade.line));
        assertEquals(failed, resultHtml.contains('Test Failed'));
        assertEquals(parseError, resultHtml.contains('Test Parse Error'));
    }

    public void testTestCaseReportForPass() {
        validateMadcowTestCaseReport(true, false, false);
    }

    public void testTestCaseReportForFailure() {
        validateMadcowTestCaseReport(false, true, false);
    }

    public void testTestCaseReportForParseError() {
        validateMadcowTestCaseReport(false, false, true);
    }

    public void testTestSuiteReport() {
        madcowReport.prepareReportDirectory();

        def blade = new GrassBlade();
        blade.line = 'tent';

        MadcowTestCase testCase = new MadcowTestCase('testTestSuiteReport01', MockMadcowConfig.getMadcowConfig(true))
        testCase.steps.add(new MadcowStep(testCase, blade, null));
        testCase.steps.first().result = MadcowStepResult.PASS();
        testCase.stopWatch = new StopWatch();
        testCase.stopWatch.start();
        testCase.stopWatch.stop();
        testCase.lastExecutedStep = testCase.steps.first();

        MadcowTestCase testCase2 = new MadcowTestCase('testTestSuiteReport02', MockMadcowConfig.getMadcowConfig(true))
        testCase2.steps.add(new MadcowStep(testCase2, blade, null));
        testCase2.steps.first().result = MadcowStepResult.FAIL('Some error occured');
        testCase2.stopWatch = new StopWatch();
        testCase2.stopWatch.start();
        testCase2.stopWatch.stop();
        testCase2.lastExecutedStep = testCase2.steps.first();

        MadcowTestSuite suite = new MadcowTestSuite('', null, [testCase, testCase2]);

        madcowReport.createTestCaseReport(testCase);
        madcowReport.createTestCaseReport(testCase2);
        madcowReport.createTestSuiteReport(suite);

        def suiteReportFile = new File(MadcowProject.MADCOW_REPORT_DIRECTORY + '/index.html');
        String suiteReportFileText = suiteReportFile.text;

        assertTrue(suiteReportFile.exists())
        assertTrue(suiteReportFileText.contains('1 passed'))
        assertTrue(suiteReportFileText.contains('1 failed'))
        assertTrue(suiteReportFileText.contains(testCase.name))
        assertTrue(suiteReportFileText.contains(testCase2.name))
    }
}
