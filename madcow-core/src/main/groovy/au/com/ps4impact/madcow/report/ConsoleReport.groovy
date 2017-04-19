package au.com.ps4impact.madcow.report

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.MadcowTestSuite
import au.com.ps4impact.madcow.MadcowTestSuiteResult
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * Madcow Execution Report for the console.
 *
 * @author Paul Bevis
 */
class ConsoleReport implements IMadcowReport {

    protected static final String SEPARATOR = '-------------------------------------------------------------------------------------------';

    void prepareReportDirectory() {
        // nothing
    }

    void createTestCaseReport(MadcowTestCase testCase) {
        // nothing
    }

    void createTestSuiteReport(MadcowTestSuite testSuite) {
        MadcowTestSuiteResult results = testSuite.buildResults()

        println('\n\n' + SEPARATOR)
        println("| TEST RESULTS: (${results.totalTests} tests)".padRight(90) + "|")
        println(SEPARATOR)

        results.testCases.each { testCase ->
            println("| ${testCase.name.padRight(70)} ${testCase.lastExecutedStep?.result?.status}".padRight(90) + "|")
        }

        println(SEPARATOR)
        println("| RESULTS: ${results.testsPassed} passed, ${results.testsFailed} failed, ${results.testsError} error".padRight(90) + "|")
        println(SEPARATOR)
    }

    void createErrorTestCaseReport(String testName, Throwable parsedException) {
        println('\ncreateErrorTestCaseReport: ' + testName + ", " + parsedException)
    }
}
