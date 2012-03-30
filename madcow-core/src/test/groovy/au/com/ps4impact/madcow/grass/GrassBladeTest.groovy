package au.com.ps4impact.madcow.grass

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig;

/**
 * Test class for GrassBlade.
 */
public class GrassBladeTest extends GroovyTestCase {

    protected MadcowTestCase madcowTestCase = new MadcowTestCase('GrassBladeTest', MockMadcowConfig.getMadcowConfig(), []);

    private void checkBladeProperties(GrassBlade blade, def line, def operation, Map<String, String> selector, def parameters, def mapping) {
        assertEquals("Check operation", operation, blade.operation);
        if (selector != null) {
            assertArrayEquals(selector?.keySet()?.toArray(), blade.mappingSelectorType);
            assertArrayEquals(selector?.values()?.toArray(), blade.mappingSelectorValue);
        } else {
            assertNull(blade.mappingSelectorType);
            assertNull(blade.mappingSelectorValue);
        }
        assertEquals("Check parameters", parameters, blade.parameters);
        assertEquals("Check mapping", mapping, blade.mapping);
        assertEquals("Check line", line, blade.line);
    }

    public void testEquationProperties() {
        GrassBlade equationBlade = new GrassBlade('addressbook_search_country.verifyText = Australia', madcowTestCase.parseScript());
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = Australia', 'verifyText', ['htmlid' : 'addressbook_search_country'], 'Australia', 'addressbook_search_country');
    }

    public void testEquationWithListParameterProperties() {
        GrassBlade equationListParam1Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ['One', 'Two']""", madcowTestCase.parseScript());
        checkBladeProperties(equationListParam1Blade, """addressbook_search_country.verifySelectFieldOptions = ['One', 'Two']""", 'verifySelectFieldOptions', ['htmlid' : 'addressbook_search_country'], ['One', 'Two'], 'addressbook_search_country');

        GrassBlade equationListParam2Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ["One", "Two"]""", madcowTestCase.parseScript());
        checkBladeProperties(equationListParam2Blade, """addressbook_search_country.verifySelectFieldOptions = ["One", "Two"]""", 'verifySelectFieldOptions', ['htmlid' : 'addressbook_search_country'], ['One', 'Two'], 'addressbook_search_country');

        GrassBlade equationListParam3Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ['One\\'s', "Two"]""", madcowTestCase.parseScript());
        checkBladeProperties(equationListParam3Blade, """addressbook_search_country.verifySelectFieldOptions = ['One\\'s', "Two"]""", 'verifySelectFieldOptions', ['htmlid' : 'addressbook_search_country'], ['One\'s', 'Two'], 'addressbook_search_country');

        GrassBlade equationListParam4Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ["One's", "Two"]""", madcowTestCase.parseScript());
        checkBladeProperties(equationListParam4Blade, """addressbook_search_country.verifySelectFieldOptions = ["One's", "Two"]""", 'verifySelectFieldOptions', ['htmlid' : 'addressbook_search_country'], ['One\'s', 'Two'], 'addressbook_search_country');
    }

    public void testEquationWithMapParameterProperties() {
        GrassBlade equationMapParam1Blade = new GrassBlade("""verifyElement = [text : 'New Zealand']""", madcowTestCase.parseScript());
        checkBladeProperties(equationMapParam1Blade, """verifyElement = [text : 'New Zealand']""", 'verifyElement', null, ['text': 'New Zealand'], null);

        GrassBlade equationMapParam2Blade = new GrassBlade("""verifyElement = ["text" : "New Zealand"]""", madcowTestCase.parseScript());
        checkBladeProperties(equationMapParam2Blade, """verifyElement = ["text" : "New Zealand"]""", 'verifyElement', null, ['text': 'New Zealand'], null);

        GrassBlade equationMapParam3Blade = new GrassBlade("""verifyElement = ["text" : "New Zealand's Islands"]""", madcowTestCase.parseScript());
        checkBladeProperties(equationMapParam3Blade, """verifyElement = ["text" : "New Zealand's Islands"]""", 'verifyElement', null, ['text': 'New Zealand\'s Islands'], null);

        GrassBlade equationMapParam4Blade = new GrassBlade("""verifyElement = [text : 'New Zealand\\'s Islands']""", madcowTestCase.parseScript());
        checkBladeProperties(equationMapParam4Blade, """verifyElement = [text : 'New Zealand\\'s Islands']""", 'verifyElement', null, ['text': 'New Zealand\'s Islands'], null);
    }

    public void testEquationWithListAndMapParameterProperties() {
        GrassBlade equationListMapParam1Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = [options : ['Australia', 'New Zealand']]""", madcowTestCase.parseScript());
        checkBladeProperties(equationListMapParam1Blade, """addressbook_search_country.verifySelectFieldOptions = [options : ['Australia', 'New Zealand']]""", 'verifySelectFieldOptions', ['htmlid' : 'addressbook_search_country'], [options: ['Australia', 'New Zealand']], 'addressbook_search_country');

        GrassBlade equationListMapParam2Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = [[country : 'Australia'], [country: 'New Zealand']]""", madcowTestCase.parseScript());
        checkBladeProperties(equationListMapParam2Blade, """addressbook_search_country.verifySelectFieldOptions = [[country : 'Australia'], [country: 'New Zealand']]""", 'verifySelectFieldOptions', ['htmlid' : 'addressbook_search_country'], [[country : 'Australia'], [country: 'New Zealand']], 'addressbook_search_country');
    }

