package au.com.ps4impact.madcow.execution

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.report.IMadcowReport

class SingleTestCaseRunnerTest extends GroovyTestCase {

    ArrayList<String> grassScript = new ArrayList<String>()

    void testTestCaseErrorTrue() {
        MadcowTestCase testCase = new MadcowTestCase("SingleTestCaseRunnerTest-testExecutionException", grassScript) {
            @Override
            void execute() {
                throw new Exception()
            }

            @Override
            protected void createStepRunner() {
            }
        }
        try {
            new SingleTestCaseRunner(testCase, new ArrayList<IMadcowReport>())
        } catch(e) {}

        assertTrue(testCase.testCaseError == true)
    }

    void testTestCaseErrorFalse() {
        MadcowTestCase testCase = new MadcowTestCase("SingleTestCaseRunnerTest-testExecutionException", grassScript) {
            @Override
            void execute() {
            }

            @Override
            protected void createStepRunner() {
            }
        }
        try {
            new SingleTestCaseRunner(testCase, new ArrayList<IMadcowReport>())
        } catch(e) {}

        assertTrue(testCase.testCaseError == false)
    }
}
