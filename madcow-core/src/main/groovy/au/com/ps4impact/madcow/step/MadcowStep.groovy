package au.com.ps4impact.madcow.step;
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.MadcowTestCase
import java.text.DecimalFormat;

/**
 * A step of an individual madcow test case.
 *
 * @author Gavin Bunney
 */
class MadcowStep {

    GrassBlade blade;
    MadcowTestCase testCase;
    MadcowStep parent;
    ArrayList<MadcowStep> children;
    MadcowStepResult result;
    Node env;
    int sequenceNumber;

    protected static DecimalFormat SEQUENCE_NUMBER_FORMAT = new DecimalFormat('0000000');

    /**
     * Create a new Madcow Step.
     */
    MadcowStep(MadcowTestCase testCase, GrassBlade blade, MadcowStep parent) {
        this.testCase   = testCase;
        this.env        = testCase.madcowConfig.environment;
        this.blade      = blade;
        this.parent     = parent;
        this.children   = new ArrayList<MadcowStep>();
        this.sequenceNumber = testCase.steps.size() + 1;

        this.result = MadcowStepResult.NOT_YET_EXECUTED();
    }

    String getSequenceNumberString() {
        return SEQUENCE_NUMBER_FORMAT.format(sequenceNumber);
    }

    /**
     * Overridden default groovy setResult method to allow us
     * to throw upto the parent steps that the child has failed
     */
    public void setResult(MadcowStepResult madcowStepResult) {
        if (this.parent != null)
            this.parent.setResult(madcowStepResult);

        this.result = madcowStepResult;
    }

    String toString() {
        return "[testCase: $testCase, blade: $blade, parent: $parent, children: $children]";
    }
}