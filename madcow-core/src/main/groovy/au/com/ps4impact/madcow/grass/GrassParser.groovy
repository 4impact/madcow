package au.com.ps4impact.madcow.grass;

import org.apache.log4j.Logger
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStep

/**
 * The Grass Parser.
 */
class GrassParser {

    private static final Logger LOG = Logger.getLogger(GrassParser.class);

    protected static Map<String, String> globalDataParameters = [:];

    protected Map<String, String> dataParameters;

    public GrassParser(MadcowTestCase testCase, ArrayList<String> grassScript) {
        clearDataParameters();
        processScript(testCase, grassScript);
    }

    /**
     * Recursive function to process a grass script. If the parentStep is supplied,
     * such as when processing an importTemplate command, the created steps are given
     * that parent.
     */
    protected void processScript(MadcowTestCase testCase, ArrayList<String> grassScript, MadcowStep parentStep = null) {
        if (grassScript == null)
            return;

        grassScript.each { String lineWithWhiteSpace ->

            String line = StringUtils.strip(lineWithWhiteSpace);

            // ignore comment and empty lines
            if (line.startsWith('#') || StringUtils.isEmpty(line))
                return;

            MadcowStep step = new MadcowStep(new GrassBlade(line, this), parentStep);
            testCase.steps.add(step);
            if (step.blade.type == GrassBlade.GrassBladeType.IMPORT)
            {
                // TODO: Load from disk and recursively callback
                processScript(testCase, [], step);
            }
        }
    }

    /**
     * Reset the collection of data parameters.
     * A copy of the global parameters are added to the empty list.
     */
    public void clearDataParameters() {
        dataParameters = new HashMap<String, String>();
        dataParameters.putAll(globalDataParameters.clone() as Map);
        LOG.debug("dataParameters after adding global : $dataParameters");
    }

    /**
     * Set the data parameter into the list of data parameters.
     */
    void setDataParameter(String key, String value) {
        
        key = key ?: '';
        if (key == '')
            throw new GrassParseException("Unable to set a data parameter without a name!");

        LOG.debug "Setting data parameter ${key} : ${value}"
        dataParameters.put key, value
    }

    /**
     * Determine if the given data parameter exists.
     */
    public boolean hasDataParameter(String parameterName) {
        return dataParameters[parameterName] != null;
    }

    /**
     * Retrieve the named data parameter.
     */
    public String getDataParameter(String parameterName) {
        def parameterValue;

        if (hasDataParameter(parameterName)) {
            parameterValue = dataParameters[parameterName];
        } else if (hasDataParameter("${parameterName}.default")) {
            parameterValue = dataParameters["${parameterName}.default"];
        } else {
            throw new GrassParseException("Data parameter not set '${parameterName}'");
        }

        LOG.debug "Retrieve data parameter '$parameterName' with value '$parameterValue'";
        return parameterValue;
    }
}