    public void testEquationDollarInProperties() {
        GrassBlade equationDollarInBlade = new GrassBlade('addressbook_recoveries_cost.verifyText = $2,000.00', madcowTestCase.parseScript());
        checkBladeProperties(equationDollarInBlade, 'addressbook_recoveries_cost.verifyText = $2,000.00', 'verifyText', [htmlid : 'addressbook_recoveries_cost'], '$2,000.00', 'addressbook_recoveries_cost');

        GrassBlade equationDollarInBlade2 = new GrassBlade('addressbook_recoveries_cost.verifyText = ${someValue}', madcowTestCase.parseScript());
        checkBladeProperties(equationDollarInBlade2, 'addressbook_recoveries_cost.verifyText = ${someValue}', 'verifyText', [htmlid : 'addressbook_recoveries_cost'], '${someValue}', 'addressbook_recoveries_cost');

        GrassBlade equationDollarInBlade3 = new GrassBlade("""addressbook_recoveries_cost.verifyText = \${someAmount\$20}""", madcowTestCase.parseScript());
        checkBladeProperties(equationDollarInBlade3, 'addressbook_recoveries_cost.verifyText = \${someAmount\$20}', 'verifyText', [htmlid : 'addressbook_recoveries_cost'], "\${someAmount\$20}", 'addressbook_recoveries_cost');
    }

    public void testEquationWithListAndDollarParameterProperties() {
        GrassBlade equationListDollarParam1Blade = new GrassBlade("""addressbook_search_costs.verifySelectFieldOptions = [options : ["\$10.00", "\$20.00"]]""", madcowTestCase.parseScript());
        checkBladeProperties(equationListDollarParam1Blade, """addressbook_search_costs.verifySelectFieldOptions = [options : ["\$10.00", "\$20.00"]]""", 'verifySelectFieldOptions', [htmlid : 'addressbook_search_costs'], [options: ["\$10.00", "\$20.00"]], 'addressbook_search_costs');

        GrassBlade equationListDollarParam2Blade = new GrassBlade("""addressbook_search_costs.verifySelectFieldOptions = [options : ['\$10.00', "\$20.00"]]""", madcowTestCase.parseScript());
        checkBladeProperties(equationListDollarParam2Blade, """addressbook_search_costs.verifySelectFieldOptions = [options : ['\$10.00', "\$20.00"]]""", 'verifySelectFieldOptions', [htmlid : 'addressbook_search_costs'], [options: ['\$10.00', "\$20.00"]], 'addressbook_search_costs');

        GrassBlade equationListDollarParam3Blade = new GrassBlade("""addressbook_search_costs.verifySelectFieldOptions = [options : ["\$20,000.00", "\$20.00"]]""", madcowTestCase.parseScript());
        checkBladeProperties(equationListDollarParam3Blade, """addressbook_search_costs.verifySelectFieldOptions = [options : ["\$20,000.00", "\$20.00"]]""", 'verifySelectFieldOptions', [htmlid : 'addressbook_search_costs'], [options: ["\$20,000.00", "\$20.00"]], 'addressbook_search_costs');

        GrassBlade equationListDollarParam4Blade = new GrassBlade('addressbook_search_costs.verifySelectFieldOptions = [options : ["\$10.00", "\$20.00"]]', madcowTestCase.parseScript());
        checkBladeProperties(equationListDollarParam4Blade, 'addressbook_search_costs.verifySelectFieldOptions = [options : ["\$10.00", "\$20.00"]]', 'verifySelectFieldOptions', [htmlid : 'addressbook_search_costs'], [options: ['$10.00', '$20.00']], 'addressbook_search_costs');
    }

