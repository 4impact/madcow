package au.com.ps4impact.madcow.util

import org.apache.commons.lang3.StringUtils

/**
 * Collection of utilities to help format paths in a variety of ways.
 *
 * @author Gavin Bunney
 */
class PathFormatter {

    /**
     * Format a path to a package name.
     * e.g. test/groovy/some/test/MyTest.grass = test.groovy.some.test.MyTest.grass
     */
    public static String formatPathToPackage(String path, String baseDirectory, String delimeter = '.') {
        
        String packageName = '';
        
        def relativeUrlSplit = StringUtils.removeStart(path, baseDirectory).split('/');
        relativeUrlSplit.each { String element ->
            packageName += StringUtils.isBlank(packageName) ? element : "$delimeter$element";
        }

        return packageName;
    }
}