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

import au.com.ps4impact.madcow.report.IJSONSerializable
import java.util.regex.Matcher
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.mappings.MadcowMappings
import groovy.transform.AutoClone
import au.com.ps4impact.madcow.util.FormatUtil

/**
 * A blade of grass.
 * This is an interchangeable format for a parsed line of grass code.
 * <p/>
 * Four formats are supported, STATEMENT, EQUATION, DATA_PARAMETER and IMPORT.
 * <p/>
 * A Statement is a line without an '=', with an Equation being one with.
 * A Data Parameter is an Equation that is setting a data parameter value.<br/>
 * The key difference being Statements do not have parameters, Equations and Data Parameters do.
 * <p/>
 * <pre>
 * addressbook_search_country.verifyText = Australia
 *  ^ mapping                  ^ operation  ^ parameters
 * </pre>
 *
 * @author Gavin Bunney
 */
@AutoClone
class GrassBlade implements IJSONSerializable {

    /**
     * Type of Grass Blade.<br/>
     * Statement is a blade in the format of 'button.clickLink'.<br/>
     * Equation is a blade in the format of 'verifyText = Australia'.<br/>
     * Data Parameter is a blade in the format of '@country = Australia'.<br/>
     * Import is importing a template, a blade in the format of 'import = CountryTemplate'.
     */
    enum GrassBladeType { STATEMENT, EQUATION, DATA_PARAMETER, IMPORT }

    GrassBladeType type;
    String  operation;
    def     parameters;
    String  line;
    String  mapping;
    String  mappingSelectorType;
    String  mappingSelectorValue;

    public static final String DATA_PARAMETER_KEY = '@';

    protected static final String DATA_PARAMETER_INLINE_REGEX = '@\\{([^}]+)\\}';

    protected static final String EVAL_MACRO_INLINE_REGEX = 'madcow.eval\\(\\{([^}]+)\\}\\)';

    /**
     * Constructor that doesn't parse a grass line.
     */
    GrassBlade() {
        //
    }

    /**
     * Create a new Blade of grass for the given line.<br/>
     * The line is split into the operation, parameters and mapping properties.
     */
    GrassBlade(String grassLine, GrassParser parser) {
        this.line = grassLine ?: '';

        if (!this.line.contains('.') && !this.line.contains('='))
            throw new GrassParseException(this.line, "No '.' or '=' found - doesn't appear to do anything.");

        if (this.line.contains('=')) {
            def equation = this.line.split('=', 2);
            parseStatement(parser, equation[0].trim());
            this.type = parseParameters(equation[1].trim(), parser);

        } else {
            this.type = GrassBladeType.STATEMENT;
            this.parameters = null;
            parseStatement(parser, this.line.trim());
        }
    }

    /**
     * Parse the statement into the Operation and Mapping properties.
     * This isn't just for Statement blades, as even equations have a statement!
     */
    protected void parseStatement(GrassParser parser, String statement) {
        if (statement.contains('.') && !statement.startsWith(DATA_PARAMETER_KEY)) {
            def operations = statement.split('\\.', 2);
            this.operation = operations[1].trim();
            this.mapping = operations[0].trim();
            
            def selector = MadcowMappings.getSelectorFromMapping(parser.testCase, this.mapping);
            this.mappingSelectorType = StringUtils.lowerCase(selector.keySet().toArray().first() as String);
            this.mappingSelectorValue = selector.values().toArray().first();
        } else {
            this.operation = statement;
        }

        if (this.operation.equalsIgnoreCase("IMPORT") || this.operation.equalsIgnoreCase("IMPORTTEMPLATE"))
            this.type = GrassBladeType.IMPORT;
    }

    /**
     * Parse the parameters string.
     * First part will set/get any data parameters, it is then evaluated through
     * evalMe to parse out into a List or Map object if it is one... otherwise it'll stay as a string.
     *
     * Returns the type of blade, DATA_PARAMETER or EQUATION.
     */
    public GrassBladeType parseParameters(String parametersString, GrassParser parser) {

        Matcher inlineParameters = parametersString =~ DATA_PARAMETER_INLINE_REGEX;
        if (inlineParameters.size() > 0) {
            inlineParameters.each { String paramMatch, String paramName ->
                parametersString = StringUtils.replace(parametersString, paramMatch, parser.getDataParameter("${DATA_PARAMETER_KEY}${paramName}"));
            }
        }

        if (parametersString.startsWith(DATA_PARAMETER_KEY)) {
            parametersString = parser.getDataParameter(parametersString);
        }

        Matcher inlineMacros = parametersString =~ EVAL_MACRO_INLINE_REGEX;
        if (inlineMacros.size() > 0) {
            inlineMacros.each { String macroMatch, String macroContents ->
                try {
                    parametersString = StringUtils.replace(parametersString, macroMatch, (new GroovyShell().evaluate(macroContents)) as String);
                } catch (e) {
                    throw new GrassParseException(line, "Unable to evaluate $macroMatch - is it a valid groovy command?\n\n$e");
                }
            }
        }

        boolean isSettingDataParameter = false;
        if (this.operation.startsWith(DATA_PARAMETER_KEY)) {
            isSettingDataParameter = true;
            parser.setDataParameter(this.operation, parametersString);
        }

        // If the param is to be stored at runtime, then set a placeholder for runtime processing
        if (this.operation.equals('store')){
            parser.setDataParameter(DATA_PARAMETER_KEY + parametersString, "${DATA_PARAMETER_KEY}{${parametersString}}")
        }

        this.parameters = ParseUtils.evalMe(parametersString);
        return this.type != null ? this.type : isSettingDataParameter ? GrassBladeType.DATA_PARAMETER : GrassBladeType.EQUATION;
    }

    /**
     * Determines if the Grass Blade is executable, that is, an Equation or Statement.
     */
    public boolean executable() {
        return (this.type == GrassBlade.GrassBladeType.EQUATION || this.type == GrassBlade.GrassBladeType.STATEMENT)
    }

    /**
     * Determine if the line has been 'parsed' - that is, is the supplied grass line the same
     * as what the Blade is when passed through the toString function.
     */
    public boolean isLineParsed() {
        // compare but ignore spaces and quotes
        return StringUtils.strip(this.line, "'\" ") != StringUtils.strip(this.toString(), "'\" ");
    }

    public String toString() {

        String parametersString = null;
        if (parameters != null) {
            if (parameters instanceof Map)
                parametersString = FormatUtil.convertMapToString(parameters);
            else if (parameters instanceof List)
                parametersString = FormatUtil.convertListToString(parameters);
            else
                parametersString = parameters as String;
        }

        switch (this.type) {
            case GrassBladeType.IMPORT:
            case GrassBladeType.DATA_PARAMETER:
            case GrassBladeType.EQUATION:
                return String.format('%s%s = %s', StringUtils.isNotBlank(mappingSelectorValue) ? "${mappingSelectorValue}." : '',
                        operation ?: '',
                        parametersString ?: '');

            case GrassBladeType.STATEMENT:
                return String.format('%s%s', StringUtils.isNotBlank(mappingSelectorValue) ? "${mappingSelectorValue}." : '',
                                             operation ?: '');

            default:
                return '';
        }
    }

    @Override
    Map toJSON() {
        return [
            type: this.type.toString(),
            operation: this.operation,
            line: this.line
        ]
    }
}