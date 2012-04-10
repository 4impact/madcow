package au.com.ps4impact.madcow.grass

import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStepResult

/**
 * GrassParse Exception Step wrapper for a GrassParseException.
 *
 * @author Gavin Bunney
 */
class GrassParseExceptionStep extends MadcowStep {

    /**
     * Create a grass parse exception step for reporting.
     */
    GrassParseExceptionStep(GrassParseException grassParseException, MadcowTestCase testCase) {

        super(testCase, null, null);

        this.blade = new GrassBlade();
        this.blade.line = grassParseException.failingGrass;
        result = MadcowStepResult.PARSE_ERROR("Failed to parse grass: ${grassParseException.failingGrass}");
        result.detailedMessage = grassParseException.message;
    }
}
