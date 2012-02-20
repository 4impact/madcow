package au.com.ps4impact.madcow.grass;

import org.apache.log4j.Logger
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.MadcowProject

/**
 * The Grass Parser.
 */
class GrassParser {

    private static final Logger LOG = Logger.getLogger(GrassParser.class);

    protected static Map<String, String> globalDataParameters = [:];

    protected Map<String, String> dataParameters;

    public GrassParser(MadcowTestCase testCase, List<String> grassScript) {
        clearDataParameters();
        processScript(testCase, grassScript);
    }

    /**
     * Recursive function to process a grass script. If the parentStep is supplied,
     * such as when processing an importTemplate command, the created steps are given
     * that parent.
     */
    protected void processScript(MadcowTestCase testCase, List<String> grassScript, MadcowStep parentStep = null) {
        if (grassScript == null)
            return;

        grassScript.each { String lineWithWhiteSpace ->

            String line = StringUtils.strip(lineWithWhiteSpace);

            // ignore comment and empty lines
            if (line.startsWith('#') || StringUtils.isEmpty(line))
                return;

            MadcowStep step = new MadcowStep(testCase, new GrassBlade(line, this), parentStep);

            // verify the executable blade can actually be executed by the configured blade runner
            if (step.blade.executable() && !testCase.stepRunner.hasBladeRunner(step.blade)) {
                String error = "Unsupported operation '${step.blade.operation}'";
                LOG.error(error); throw new GrassParseException(error);
            }

            testCase.steps.add(step);
            if (step.blade.type == GrassBlade.GrassBladeType.IMPORT)
            {
                // recursive callback to self for the template file
                def template = ResourceFinder.locateFileOnClasspath(this.class.classLoader, "**/" + ResourceFinder.addFileExtensionIfRequired(step.blade.parameters, '.grass'), MadcowProject.TEMPLATES_DIRECTORY);
                processScript(testCase, template.readLines(), step);
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
