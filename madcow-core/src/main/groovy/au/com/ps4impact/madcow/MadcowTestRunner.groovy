package au.com.ps4impact.madcow

import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder

/**
 * Madcow Test Coordinator class.
 */
class MadcowTestRunner {

    static void executeTests(ArrayList<String> testNames = [], MadcowConfig madcowConfig) {

        testNames.each { String testName ->
            
            def testScript = ResourceFinder.locateFileOnClasspath(this.classLoader, "**/${testName}.grass", MadcowProject.TESTS_DIRECTORY);
            MadcowTestCase testCase = new MadcowTestCase(testName, madcowConfig, testScript.readLines() as ArrayList<String>);
            testCase.execute();
        }
    }

}
