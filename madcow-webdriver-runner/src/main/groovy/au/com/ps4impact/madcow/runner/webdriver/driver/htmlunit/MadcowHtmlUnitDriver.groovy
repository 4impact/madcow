package au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController

/**
 * Madcow specific HTML Unit Driver.
 *
 * @author: Gavin Bunney
 */
class MadcowHtmlUnitDriver extends HtmlUnitDriver {

    protected WebClient modifyWebClient(WebClient client) {
        super.modifyWebClient(client);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        return client;
    }
}
