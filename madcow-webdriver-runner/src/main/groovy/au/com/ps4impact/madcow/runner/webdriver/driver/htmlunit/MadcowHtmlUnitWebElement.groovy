package au.com.ps4impact.madcow.runner.webdriver.driver.htmlunit

import org.openqa.selenium.htmlunit.HtmlUnitWebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import com.gargoylesoftware.htmlunit.html.HtmlElement

/**
 * Madcow specific version of the HtmlUnitWebElement class.
 *
 * This allows us to expose protected methods on the parent class
 * that are needed to drive correctly.
 *
 * @author: Gavin Bunney
 */
class MadcowHtmlUnitWebElement extends HtmlUnitWebElement {

    public MadcowHtmlUnitWebElement(HtmlUnitDriver parent, HtmlElement element) {
        super(parent, element);
    }

    public HtmlElement getElement() {
        super.getElement();
    }
}