    public void testStatementProperties() {
        GrassBlade statementBlade = new GrassBlade('addressbook_search_button.clickLink', madcowTestCase.parseScript());
        checkBladeProperties(statementBlade, 'addressbook_search_button.clickLink', 'clickLink', ['htmlid' : 'addressbook_search_button'], null, 'addressbook_search_button');
    }

    public void testEquationNoMappingProperties() {
        GrassBlade equationNoMappingBlade = new GrassBlade('verifyText = Australia', madcowTestCase.parseScript());
        checkBladeProperties(equationNoMappingBlade, 'verifyText = Australia', 'verifyText', null, 'Australia', null);
    }

    public void testToString() {
        assertToString(new GrassBlade('verifyText = Australia', madcowTestCase.parseScript()), "operation: {verifyText} | selector: {null:null} | params: {Australia} | line: {verifyText = Australia} | mapping: {null}");
        assertToString(new GrassBlade('addressbook_search_country.verifyText = Australia', madcowTestCase.parseScript()), "operation: {verifyText} | selector: {htmlid:addressbook_search_country} | params: {Australia} | line: {addressbook_search_country.verifyText = Australia} | mapping: {addressbook_search_country}");
        assertToString(new GrassBlade('addressbook_search_button.clickLink', madcowTestCase.parseScript()), "operation: {clickLink} | selector: {htmlid:addressbook_search_button} | line: {addressbook_search_button.clickLink} | mapping: {addressbook_search_button}");
    }

    public void testTrimLine() {
        assertToString(new GrassBlade('verifyText =          Australia            ', madcowTestCase.parseScript()), "operation: {verifyText} | selector: {null:null} | params: {Australia} | line: {verifyText =          Australia            } | mapping: {null}");
    }

    public void testEmptyLineBlade() {

        String emptyLineError = "No '.' or '=' found - doesn't appear to do anything.";

        try {
            def b = new GrassBlade(null, madcowTestCase.parseScript());
            fail("Should always exception" + b);
        } catch (GrassParseException e) {
            assertEquals(e.message, emptyLineError);
        } catch (e) {
            fail("Unexpected exception");
        }

        try {
            def b = new GrassBlade('', madcowTestCase.parseScript());
            fail("Should always exception " + b);
        } catch (GrassParseException e) {
            assertEquals(e.message, emptyLineError);
        } catch (e) {
            fail("Unexpected exception");
        }
    }

    public void testDataParameterBasic() {
        GrassBlade dataParameterSet = new GrassBlade('@expectedValue = Australia', madcowTestCase.parseScript());
        checkBladeProperties(dataParameterSet, '@expectedValue = Australia', '@expectedValue', null, 'Australia', null);

        GrassBlade dataParameterUsed = new GrassBlade('verifyText = @expectedValue', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterUsed, 'verifyText = @expectedValue', 'verifyText', null, 'Australia', null);
    }

    public void testDataParameterInline() {
        GrassBlade dataParameterSet = new GrassBlade('@expected = tr', madcowTestCase.parseScript());
        checkBladeProperties(dataParameterSet, '@expected = tr', '@expected', null, 'tr', null);

        GrassBlade dataParameterUseInline = new GrassBlade('verifyText = Aus@{expected}alia', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterUseInline, 'verifyText = Aus@{expected}alia', 'verifyText', null, 'Australia', null);
    }

    public void testDataParameterReferringOtherParams() {
        GrassBlade dataParameterSet = new GrassBlade('@expectedTr = tr', madcowTestCase.parseScript());
        checkBladeProperties(dataParameterSet, '@expectedTr = tr', '@expectedTr', null, 'tr', null);

        GrassBlade dataParameterReferOther = new GrassBlade('@expected = @expectedTr', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterReferOther, '@expected = @expectedTr', '@expected', null, 'tr', null);

        GrassBlade dataParameterInline = new GrassBlade('verifyText = Aus@{expected}alia', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterInline, 'verifyText = Aus@{expected}alia', 'verifyText', null, 'Australia', null);

    }

