package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import org.junit.AfterClass
import org.junit.Before

class AbstractBladeTestCase {

    MadcowTestCase testCase;
    String testHtmlFilePath;
    String lastTestCase = null;

    @Before
    void setUp_test() {
//        if (this.class.name != lastTestCase) {
//            lastTestCase = this.class.name;
//
//            if (testCase && testCase.stepRunner) {
//                testCase.stepRunner.finishTestCase()
//            }
//
//            testCase = null;
//        }
//
//        if (testCase == null) {
            testCase = new MadcowTestCase('BladeTest', new MadcowConfig(), []);
//        }
//
//        if (testHtmlFilePath == null) {
            testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html')
//        }
    }

//    @AfterClass
//    static void tearDown() {
//        if (testCase && testCase.stepRunner) {
//            testCase.stepRunner.finishTestCase()
//        }
//    }
}
