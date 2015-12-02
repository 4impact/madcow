package au.com.ps4impact.madcow.report

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.MadcowTestSuite
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * Madcow Execution Report for the console.
 *
 * @author Paul Bevis
 */
class ConsoleReport implements IMadcowReport {

    void prepareReportDirectory() {
    }

    void createTestCaseReport(MadcowTestCase testCase) {
    }

    void createTestSuiteReport(MadcowTestSuite testSuite) {
        def tests = testSuite.size()

        def SEPERATOR = '-------------------------------------------------------------------------------------------'
        println('\n\n' + SEPERATOR)
        println("| TEST RESULTS: (${tests} tests)".padRight(90) + "|")
        println(SEPERATOR)
        def passed = 0
        def failed = 0
        def error = 0
        testSuite.getTestCasesRecusively().each {
            switch (it.lastExecutedStep?.result?.status) {
                case (MadcowStepResult.StatusType.PASS):
                    passed++
                    break
                case (MadcowStepResult.StatusType.FAIL):
                    failed++
                    break
                case (MadcowStepResult.StatusType.PARSE_ERROR):
                    error++
                    break
            }
            println("| ${it.name.padRight(70)} ${it.lastExecutedStep?.result?.status}".padRight(90) + "|")
        }
        println(SEPERATOR)
        println("| RESULTS: ${passed} passed, ${failed} failed, ${error} error".padRight(90) + "|")
        println(SEPERATOR)
    }

    void createErrorTestCaseReport(String testName, Throwable parsedException) {
        println('\ncreateErrorTestCaseReport: ' + testName + ", " + parsedException)

    }
}
