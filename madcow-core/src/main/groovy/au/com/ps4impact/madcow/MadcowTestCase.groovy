package au.com.ps4impact.madcow;

import org.apache.log4j.Logger
import au.com.ps4impact.madcow.grass.GrassParser
import au.com.ps4impact.madcow.step.MadcowStep;

/**
 * A Madcow Test Case.
 */
class MadcowTestCase {

    private static final Logger LOG = Logger.getLogger(MadcowTestCase.class);

    public GrassParser grassParser = null;
    
    public ArrayList<MadcowStep> steps = new ArrayList<MadcowStep>();

    public MadcowTestCase(ArrayList<String> grassScript = null) {
        this.parseScript(grassScript);
    }
    
    public void parseScript(ArrayList<String> grassScript) {
        grassParser = new GrassParser(this, grassScript);
    }
    
}
