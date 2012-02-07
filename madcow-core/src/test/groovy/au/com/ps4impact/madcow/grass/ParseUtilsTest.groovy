package au.com.ps4impact.madcow.grass;

/**
 * Test class for ParseUtils.
 */
public class ParseUtilsTest extends GroovyTestCase {

    public void testUnquote() {
        assertEquals('Queensland', ParseUtils.unquote("""'Queensland'"""));
        assertEquals('Queensland', ParseUtils.unquote("""\"Queensland\""""));
        assertEquals('Queensland', ParseUtils.unquote('\'Queensland\''));

        assertEquals('Queensland', ParseUtils.unquote('"""Queensland"""'));
        assertEquals('Queensland', ParseUtils.unquote('"Queensland"'));

        assertEquals('Queensland', ParseUtils.unquote("Queensland"));
    }

    public void testUnquoteNotBalanced() {
        assertEquals('\'Queensland"', ParseUtils.unquote("'Queensland\""));
        assertEquals('"Queensland\'', ParseUtils.unquote("\"Queensland'"));
        assertEquals('"""Queensland\'', ParseUtils.unquote("\"\"\"Queensland'"));
    }

    public void testEvalMeStrings() {
        assertEquals('Queensland', ParseUtils.evalMe('Queensland'));
        assertEquals('Queensland', ParseUtils.evalMe("Queensland"));
        assertEquals('Queensland', ParseUtils.evalMe("""Queensland"""));
        assertEquals('Queen\'sland', ParseUtils.evalMe("Queen'sland"));

        assertEquals('"Queensland\'', ParseUtils.evalMe("\"Queensland'"));
        assertEquals('Queensland', ParseUtils.evalMe("'Queensland'"));
    }

    public void testEvalMeLists() {
        assertEquals(['Queensland', 'Victoria'], ParseUtils.evalMe("['Queensland', 'Victoria']"));
        assertEquals(['Queen\'sland', 'Victoria'], ParseUtils.evalMe("['Queen\\'sland', 'Victoria']"));
    }

    public void testEvalMeMaps() {
        assertEquals([state: 'Queensland'], ParseUtils.evalMe("[state: 'Queensland']"));
        assertEquals([state: 'Queensland', city: 'Brisbane'], ParseUtils.evalMe("[state: 'Queensland', city: 'Brisbane']"));
    }

    public void testEvalMeListOfMap() {
        assertEquals([[state: 'Queensland'], [state: 'Victoria']], ParseUtils.evalMe("[[state: 'Queensland'], [state: 'Victoria']]"));
    }

    public void testEvalMeMapOfLists() {
        assertEquals([states: ['Queensland', 'Victoria']], ParseUtils.evalMe("[states: ['Queensland', 'Victoria']]"));
    }

    /**
     * Test to prove that not all valid groovy code will be evaluated - all treated as strings.
     */
    public void testEvalMeGroovyCode() {
        assertEquals('{ new String(\'tent\') }', ParseUtils.evalMe("{ new String('tent') }"));
        assertEquals('2 + 2', ParseUtils.evalMe("2 + 2"));
        assertEquals('2\' + \'2', ParseUtils.evalMe("'2' + '2'"));
    }
}
