package au.com.ps4impact.madcow.step

import au.com.ps4impact.madcow.grass.GrassBlade

/**
 * A Step Runner is the invocation hook for executing an individual MadcowStep.
 */
abstract class MadcowStepRunner {

    MadcowStepRunner() {
        this(new HashMap<String, String>());
    }
    
    MadcowStepRunner(HashMap<String, String> parameters) {
        // overridden in children
    }

    /**
     * Execute a MadcowStep. This is the main entry point for calling
     * out to a MadcowStepRunner.
     */
    public abstract void execute(MadcowStep step);

    /**
     * Determine if the step runner has a blade runner capable of executing the step.
     * This is used during test 'compilation' to see if it can even be done.
     */
    public abstract boolean hasBladeRunner(GrassBlade blade);

    /**
     * Retrieve the default mappings selector this step runner.
     * This is used as the 'type' of selector when no type is given.
     */
    public abstract String getDefaultSelector();
}
