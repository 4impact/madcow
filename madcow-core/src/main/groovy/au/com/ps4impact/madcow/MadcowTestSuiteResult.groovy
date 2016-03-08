package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * Result wrapper for an entire test suite.
 */
class MadcowTestSuiteResult {

    int totalTests = 0;
    int testsPassed = 0;
    int testsFailed = 0;
    int testsError = 0;
    List<MadcowTestCase> testCases;

    MadcowTestSuiteResult(MadcowTestSuite testSuite) {
        testCases = testSuite.getTestCasesRecusively();
        totalTests = testCases.size();

        testCases.each { testCase ->
            switch (testCase.lastExecutedStep?.result?.status) {
                case (MadcowStepResult.StatusType.PASS):
                    testsPassed++
                    break
                case (MadcowStepResult.StatusType.FAIL):
                    testsFailed++
                    break
                case (MadcowStepResult.StatusType.PARSE_ERROR):
                    testsError++
                    break
            }
        }
    }
}
