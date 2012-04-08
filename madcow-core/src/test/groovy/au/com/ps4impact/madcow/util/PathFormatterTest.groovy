package au.com.ps4impact.madcow.util

/**
 * Test class for PathFormatter.
 */
class PathFormatterTest extends GroovyTestCase {

    void testPathFormatDefaultDelimiter() {
        def formatted = PathFormatter.formatPathToPackage('test/groovy/some/test/MyTest.grass', 'test/groovy');
        assertEquals('some.test.MyTest.grass', formatted);
    }

    void testPathFormatCustomDelimiter() {
        def formatted = PathFormatter.formatPathToPackage('test/groovy/some/test/MyTest.grass', 'test/groovy', '_');
        assertEquals('some_test_MyTest.grass', formatted);
    }

    void testPathFormatEdges() {
        def formatted = PathFormatter.formatPathToPackage('', 'test/groovy');
        assertEquals('', formatted);

        formatted = PathFormatter.formatPathToPackage('', '');
        assertEquals('', formatted);

        formatted = PathFormatter.formatPathToPackage('test/groovy/some/test/MyTest.grass', 'not/a/basedir', '_');
        assertEquals('test_groovy_some_test_MyTest.grass', formatted);
    }
}
