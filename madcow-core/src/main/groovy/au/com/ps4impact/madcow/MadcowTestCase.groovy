package au.com.ps4impact.madcow;

import org.apache.log4j.Logger
import au.com.ps4impact.madcow.grass.GrassParser
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * A Madcow Test Case.
 */
class MadcowTestCase {

    private static final Logger LOG = Logger.getLogger(MadcowTestCase.class);

    public GrassParser grassParser = null;
    
    public ArrayList<MadcowStep> steps = new ArrayList<MadcowStep>();

    /**
     * Create a new MadcowTestCase, parsing the given grassScript if specified.
     */
    public MadcowTestCase(ArrayList<String> grassScript = null) {
        this.parseScript(grassScript);
    }

    /**
     * Parse the given grass script through a new GrassParser instance.
     */
    public void parseScript(ArrayList<String> grassScript) {
        grassParser = new GrassParser(this, grassScript);
    }

    /**
     * Execute the Madcow Test Case. This will call out to the
     * MadcowStepRunner specified by configuration for handling the step execution.
     */
    public void execute() {

        MadcowStepRunner stepRunner;

        try {
            stepRunner = Class.forName(MadcowConfig.StepRunner).newInstance() as MadcowStepRunner;
        } catch (ClassNotFoundException cnfe) {
            throw new Exception("The specified MadcowStepRunner '${MadcowConfig.StepRunner}' cannot be found\n\n$cnfe");
        } catch (ClassCastException cce) {
            throw new Exception("The specified MadcowStepRunner '${MadcowConfig.StepRunner}' isn't a MadcowStepRunner!\n\n$cce");
        } catch (e) {
            throw new Exception("Unexpected error creating the MadcowStepRunner '${MadcowConfig.StepRunner}'\n\n$e");
        }
        
        steps.each { step ->
            executeStep(stepRunner, step);
        }
    }

    /**
     * Recursive callback to execute an individual step.
     */
    protected void executeStep(MadcowStepRunner stepRunner, MadcowStep step) {
        
        // only execute blades that need executing
        if (step.blade.type == GrassBlade.GrassBladeType.EQUATION || step.blade.type == GrassBlade.GrassBladeType.STATEMENT)
            stepRunner.execute(this, step);
        else
            step.result = MadcowStepResult.NO_OPERATION("${step.blade.type} has no operation to execute");

        if (step.result.failed())
            throw new Exception("Step failed - ${step.result}");

        step.children.each { child -> executeStep (stepRunner, child) }
    }
    
}
