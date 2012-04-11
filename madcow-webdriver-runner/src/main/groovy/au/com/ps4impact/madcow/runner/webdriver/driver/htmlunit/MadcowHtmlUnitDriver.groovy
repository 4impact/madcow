package au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.BrowserVersion
import org.openqa.selenium.WebElement
import com.gargoylesoftware.htmlunit.html.HtmlElement

/**
 * Madcow specific HTML Unit Driver.
 *
 * @author: Gavin Bunney
 */
class MadcowHtmlUnitDriver extends HtmlUnitDriver {

    public MadcowHtmlUnitDriver() {
        super(BrowserVersion.FIREFOX_3_6); // TODO: parameterise the browser in madcow-config.xml
        this.setJavascriptEnabled(true);
    }

    protected WebClient modifyWebClient(WebClient client) {
        super.modifyWebClient(client);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        return client;
    }

    protected WebElement newHtmlUnitWebElement(HtmlElement element) {
        return new MadcowHtmlUnitWebElement(this, element);
    }
}
