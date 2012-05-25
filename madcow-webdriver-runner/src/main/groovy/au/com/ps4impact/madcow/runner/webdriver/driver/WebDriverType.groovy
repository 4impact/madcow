package au.com.ps4impact.madcow.runner.webdriver.driver

/**
 * Enumerated type for the different web driver browsers.
 *
 * @author: Gavin Bunney
 */
public enum WebDriverType {

    HTMLUNIT('HtmlUnit', au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit.MadcowHtmlUnitDriver),
    FIREFOX('Firefox', org.openqa.selenium.firefox.FirefoxDriver),

    public String name;
    public Class driverClass;

    WebDriverType(String name, Class driverClass) {
        this.name = name;
        this.driverClass = driverClass;
    }

    public static WebDriverType getDriverType(String type) {
        return WebDriverType.values().find() { WebDriverType driver -> driver.toString().toUpperCase() == type }
    }
}
