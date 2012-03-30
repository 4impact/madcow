package au.com.ps4impact.madcow;

import org.apache.log4j.Logger
import au.com.ps4impact.madcow.grass.GrassParser
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.step.MadcowStepResult
import groovy.text.GStringTemplateEngine
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.commons.lang3.StringUtils

/**
 * A Madcow Test Case.
 */
class MadcowTestCase {

    private static final Logger LOG = Logger.getLogger(MadcowTestCase.class);

    public String name;
    public ArrayList<String> grassScript;
    public GrassParser grassParser;
    public MadcowStepRunner stepRunner;
    
    public ArrayList<MadcowStep> steps = new ArrayList<MadcowStep>();
    public MadcowStep lastExecutedStep;

    public MadcowConfig madcowConfig;

    /**
     * Create a new MadcowTestCase, parsing the given grassScript if specified.
     */
    public MadcowTestCase(String name, MadcowConfig madcowConfig = new MadcowConfig(), ArrayList<String> grassScript = null) {
        this.name = name;
        this.madcowConfig = madcowConfig;
        this.grassScript = grassScript;

        try {
            this.stepRunner = Class.forName(this.madcowConfig.stepRunner).newInstance([this.madcowConfig.stepRunnerParameters ?: new HashMap<String, String>()] as Object[]) as MadcowStepRunner;
        } catch (ClassNotFoundException cnfe) {
            throw new Exception("The specified MadcowStepRunner '${this.madcowConfig.stepRunner}' cannot be found\n\n$cnfe");
        } catch (ClassCastException cce) {
            throw new Exception("The specified MadcowStepRunner '${this.madcowConfig.stepRunner}' isn't a MadcowStepRunner!\n\n$cce");
        } catch (e) {
            throw new Exception("Unexpected error creating the MadcowStepRunner '${this.madcowConfig.stepRunner}'\n\n$e");
        }
    }

    /**
     * Overloaded constructor to allow only grass script to be specified.
     */
    public MadcowTestCase(String name, ArrayList<String> grassScript) {
        this(name, new MadcowConfig(), grassScript);
    }

    /**
     * Parse the given grass script through a new GrassParser instance.
     */
    public GrassParser parseScript() {
        grassParser = new GrassParser(this, grassScript);
        return grassParser;
    }

    /**
     * Execute the Madcow Test Case. This will call out to the
     * MadcowStepRunner specified by configuration for handling the step execution.
     */
    public void execute() {

        if (grassParser == null)
            parseScript();

        steps.each { step ->
            executeStep(step);
        }
    }

    /**
     * Recursive callback to execute an individual step.
     */
    protected void executeStep(MadcowStep step) {
        
        // only execute blades that need executing
        if (step.blade.executable())
            this.stepRunner.execute(step);
        else
            step.result = MadcowStepResult.NO_OPERATION("${step.blade.type} has no operation to execute");

        this.lastExecutedStep = step;
        if (step.result.failed())
            throw new RuntimeException("Step failed - ${step.result}");

        step.children.each { child -> executeStep (child) }
    }

    /**
     * Create the result files.
     */
    public void createResult() {

        // TODO - refactor out into some fancy facade thing

        def binding = ['errorCount' : '0',
                       'failureCount' : this.lastExecutedStep.result.failed() ? '1' : '0',
                       'hostname' : InetAddress.getLocalHost().getHostName(),
                       'testName' : name,
                       'time' : '',
                       'timestamp': '',
                      ];
        def engine = new GStringTemplateEngine();
        def template = engine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'result-junit.gtemplate').URL).make(binding);
        
        String templateContents = template.toString();
        def result = new File(MadcowProject.RESULTS_DIRECTORY + "/TEST-${StringUtils.replace(name, '/', '_')}.xml");
        result << templateContents;
    }

    public String toString() {
        return name;
    }
}
