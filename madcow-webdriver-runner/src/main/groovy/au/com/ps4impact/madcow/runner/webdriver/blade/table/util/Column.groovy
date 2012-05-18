package au.com.ps4impact.madcow.runner.webdriver.blade.table.util

/**
 * 
 *
 * @author: Gavin Bunney
 */
class Column {

    private String tableXPath

    private def columnHeader

    public Column(String tableXPath, def columnHeader) {
        this.tableXPath = tableXPath
        this.columnHeader = columnHeader
    }

    public String getColumnPositionXPath() {
        if (columnHeader == "firstColumn")
            return "1"
        else if (columnHeader == "lastColumn")
            return "count(${tableXPath}/thead/tr/th[position() = last()]/preceding-sibling::*)+1"
        else if (columnHeader.toString().toLowerCase() ==~ /column\d*/)
            return columnHeader.toString().substring(6)
        else
            return "count(${tableXPath}/thead/tr/th[normalize-space(.//text()) = '${columnHeader}' or normalize-space(.//@value) = '${columnHeader}']/preceding-sibling::*)+1"
    }
}
