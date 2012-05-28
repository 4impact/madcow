package au.com.ps4impact.madcow.step

import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger;

/**
 * Base class for a Blade Runner.
 *
 * @author Gavin Bunney
 */
abstract class BladeRunner {

    private static final Logger LOG = Logger.getLogger(BladeRunner.class);

    static ArrayList<String> pluginPackages;

    static {
        pluginPackages = initPluginPackages();
    }

    /**
     * Called to execute a particular step operation.
     */
    public abstract void execute(MadcowStepRunner stepRunner, MadcowStep step);

    /**
     * Initialise the plugin packages.
     */
    static ArrayList<String> initPluginPackages() {
        GroovyClassLoader loader = new GroovyClassLoader(BladeRunner.getClassLoader())
        Enumeration pluginFiles = loader.getResources("madcow.bladerunner.plugins");
        ArrayList<String> packages = new ArrayList<String>();
        pluginFiles.each { URL url -> packages.addAll(IOUtils.readLines(url.openStream())) }
        LOG.info("Plugin Packages: $packages");
        return packages;
    }

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

    /**
     * Retrieve an instance of the specified BladeRunner.
     */
    public static BladeRunner getBladeRunner(String bladeClassName) {
        BladeRunner bladeRunner;

        for (String pluginPackage : pluginPackages) {
            try {
                bladeRunner = getBladeRunner(pluginPackage, bladeClassName);
                if (bladeRunner != null)
                    return bladeRunner;
            } catch (ClassNotFoundException ignored) { }
        }

        throw new Exception("The specified BladeRunner '$bladeClassName' cannot be found");
    }
}
