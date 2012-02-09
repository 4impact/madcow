package au.com.ps4impact.madcow.step;
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestCase;

/**
 * A step of an individual madcow test case.
 */
class MadcowStep {

    GrassBlade blade;
    MadcowTestCase testCase;
    MadcowStep parent;
    ArrayList<MadcowStep> children;
    MadcowStepResult result;
    Node env;

    /**
     * Create a new Madcow Step.
     */
    MadcowStep(MadcowTestCase testCase, GrassBlade blade, MadcowStep parent) {
        this.testCase   = testCase;
        this.env        = testCase.madcowConfig.environment;
        this.blade      = blade;
        this.parent     = parent;
        this.children   = new ArrayList<MadcowStep>();
    }
    
    String toString() {
        return "[testCase: $testCase, blade: $blade, parent: $parent, children: $children]";
    }
}