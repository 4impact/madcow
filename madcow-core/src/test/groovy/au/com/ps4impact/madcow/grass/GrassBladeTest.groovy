/*
 * Copyright 2012 4impact, Brisbane, Australia
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package au.com.ps4impact.madcow.grass

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig;

/**
 * Test class for GrassBlade.
 *
 * @author Gavin Bunney
 */
class GrassBladeTest extends GroovyTestCase {

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
        GrassBlade equationBlade = new GrassBlade('addressbook_search_country.verifyText = Australia', madcowTestCase.grassParser);
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = Australia', 'verifyText', ['id' : 'addressbook_search_country'], 'Australia', 'addressbook_search_country');
    }

    public void testEquationWithListParameterProperties() {
        GrassBlade equationListParam1Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ['One', 'Two']""", madcowTestCase.grassParser);
        checkBladeProperties(equationListParam1Blade, """addressbook_search_country.verifySelectFieldOptions = ['One', 'Two']""", 'verifySelectFieldOptions', ['id' : 'addressbook_search_country'], ['One', 'Two'], 'addressbook_search_country');

        GrassBlade equationListParam2Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = ["One", "Two"]""", madcowTestCase.grassParser);
        checkBladeProperties(equationListParam2Blade, """addressbook_search_country.verifySelectFieldOptions = ["One", "Two"]""", 'verifySelectFieldOptions', ['id' : 'addressbook_search_country'], ['One', 'Two'], 'addressbook_search_country');

    }

    public void testEquationWithMapParameterProperties() {
        GrassBlade equationMapParam1Blade = new GrassBlade("""verifyElement = [text : 'New Zealand']""", madcowTestCase.grassParser);
        checkBladeProperties(equationMapParam1Blade, """verifyElement = [text : 'New Zealand']""", 'verifyElement', null, ['text': 'New Zealand'], null);

        GrassBlade equationMapParam2Blade = new GrassBlade("""verifyElement = ["text" : "New Zealand"]""", madcowTestCase.grassParser);
        checkBladeProperties(equationMapParam2Blade, """verifyElement = ["text" : "New Zealand"]""", 'verifyElement', null, ['text': 'New Zealand'], null);

        GrassBlade equationMapParam3Blade = new GrassBlade("""verifyElement = ["text" : "New Zealand's Islands"]""", madcowTestCase.grassParser);
        checkBladeProperties(equationMapParam3Blade, """verifyElement = ["text" : "New Zealand's Islands"]""", 'verifyElement', null, ['text': 'New Zealand\'s Islands'], null);

        GrassBlade equationMapParam4Blade = new GrassBlade("""verifyElement = [text : 'New Zealand\\'s Islands']""", madcowTestCase.grassParser);
        checkBladeProperties(equationMapParam4Blade, """verifyElement = [text : 'New Zealand\\'s Islands']""", 'verifyElement', null, ['text': 'New Zealand\'s Islands'], null);
    }

    public void testEquationWithListAndMapParameterProperties() {
        GrassBlade equationListMapParam1Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = [options : ['Australia', 'New Zealand']]""", madcowTestCase.grassParser);
        checkBladeProperties(equationListMapParam1Blade, """addressbook_search_country.verifySelectFieldOptions = [options : ['Australia', 'New Zealand']]""", 'verifySelectFieldOptions', ['id' : 'addressbook_search_country'], [options: ['Australia', 'New Zealand']], 'addressbook_search_country');

        GrassBlade equationListMapParam2Blade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = [[country : 'Australia'], [country: 'New Zealand']]""", madcowTestCase.grassParser);
        checkBladeProperties(equationListMapParam2Blade, """addressbook_search_country.verifySelectFieldOptions = [[country : 'Australia'], [country: 'New Zealand']]""", 'verifySelectFieldOptions', ['id' : 'addressbook_search_country'], [[country : 'Australia'], [country: 'New Zealand']], 'addressbook_search_country');
    }

    public void testEquationDollarInProperties() {
        GrassBlade equationDollarInBlade = new GrassBlade('addressbook_recoveries_cost.verifyText = $2,000.00', madcowTestCase.grassParser);
        checkBladeProperties(equationDollarInBlade, 'addressbook_recoveries_cost.verifyText = $2,000.00', 'verifyText', [id : 'addressbook_recoveries_cost'], '$2,000.00', 'addressbook_recoveries_cost');

        GrassBlade equationDollarInBlade2 = new GrassBlade('addressbook_recoveries_cost.verifyText = ${someValue}', madcowTestCase.grassParser);
        checkBladeProperties(equationDollarInBlade2, 'addressbook_recoveries_cost.verifyText = ${someValue}', 'verifyText', [id : 'addressbook_recoveries_cost'], '${someValue}', 'addressbook_recoveries_cost');

        GrassBlade equationDollarInBlade3 = new GrassBlade("""addressbook_recoveries_cost.verifyText = \${someAmount\$20}""", madcowTestCase.grassParser);
        checkBladeProperties(equationDollarInBlade3, 'addressbook_recoveries_cost.verifyText = \${someAmount\$20}', 'verifyText', [id : 'addressbook_recoveries_cost'], "\${someAmount\$20}", 'addressbook_recoveries_cost');
    }

    public void testEquationWithListAndDollarParameterProperties() {
        GrassBlade equationListDollarParam1Blade = new GrassBlade("""addressbook_search_costs.verifySelectFieldOptions = [options : ["\$10.00", "\$20.00"]]""", madcowTestCase.grassParser);
        checkBladeProperties(equationListDollarParam1Blade, """addressbook_search_costs.verifySelectFieldOptions = [options : ["\$10.00", "\$20.00"]]""", 'verifySelectFieldOptions', [id : 'addressbook_search_costs'], [options: ["\$10.00", "\$20.00"]], 'addressbook_search_costs');

        GrassBlade equationListDollarParam2Blade = new GrassBlade("""addressbook_search_costs.verifySelectFieldOptions = [options : ['\$10.00', "\$20.00"]]""", madcowTestCase.grassParser);
        checkBladeProperties(equationListDollarParam2Blade, """addressbook_search_costs.verifySelectFieldOptions = [options : ['\$10.00', "\$20.00"]]""", 'verifySelectFieldOptions', [id : 'addressbook_search_costs'], [options: ['\$10.00', "\$20.00"]], 'addressbook_search_costs');

        GrassBlade equationListDollarParam3Blade = new GrassBlade("""addressbook_search_costs.verifySelectFieldOptions = [options : ["\$20,000.00", "\$20.00"]]""", madcowTestCase.grassParser);
        checkBladeProperties(equationListDollarParam3Blade, """addressbook_search_costs.verifySelectFieldOptions = [options : ["\$20,000.00", "\$20.00"]]""", 'verifySelectFieldOptions', [id : 'addressbook_search_costs'], [options: ["\$20,000.00", "\$20.00"]], 'addressbook_search_costs');

        GrassBlade equationListDollarParam4Blade = new GrassBlade('addressbook_search_costs.verifySelectFieldOptions = [options : ["\$10.00", "\$20.00"]]', madcowTestCase.grassParser);
        checkBladeProperties(equationListDollarParam4Blade, 'addressbook_search_costs.verifySelectFieldOptions = [options : ["\$10.00", "\$20.00"]]', 'verifySelectFieldOptions', [id : 'addressbook_search_costs'], [options: ['$10.00', '$20.00']], 'addressbook_search_costs');
    }

    public void testStatementProperties() {
        GrassBlade statementBlade = new GrassBlade('addressbook_search_button.clickLink', madcowTestCase.grassParser);
        checkBladeProperties(statementBlade, 'addressbook_search_button.clickLink', 'clickLink', ['id' : 'addressbook_search_button'], null, 'addressbook_search_button');
    }

    public void testEquationNoMappingProperties() {
        GrassBlade equationNoMappingBlade = new GrassBlade('verifyText = Australia', madcowTestCase.grassParser);
        checkBladeProperties(equationNoMappingBlade, 'verifyText = Australia', 'verifyText', null, 'Australia', null);
    }

    public void testToString() {
        assertToString(new GrassBlade('verifyText = Australia', madcowTestCase.grassParser), "verifyText = Australia");
        assertToString(new GrassBlade('addressbook_search_country.verifyText = Australia', madcowTestCase.grassParser), "addressbook_search_country.verifyText = Australia");
        assertToString(new GrassBlade('addressbook_search_button.clickLink', madcowTestCase.grassParser), "addressbook_search_button.clickLink");

        assertToString(new GrassBlade('addressbook_search_button.verifyText = ["Country" : "Australia"]', madcowTestCase.grassParser), "addressbook_search_button.verifyText = ['Country' : 'Australia']");
        assertToString(new GrassBlade('addressbook_search_button.verifyText = ["New Zealand", "Australia"]', madcowTestCase.grassParser), "addressbook_search_button.verifyText = ['New Zealand', 'Australia']");

        madcowTestCase.grassParser.setDataParameter('@countryName', 'Australia');
        assertToString(new GrassBlade('verifyText = @countryName', madcowTestCase.grassParser), "verifyText = Australia");
        assertToString(new GrassBlade('verifyText = Winner is @{countryName}', madcowTestCase.grassParser), "verifyText = Winner is Australia");
    }

    public void testTrimLine() {
        assertToString(new GrassBlade('verifyText =          Australia            ', madcowTestCase.grassParser), "verifyText = Australia");
    }

    public void testEmptyLineBlade() {

        String emptyLineError = "No '.' or '=' found - doesn't appear to do anything.";

        try {
            def b = new GrassBlade(null, madcowTestCase.grassParser);
            fail("Should always exception" + b);
        } catch (GrassParseException e) {
            assertEquals(e.message, emptyLineError);
        } catch (e) {
            fail("Unexpected exception");
        }

        try {
            def b = new GrassBlade('', madcowTestCase.grassParser);
            fail("Should always exception " + b);
        } catch (GrassParseException e) {
            assertEquals(e.message, emptyLineError);
        } catch (e) {
            fail("Unexpected exception");
        }
    }

    public void testDataParameterBasic() {
        GrassBlade dataParameterSet = new GrassBlade('@expectedValue = Australia', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterSet, '@expectedValue = Australia', '@expectedValue', null, 'Australia', null);

        GrassBlade dataParameterUsed = new GrassBlade('verifyText = @expectedValue', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterUsed, 'verifyText = @expectedValue', 'verifyText', null, 'Australia', null);
    }

    public void testDataParameterInline() {
        GrassBlade dataParameterSet = new GrassBlade('@expected = tr', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterSet, '@expected = tr', '@expected', null, 'tr', null);

        GrassBlade dataParameterUseInline = new GrassBlade('verifyText = Aus@{expected}alia', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterUseInline, 'verifyText = Aus@{expected}alia', 'verifyText', null, 'Australia', null);
    }

    public void testDataParameterReferringOtherParams() {
        GrassBlade dataParameterSet = new GrassBlade('@expectedTr = tr', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterSet, '@expectedTr = tr', '@expectedTr', null, 'tr', null);

        GrassBlade dataParameterReferOther = new GrassBlade('@expected = @expectedTr', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterReferOther, '@expected = @expectedTr', '@expected', null, 'tr', null);

        GrassBlade dataParameterInline = new GrassBlade('verifyText = Aus@{expected}alia', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterInline, 'verifyText = Aus@{expected}alia', 'verifyText', null, 'Australia', null);

    }

    public void testDataParameterSelfReferral() {

        GrassBlade dataParameterSet = new GrassBlade('@expectedTr = tr', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterSet, '@expectedTr = tr', '@expectedTr', null, 'tr', null);

        GrassBlade dataParameterReferSelf = new GrassBlade('@expectedTr = @expectedTr', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterReferSelf, '@expectedTr = @expectedTr', '@expectedTr', null, 'tr', null);

        dataParameterReferSelf = new GrassBlade('@expectedTr = Aus@{expectedTr}alia', madcowTestCase.grassParser);
        checkBladeProperties(dataParameterReferSelf, '@expectedTr = Aus@{expectedTr}alia', '@expectedTr', null, 'Australia', null);
    }

    public void testImportType() {
        GrassBlade importBlade = new GrassBlade('import = CountryTemplate', madcowTestCase.grassParser);
        assertEquals(GrassBlade.GrassBladeType.IMPORT, importBlade.type);
        checkBladeProperties(importBlade, 'import = CountryTemplate', 'import', null, 'CountryTemplate', null);

        importBlade = new GrassBlade('importTemplate = CountryTemplate', madcowTestCase.grassParser);
        assertEquals(GrassBlade.GrassBladeType.IMPORT, importBlade.type);
        checkBladeProperties(importBlade, 'importTemplate = CountryTemplate', 'importTemplate', null, 'CountryTemplate', null);
    }

    public void testEvalMacro() {
        GrassBlade equationBlade = new GrassBlade('addressbook_search_country.verifyText = madcow.eval({return \'Australia\'})', madcowTestCase.grassParser);
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = madcow.eval({return \'Australia\'})', 'verifyText', [id : 'addressbook_search_country'], 'Australia', 'addressbook_search_country');

        equationBlade = new GrassBlade('addressbook_search_country.verifyText = Winner is madcow.eval({return \'Australia\'})', madcowTestCase.grassParser);
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = Winner is madcow.eval({return \'Australia\'})', 'verifyText', [id : 'addressbook_search_country'], 'Winner is Australia', 'addressbook_search_country');

        equationBlade = new GrassBlade('addressbook_search_country.verifyText = The year is madcow.eval({ new Date().format(\'yyyy\')}) - awesome!', madcowTestCase.grassParser);
        def year = new Date().format('yyyy');
        checkBladeProperties(equationBlade, 'addressbook_search_country.verifyText = The year is madcow.eval({ new Date().format(\'yyyy\')}) - awesome!', 'verifyText', [id : 'addressbook_search_country'], 'The year is '+year+' - awesome!', 'addressbook_search_country');

        equationBlade = new GrassBlade("""addressbook_search_country.verifySelectFieldOptions = [[country : 'madcow.eval({return 'Australia'})'], [country: 'New Zealand']]""", madcowTestCase.grassParser);
        checkBladeProperties(equationBlade, """addressbook_search_country.verifySelectFieldOptions = [[country : 'madcow.eval({return 'Australia'})'], [country: 'New Zealand']]""", 'verifySelectFieldOptions', [id : 'addressbook_search_country'], [[country : 'Australia'], [country: 'New Zealand']], 'addressbook_search_country');
    }

    public void testEvalInvalidMacro() {
        try {
            GrassBlade macroBlade = new GrassBlade('addressbook_search_country.verifyText = madcow.eval({return RARR!})', madcowTestCase.grassParser);
            fail('should always exception');
        } catch (e) {
            assertEquals(true, e.message.startsWith('Unable to evaluate'));
            assertEquals(true, e.message.contains('is it a valid groovy command?'));
        }
    }

    public void testIsExecutable() {
        GrassBlade statementBlade = new GrassBlade('addressbook_search_country.clickLink', madcowTestCase.grassParser);
        assertEquals(true, statementBlade.executable());
        
        GrassBlade equationBlade = new GrassBlade('addressbook_search_country.verifyText = Winner is Australia', madcowTestCase.grassParser);
        assertEquals(true, equationBlade.executable());

        GrassBlade importBlade = new GrassBlade('import = CountryTemplate', madcowTestCase.grassParser);
        assertEquals(false, importBlade.executable());

        GrassBlade dataBlade = new GrassBlade('@country = Australia', madcowTestCase.grassParser);
        assertEquals(false, dataBlade.executable());
    }
}
