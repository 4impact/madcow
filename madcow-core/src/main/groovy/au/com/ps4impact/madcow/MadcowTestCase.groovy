package au.com.ps4impact.madcow;

import org.apache.log4j.Logger
import au.com.ps4impact.madcow.grass.GrassParser
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepRunner

/**
 * A Madcow Test Case.
 */
class MadcowTestCase {

    private static final Logger LOG = Logger.getLogger(MadcowTestCase.class);

    public GrassParser grassParser = null;
    
    public ArrayList<MadcowStep> steps = new ArrayList<MadcowStep>();

    public MadcowTestCase(ArrayList<String> grassScript = null) {
        this.parseScript(grassScript);
    }
    
    public void parseScript(ArrayList<String> grassScript) {
        grassParser = new GrassParser(this, grassScript);
    }

    public void execute() {

        MadcowStepRunner stepRunner = Class.forName(MadcowConfig.StepRunner).newInstance() as MadcowStepRunner;
        
        steps.each { step ->
            executeStep(stepRunner, step);
        }
    }
    
    protected void executeStep(MadcowStepRunner stepRunner, MadcowStep step) {
        LOG.trace("executeStep START - $step")
        stepRunner.execute(this, step);

        if (step.result == MadcowStep.MadcowStepResult.FAIL)
            throw new Exception("FAILED");
        LOG.trace("executeStep step children...")
        step.children.each { child -> executeStep (stepRunner, child) }

        LOG.trace("executeStep END")
    }
    
}
