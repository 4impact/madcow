package au.com.ps4impact.madcow.runner.webdriver.driver

/**
 * Enumerated type for the different web driver browsers.
 *
 * @author: Gavin Bunney
 */
public enum WebDriverType {

    HTMLUNIT(au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver),
    FIREFOX(org.openqa.selenium.firefox.FirefoxDriver),

    public Class driverClass;

    WebDriverType(Class driverClass) {
        this.driverClass = driverClass;
    }

    public static WebDriverType getDriverType(String type) {
        return WebDriverType.values().find() { WebDriverType driver -> driver.toString().toUpperCase() == type }
    }
}
