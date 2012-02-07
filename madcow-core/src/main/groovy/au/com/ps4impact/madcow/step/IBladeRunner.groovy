package au.com.ps4impact.madcow.step;

/**
 * Interface for a Blade Runner.
 */
interface IBladeRunner {

    /**
     * Called to execute a particular step operation.
     */
    public void execute(MadcowStepRunner stepRunner, MadcowStep step);
}
