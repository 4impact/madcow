package au.com.ps4impact.madcow.grass

import java.util.regex.Matcher
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

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
 */
class GrassBlade {

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
    HashMap selector;

    protected static final Logger LOG = Logger.getLogger(GrassBlade.class);

    protected static final String DATA_PARAMETER_KEY = '@';

    protected static final String DATA_PARAMETER_INLINE_REGEX = '@\\{([^}]+)\\}';

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
            parseStatement(equation[0].trim());
            this.type = parseParameters(equation[1].trim(), parser);

        } else {
            this.type = GrassBladeType.STATEMENT;
            this.parameters = null;
            parseStatement(this.line.trim());
        }
    }

    /**
     * Parse the statement into the Operation and Mapping properties.
     * This isn't just for Statement blades, as even equations have a statement!
     */
    protected void parseStatement(String statement) {
        if (statement.contains('.')) {
            def operations = statement.split('\\.', 2);
            this.operation = operations[1].trim();
            this.mapping = operations[0].trim();
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
    protected GrassBladeType parseParameters(String parametersString, GrassParser parser) {

        Matcher inlineParameters = parametersString =~ DATA_PARAMETER_INLINE_REGEX;
        if (inlineParameters.size() > 0) {
            inlineParameters.each { String paramMatch, String paramName ->
                parametersString = StringUtils.replace(parametersString, paramMatch, parser.getDataParameter("${DATA_PARAMETER_KEY}${paramName}"));
            }
        }

        if (parametersString.startsWith(DATA_PARAMETER_KEY)) {
            parametersString = parser.getDataParameter(parametersString);
        }

        boolean isSettingDataParameter = false;
        if (this.operation.startsWith(DATA_PARAMETER_KEY)) {
            isSettingDataParameter = true;
            parser.setDataParameter(this.operation, parametersString);
        }

        this.parameters = ParseUtils.evalMe(parametersString);
        return this.type != null ? this.type : isSettingDataParameter ? GrassBladeType.DATA_PARAMETER : GrassBladeType.EQUATION;
    }

    String toString() {
        return "operation: {${this.operation}} | selector: {${this.selector}} | " +
               (this.type != GrassBladeType.STATEMENT ? "params: {${this.parameters}} | " : '') +
               "line: {${this.line}} | " + "mapping: {${this.mapping}}";
    }
}