    public void testDataParameterSelfReferral() {

        GrassBlade dataParameterSet = new GrassBlade('@expectedTr = tr', madcowTestCase.parseScript());
        checkBladeProperties(dataParameterSet, '@expectedTr = tr', '@expectedTr', null, 'tr', null);

        GrassBlade dataParameterReferSelf = new GrassBlade('@expectedTr = @expectedTr', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterReferSelf, '@expectedTr = @expectedTr', '@expectedTr', null, 'tr', null);

        dataParameterReferSelf = new GrassBlade('@expectedTr = Aus@{expectedTr}alia', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterReferSelf, '@expectedTr = Aus@{expectedTr}alia', '@expectedTr', null, 'Australia', null);
    }

    public void testImportType() {
        GrassBlade importBlade = new GrassBlade('import = CountryTemplate', madcowTestCase.parseScript());
        assertEquals(GrassBlade.GrassBladeType.IMPORT, importBlade.type);
        checkBladeProperties(importBlade, 'import = CountryTemplate', 'import', null, 'CountryTemplate', null);

        importBlade = new GrassBlade('importTemplate = CountryTemplate', madcowTestCase.grassParser);
        assertEquals(GrassBlade.GrassBladeType.IMPORT, importBlade.type);
        checkBladeProperties(importBlade, 'importTemplate = CountryTemplate', 'importTemplate', null, 'CountryTemplate', null);
    }

    public void testEvalMacro() {
        GrassBlade equationBlade = new GrassBlade('addressbook_search_country.verifyText = madcow.eval({return \'Australia\'})', madcowTestCase.parseScript());
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = madcow.eval({return \'Australia\'})', 'verifyText', [htmlid : 'addressbook_search_country'], 'Australia', 'addressbook_search_country');

        equationBlade = new GrassBlade('addressbook_search_country.verifyText = Winner is madcow.eval({return \'Australia\'})', madcowTestCase.grassParser);
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = Winner is madcow.eval({return \'Australia\'})', 'verifyText', [htmlid : 'addressbook_search_country'], 'Winner is Australia', 'addressbook_search_country');

        equationBlade = new GrassBlade('addressbook_search_country.verifyText = The year is madcow.eval({ new Date().format(\'yyyy\')}) - awesome!', madcowTestCase.grassParser);
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = The year is madcow.eval({ new Date().format(\'yyyy\')}) - awesome!', 'verifyText', [htmlid : 'addressbook_search_country'], 'The year is 2012 - awesome!', 'addressbook_search_country');

        equationBlade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = [[country : 'madcow.eval({return 'Australia'})'], [country: 'New Zealand']]""", madcowTestCase.grassParser);
        checkBladeProperties(equationBlade, """addressbook_search_country.verifySelectFieldOptions = [[country : 'madcow.eval({return 'Australia'})'], [country: 'New Zealand']]""", 'verifySelectFieldOptions', [htmlid : 'addressbook_search_country'], [[country : 'Australia'], [country: 'New Zealand']], 'addressbook_search_country');
    }

    public void testEvalInvalidMacro() {
        try {
            GrassBlade macroBlade = new GrassBlade('addressbook_search_country.verifyText = madcow.eval({return RARR!})', madcowTestCase.parseScript());
            fail('should always exception');
        } catch (e) {
            assertEquals(true, e.message.startsWith('Unable to evaluate'));
            assertEquals(true, e.message.contains('is it a valid groovy command?'));
        }
    }

    public void testIsExecutable() {
        GrassBlade statementBlade = new GrassBlade('addressbook_search_country.clickLink', madcowTestCase.parseScript());
        assertEquals(true, statementBlade.executable());
        
        GrassBlade equationBlade = new GrassBlade('addressbook_search_country.verifyText = Winner is Australia', madcowTestCase.grassParser);
        assertEquals(true, equationBlade.executable());

        GrassBlade importBlade = new GrassBlade('import = CountryTemplate', madcowTestCase.grassParser);
        assertEquals(false, importBlade.executable());

        GrassBlade dataBlade = new GrassBlade('@country = Australia', madcowTestCase.grassParser);
        assertEquals(false, dataBlade.executable());
    }
}
