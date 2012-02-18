package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Madcow Test Coordinator class.
 */
class MadcowTestRunner {

    static void executeTests(String environment = null, ArrayList<String> testNames = []) {

        MadcowConfig config = new MadcowConfig(environment);
        
        testNames.each { String testName ->
            // TODO: Load test case file... from some fancy directories
            MadcowTestCase testCase = new MadcowTestCase(testName, config, new File(testName).readLines() as ArrayList<String>);
            testCase.execute();
        }
    }

}
