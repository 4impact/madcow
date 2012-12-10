package au.com.ps4impact.madcow.runner.webdriver.driver.remote

import org.openqa.selenium.Capabilities
import org.openqa.selenium.remote.RemoteWebDriver

/**
 * Madcow Remote WebDriver
 *
 * @author Tom Romano
 */
class MadcowRemoteWebDriver extends RemoteWebDriver {

    public MadcowRemoteWebDriver(HashMap driverParameters){
        super(new URL(driverParameters.url as String),
              driverParameters.desiredCapabilities as Capabilities);
    }


}
