package au.com.ps4impact.madcow.step

import au.com.ps4impact.madcow.MadcowTestCase

/**
 * A Step Runner is the invocation hook for executing an individual MadcowStep.
 */
abstract class MadcowStepRunner {

    /**
     * Execute a MadcowStep. This is the main entry point for calling
     * out to a MadcowStepRunner.
     */
    public abstract void execute(MadcowTestCase testCase, MadcowStep step);
}
