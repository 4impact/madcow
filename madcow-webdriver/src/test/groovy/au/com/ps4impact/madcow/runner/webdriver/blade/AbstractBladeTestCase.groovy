package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.util.ResourceFinder
import org.junit.AfterClass
import org.junit.Before

class AbstractBladeTestCase {

    MadcowTestCase testCase;
    String testHtmlFilePath;
    static AbstractBladeTestCase currentInstance;

    @Before
    void setUp() {
        currentInstance = this;
        if (testCase == null) {
            testCase = new MadcowTestCase('BladeTest', new MadcowConfig(), []);
        }

        if (testHtmlFilePath == null) {
            testHtmlFilePath = ResourceFinder.locateFileOnClasspath(this.class.classLoader, 'test.html', 'html')
        }
    }

    @AfterClass
    static void tearDown() {

        if (currentInstance.testCase && currentInstance.testCase.stepRunner) {
            currentInstance.testCase.stepRunner.finishTestCase()
        }

        currentInstance.testCase = null;
    }
}
