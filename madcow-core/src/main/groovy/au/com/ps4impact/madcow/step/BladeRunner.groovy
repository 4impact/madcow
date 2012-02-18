package au.com.ps4impact.madcow.step;

/**
 * Base class for a Blade Runner.
 */
abstract class BladeRunner {

    /**
     * Called to execute a particular step operation.
     */
    public abstract void execute(MadcowStepRunner stepRunner, MadcowStep step);

    /**
     * Retrieve an instance of the specified BladeRunner.
     */
    public static BladeRunner getBladeRunner(String bladePackage, String bladeClassName) {
        BladeRunner bladeRunner;
        String fqn = "$bladePackage.$bladeClassName";

        try {
            return Class.forName(fqn).newInstance() as BladeRunner;
        } catch (ClassNotFoundException cnfe) {
            throw new Exception("The specified BladeRunner '$fqn' cannot be found\n\n$cnfe");
        } catch (ClassCastException cce) {
            throw new Exception("The specified BladeRunner '$fqn' isn't a BladeRunner!\n\n$cce");
        } catch (e) {
            throw new Exception("Unexpected error creating the BladeRunner '$fqn'\n\n$e");
        }
    }
}
