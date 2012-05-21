package au.com.ps4impact.madcow.util

import org.apache.commons.lang3.StringUtils;

/**
 * Collection of formatting utilities.
 *
 * @author: Gavin Bunney
 */
class FormatUtil {

    /**
     * Converts a Map to a String.
     */
    static String convertMapToString(Map mapToConvert) {
        String line = '';
        mapToConvert.each { key, value ->

            String valueString
            if ((value instanceof String) || (value instanceof GString)) {
                valueString = "'$value'";
            } else if (value instanceof List) {
                valueString = FormatUtil.convertListToString(value as List)
            } else {
                valueString = FormatUtil.convertMapToString(value as Map)
            }

            line += "'$key' : ${valueString}, "
        }

        if (line != '') {
            line = StringUtils.substringBeforeLast(line, ', ');
        }

        return "[$line]"
    }

    /**
     * Converts a List to a String.
     */
    static String convertListToString(List listToConvert) {
        String quotedList = '';
        listToConvert.each { String val -> quotedList += "'${val}', " }

        if (quotedList != '') {
            quotedList = StringUtils.substringBeforeLast(quotedList, ', ');
        }
        return "[$quotedList]"
    }
}
