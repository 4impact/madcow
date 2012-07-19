package au.com.ps4impact.madcow.runner.webdriver.blade.table.util

import au.com.ps4impact.madcow.grass.ParseUtils
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner

/**
 * 
 *
 * @author: Gavin Bunney
 */
public class TableXPather {

    private GrassBlade blade;

    public TableXPather(GrassBlade blade) {
        this.blade = blade;
    }

    /**
     * Get the key used in the runtime storage container when storing the xpath.
     */
    public String getRuntimeStorageKey() {
        return "webdriver.blade.table.${this.blade.mappingSelectorValue}";
    }

    public String getColumnPositionXPath(def columnHeaderText) {
        return new Column(prefixXPath, columnHeaderText).getColumnPositionXPath()
    }

    /**
     * Returns an xpath expression to get the row number within the table, with the specific cellText.
     * Parameter must be a map of [columnHeaderText : cellText]
     */
    public String getRowPositionXPath(Map columnHeaderTextCellTextMap) {
        String rowXPath = "count(${getPrefixXPath()}/tbody/tr"

        columnHeaderTextCellTextMap.each { String columnText, String cellText ->
            String formattedCellText = ParseUtils.escapeSingleQuotes(cellText)
            rowXPath += "/td[position() = (${getColumnPositionXPath(columnText)}) and (normalize-space(.//text()) = ${formattedCellText} or normalize-space(.//@value) = ${formattedCellText})]/parent::*"
        }
        rowXPath += "/preceding-sibling::*)+1"

        return rowXPath
    }

    public String getRowXPath(def rowPositionXPath) {
        return "${getPrefixXPath()}/tbody/tr[${rowPositionXPath}]"
    }

    /**
     * Returns an xpath expression for a particular cell on a particular row
     */
    public String getCellXPath(def rowPositionXPath, def columnHeaderText) {
        def xpath = "${getRowXPath(rowPositionXPath)}/td[${getColumnPositionXPath(columnHeaderText)}]"
        return xpath
    }

    public String getCellXPath(Map rowPositionMap, def columnHeaderText) {
        return getCellXPath(getRowPositionXPath(rowPositionMap), columnHeaderText)
    }

    public String getFirstRowPositionXPath() {
        return "1"
    }

    public String getLastRowPositionXPath() {
        return "count(${getPrefixXPath()}/tbody/tr[position() = last()]/preceding-sibling::*)+1"
    }

    protected String getPrefixXPath() {

        String selectorXPath = '';
        switch (WebDriverBladeRunner.getSelectorType(this.blade.mappingSelectorType)) {

            case WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.XPATH:
                selectorXPath = this.blade.mappingSelectorValue;
                break;

            case WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.HTMLID:
                selectorXPath = "//table[@id='${this.blade.mappingSelectorValue}']"
                break;

            case WebDriverBladeRunner.BLADE_MAPPING_SELECTOR_TYPE.NAME:
                selectorXPath = "//table[@name='${this.blade.mappingSelectorValue}']"
                break;
        }

        if (selectorXPath == '')
            return null;

        // support for fieldset prior to tbody/thead tags
        return "($selectorXPath|$selectorXPath/fieldset)";
    }
}
