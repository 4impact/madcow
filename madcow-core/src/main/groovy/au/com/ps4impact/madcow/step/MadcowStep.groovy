package au.com.ps4impact.madcow.step;
import au.com.ps4impact.madcow.grass.GrassBlade;

/**
 * A step of an individual madcow test case.
 */
class MadcowStep {

    enum MadcowStepResult {
        PASS,
        FAIL
    }

    GrassBlade blade;
    MadcowStep parent;
    ArrayList<MadcowStep> children;
    MadcowStepResult result;

    /**
     * Create a new Madcow Step.
     */
    MadcowStep(GrassBlade blade, MadcowStep parent) {
        this.blade = blade;
        this.parent = parent;
        this.children = new ArrayList<MadcowStep>();
    }
    
    String toString() {
        return "[blade: $blade, parent: $parent, childen: $children]";
    }
}