package au.com.ps4impact.madcow.grass

import au.com.ps4impact.madcow.MadcowTestCase;

/**
 * Test class for GrassBlade.
 */
public class GrassBladeTest extends GroovyTestCase {

    protected MadcowTestCase madcowTestCase = new MadcowTestCase([]);
    protected GrassParser grassParser = madcowTestCase.grassParser;

    private void checkBladeProperties(GrassBlade blade, def line, def operation, def selector, def parameters, def mapping) {
        assertEquals("Check operation", operation, blade.operation);
        assertEquals("Check selector", selector, blade.selector);
        assertEquals("Check parameters", parameters, blade.parameters);
        assertEquals("Check mapping", mapping, blade.mapping);
        assertEquals("Check line", line, blade.line);
    }

    public void testEquationProperties() {
        GrassBlade equationBlade = new GrassBlade('addressbook_search_country.verifyText = Australia', grassParser);
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = Australia', 'verifyText', null, 'Australia', 'addressbook_search_country');
    }

    public void testEquationWithListParameterProperties() {
        GrassBlade equationListParam1Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ['One', 'Two']""", grassParser);
        checkBladeProperties(equationListParam1Blade, """addressbook_search_country.verifySelectFieldOptions = ['One', 'Two']""", 'verifySelectFieldOptions', null, ['One', 'Two'], 'addressbook_search_country');

        GrassBlade equationListParam2Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ["One", "Two"]""", grassParser);
        checkBladeProperties(equationListParam2Blade, """addressbook_search_country.verifySelectFieldOptions = ["One", "Two"]""", 'verifySelectFieldOptions', null, ['One', 'Two'], 'addressbook_search_country');

        GrassBlade equationListParam3Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ['One\\'s', "Two"]""", grassParser);
        checkBladeProperties(equationListParam3Blade, """addressbook_search_country.verifySelectFieldOptions = ['One\\'s', "Two"]""", 'verifySelectFieldOptions', null, ['One\'s', 'Two'], 'addressbook_search_country');

        GrassBlade equationListParam4Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ["One's", "Two"]""", grassParser);
        checkBladeProperties(equationListParam4Blade, """addressbook_search_country.verifySelectFieldOptions = ["One's", "Two"]""", 'verifySelectFieldOptions', null, ['One\'s', 'Two'], 'addressbook_search_country');
    }

    public void testEquationWithMapParameterProperties() {
        GrassBlade equationMapParam1Blade = new GrassBlade("""verifyElement = [text : 'New Zealand']""", grassParser);
        checkBladeProperties(equationMapParam1Blade, """verifyElement = [text : 'New Zealand']""", 'verifyElement', null, ['text': 'New Zealand'], null);

        GrassBlade equationMapParam2Blade = new GrassBlade("""verifyElement = ["text" : "New Zealand"]""", grassParser);
        checkBladeProperties(equationMapParam2Blade, """verifyElement = ["text" : "New Zealand"]""", 'verifyElement', null, ['text': 'New Zealand'], null);

        GrassBlade equationMapParam3Blade = new GrassBlade("""verifyElement = ["text" : "New Zealand's Islands"]""", grassParser);
        checkBladeProperties(equationMapParam3Blade, """verifyElement = ["text" : "New Zealand's Islands"]""", 'verifyElement', null, ['text': 'New Zealand\'s Islands'], null);

        GrassBlade equationMapParam4Blade = new GrassBlade("""verifyElement = [text : 'New Zealand\\'s Islands']""", grassParser);
        checkBladeProperties(equationMapParam4Blade, """verifyElement = [text : 'New Zealand\\'s Islands']""", 'verifyElement', null, ['text': 'New Zealand\'s Islands'], null);
    }

    public void testEquationWithListAndMapParameterProperties() {
        GrassBlade equationListMapParam1Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = [options : ['Australia', 'New Zealand']]""", grassParser);
        checkBladeProperties(equationListMapParam1Blade, """addressbook_search_country.verifySelectFieldOptions = [options : ['Australia', 'New Zealand']]""", 'verifySelectFieldOptions', null, [options: ['Australia', 'New Zealand']], 'addressbook_search_country');

        GrassBlade equationListMapParam2Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = [[country : 'Australia'], [country: 'New Zealand']]""", grassParser);
        checkBladeProperties(equationListMapParam2Blade, """addressbook_search_country.verifySelectFieldOptions = [[country : 'Australia'], [country: 'New Zealand']]""", 'verifySelectFieldOptions', null, [[country : 'Australia'], [country: 'New Zealand']], 'addressbook_search_country');
    }

    public void testStatementProperties() {
        GrassBlade statementBlade = new GrassBlade('addressbook_search_button.clickLink', grassParser);
        checkBladeProperties(statementBlade, 'addressbook_search_button.clickLink', 'clickLink', null, null, 'addressbook_search_button');
    }

