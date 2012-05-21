package au.com.ps4impact.madcow.util

/**
 * Test class for FormatUtil.
 *
 * @author Gavin Bunney
 */
class FormatUtilTest extends GroovyTestCase {

    void testList() {
        assertEquals("['One', 'Two']", FormatUtil.convertListToString(['One', 'Two']));
    }

    void testListEmpty() {
        assertEquals("[]", FormatUtil.convertListToString([]));
    }

    void testMap() {
        assertEquals("['One' : 'Two']", FormatUtil.convertMapToString(['One' : 'Two']));
    }

    void testMapEmpty() {
        assertEquals("[]", FormatUtil.convertMapToString([:]));
    }

    void testMapWithMapElement() {
        assertEquals("['One' : ['Two' : 'Three']]", FormatUtil.convertMapToString(['One' : ['Two' : 'Three']]));
    }

    void testMapWithListElement() {
        assertEquals("['One' : ['Two', 'Three']]", FormatUtil.convertMapToString(['One' : ['Two', 'Three']]));
    }

}
