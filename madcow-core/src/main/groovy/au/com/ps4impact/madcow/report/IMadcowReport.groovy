package au.com.ps4impact.madcow.report

import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Abstract base class for reporting.
 */
interface IMadcowReport {

    /**
     * Prepare the report directory. Typically deletes and creates the directory afresh.
     */
    public void prepareReportDirectory();

    /**
     * Create the report for the given test case.
     */
    public void createTestCaseReport(MadcowTestCase testCase);

    /**
     * Create a suite level report.
     */
    public void createTestSuiteReport(ArrayList<MadcowTestCase> testSuite);
}