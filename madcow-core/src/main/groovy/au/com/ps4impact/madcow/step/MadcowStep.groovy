package au.com.ps4impact.madcow.step;
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.config.MadcowConfig;

/**
 * A step of an individual madcow test case.
 */
class MadcowStep {

    GrassBlade blade;
    MadcowStep parent;
    ArrayList<MadcowStep> children;
    MadcowStepResult result;
    Node config;
    Node env;

    /**
     * Create a new Madcow Step.
     */
    MadcowStep(GrassBlade blade, MadcowStep parent) {
        this.blade      = blade;
        this.parent     = parent;
        this.children   = new ArrayList<MadcowStep>();
        this.config     = MadcowConfig.ConfigData;
        this.env        = MadcowConfig.EnvironmentData;
    }
    
    String toString() {
        return "[blade: $blade, parent: $parent, childen: $children]";
    }
}