    public void testEquationNoMappingProperties() {
        GrassBlade equationNoMappingBlade = new GrassBlade('verifyText = Australia', grassParser);
        checkBladeProperties(equationNoMappingBlade, 'verifyText = Australia', 'verifyText', null, 'Australia', null);
    }

    public void testToString() {
        assertToString(new GrassBlade('verifyText = Australia', grassParser), "operation: {verifyText} | selector: {null} | params: {Australia} | line: {verifyText = Australia} | mapping: {null}");
        assertToString(new GrassBlade('addressbook_search_country.verifyText = Australia', grassParser), "operation: {verifyText} | selector: {null} | params: {Australia} | line: {addressbook_search_country.verifyText = Australia} | mapping: {addressbook_search_country}");
        assertToString(new GrassBlade('addressbook_search_button.clickLink', grassParser), "operation: {clickLink} | selector: {null} | line: {addressbook_search_button.clickLink} | mapping: {addressbook_search_button}");
    }

    public void testTrimLine() {
        assertToString(new GrassBlade('verifyText =          Australia            ', grassParser), "operation: {verifyText} | selector: {null} | params: {Australia} | line: {verifyText =          Australia            } | mapping: {null}");
    }

    public void testEmptyLineBlade() {

        String emptyLineError = "No '.' or '=' found - doesn't appear to do anything.";

        try {
            def b = new GrassBlade(null, grassParser);
            fail("Should always exception" + b);
        } catch (GrassParseException e) {
            assertEquals(e.message, emptyLineError);
        } catch (e) {
            fail("Unexpected exception");
        }

        try {
            def b = new GrassBlade('', grassParser);
            fail("Should always exception " + b);
        } catch (GrassParseException e) {
            assertEquals(e.message, emptyLineError);
        } catch (e) {
            fail("Unexpected exception");
        }
    }

    public void testDataParameterBasic() {
        GrassBlade dataParameterSet = new GrassBlade('@expectedValue = Australia', grassParser);
        checkBladeProperties(dataParameterSet, '@expectedValue = Australia', '@expectedValue', null, 'Australia', null);

        GrassBlade dataParameterUsed = new GrassBlade('verifyText = @expectedValue', grassParser);
        checkBladeProperties(dataParameterUsed, 'verifyText = @expectedValue', 'verifyText', null, 'Australia', null);
    }

    public void testDataParameterInline() {
        GrassBlade dataParameterSet = new GrassBlade('@expected = tr', grassParser);
        checkBladeProperties(dataParameterSet, '@expected = tr', '@expected', null, 'tr', null);

        GrassBlade dataParameterUseInline = new GrassBlade('verifyText = Aus@{expected}alia', grassParser);
        checkBladeProperties(dataParameterUseInline, 'verifyText = Aus@{expected}alia', 'verifyText', null, 'Australia', null);
    }

    public void testDataParameterReferringOtherParams() {
        GrassBlade dataParameterSet = new GrassBlade('@expectedTr = tr', grassParser);
        checkBladeProperties(dataParameterSet, '@expectedTr = tr', '@expectedTr', null, 'tr', null);

        GrassBlade dataParameterReferOther = new GrassBlade('@expected = @expectedTr', grassParser);
        checkBladeProperties(dataParameterReferOther, '@expected = @expectedTr', '@expected', null, 'tr', null);

        GrassBlade dataParameterInline = new GrassBlade('verifyText = Aus@{expected}alia', grassParser);
        checkBladeProperties(dataParameterInline, 'verifyText = Aus@{expected}alia', 'verifyText', null, 'Australia', null);

    }

    public void testDataParameterSelfReferral() {

        GrassBlade dataParameterSet = new GrassBlade('@expectedTr = tr', grassParser);
        checkBladeProperties(dataParameterSet, '@expectedTr = tr', '@expectedTr', null, 'tr', null);

        GrassBlade dataParameterReferSelf = new GrassBlade('@expectedTr = @expectedTr', grassParser);
        checkBladeProperties(dataParameterReferSelf, '@expectedTr = @expectedTr', '@expectedTr', null, 'tr', null);

        dataParameterReferSelf = new GrassBlade('@expectedTr = Aus@{expectedTr}alia', grassParser);
        checkBladeProperties(dataParameterReferSelf, '@expectedTr = Aus@{expectedTr}alia', '@expectedTr', null, 'Australia', null);
    }

    public void testImportType() {
        GrassBlade importBlade = new GrassBlade('import = CountryTemplate', grassParser);
        assertEquals(GrassBlade.GrassBladeType.IMPORT, importBlade.type);
        checkBladeProperties(importBlade, 'import = CountryTemplate', 'import', null, 'CountryTemplate', null);

        importBlade = new GrassBlade('importTemplate = CountryTemplate', grassParser);
        assertEquals(GrassBlade.GrassBladeType.IMPORT, importBlade.type);
        checkBladeProperties(importBlade, 'importTemplate = CountryTemplate', 'importTemplate', null, 'CountryTemplate', null);
    }
}
