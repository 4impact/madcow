package au.com.ps4impact.madcow;

import au.com.ps4impact.madcow.grass.GrassParser
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepRunner
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.step.MadcowStepResult
import au.com.ps4impact.madcow.grass.GrassParseException
import au.com.ps4impact.madcow.grass.GrassParseExceptionStep
import java.text.DecimalFormat
import org.apache.commons.lang3.time.StopWatch

/**
 * A Madcow Test Case.
 *
 * @author Gavin Bunney
 */
class MadcowTestCase {

    public String name;
    public MadcowTestSuite testSuite;
    public ArrayList<String> grassScript;
    public GrassParser grassParser;
    public MadcowStepRunner stepRunner;
    
    public ArrayList<MadcowStep> steps = new ArrayList<MadcowStep>();
    public MadcowStep lastExecutedStep;

    public StopWatch stopWatch;

    public MadcowConfig madcowConfig;

    public Map<String, Object> runtimeStorage = new HashMap<String, Object>();

    protected static final DecimalFormat TIME_SECONDS_FORMAT = new DecimalFormat("########.###");

    /**
     * Create a new MadcowTestCase, parsing the given grassScript if specified.
     */
    public MadcowTestCase(String name, MadcowConfig madcowConfig = new MadcowConfig(), ArrayList<String> grassScript = null) {
        this.name = name;
        this.madcowConfig = madcowConfig;
        this.grassScript = grassScript;
        this.grassParser = new GrassParser(this);
        this.runtimeStorage = new HashMap<String, Object>();

        try {
            this.stepRunner = Class.forName(this.madcowConfig.stepRunner).newInstance([this.madcowConfig.stepRunnerParameters ?: new HashMap<String, String>()] as Object[]) as MadcowStepRunner;
        } catch (ClassNotFoundException cnfe) {
            throw new Exception("The specified MadcowStepRunner '${this.madcowConfig.stepRunner}' cannot be found\n\n$cnfe");
        } catch (ClassCastException cce) {
            throw new Exception("The specified MadcowStepRunner '${this.madcowConfig.stepRunner}' isn't a MadcowStepRunner!\n\n$cce");
        } catch (e) {
            throw new Exception("Unexpected error creating the MadcowStepRunner '${this.madcowConfig.stepRunner}'\n\n$e");
        }
    }

    /**
     * Overloaded constructor to allow only grass script to be specified.
     */
    public MadcowTestCase(String name, ArrayList<String> grassScript) {
        this(name, new MadcowConfig(), grassScript);
    }

    /**
     * Parse the given grass script.
     */
    public void parseScript() {

        this.steps = new ArrayList<MadcowStep>();

        try {
            grassParser.processScriptForTestCase(grassScript);
        } catch (GrassParseException gpe) {
            MadcowStep exceptionStep = new GrassParseExceptionStep(gpe, this);
            this.steps.clear();
            this.steps.add(exceptionStep);
            this.lastExecutedStep = exceptionStep;
            stopWatch = new StopWatch();
            stopWatch.start(); stopWatch.stop();
            throw new RuntimeException("Grass Parse Error: ${gpe.message}");
        }
    }

    /**
     * Execute the Madcow Test Case. This will call out to the
     * MadcowStepRunner specified by configuration for handling the step execution.
     */
    public void execute() {
        parseScript();

        stopWatch = new StopWatch();
        stopWatch.start();

        steps.each { step ->
            executeStep(step);
        }

        stopWatch.stop();
    }

    /**
     * Recursive callback to execute an individual step.
     */
    protected void executeStep(MadcowStep step) {
        
        // only execute blades that need executing
        if (step.blade.executable())
            this.stepRunner.execute(step);
        else
            step.result = MadcowStepResult.NO_OPERATION();

        this.lastExecutedStep = step;
        if (step.result.failed())
            throw new RuntimeException("Step failed - ${step.result}");

        step.children.each { child -> executeStep (child) }
    }

    public File getResultDirectory() {
        File resultDir = new File("${MadcowProject.MADCOW_REPORT_DIRECTORY}/${this.name}");
        if (!resultDir.exists())
            resultDir.mkdirs();

        return resultDir;
    }

    public String toString() {
        return name;
    }

    public String getTotalTimeInSeconds() {
        try {
            return TIME_SECONDS_FORMAT.format(stopWatch.time / 1000);
        } catch (ignored) {
            return TIME_SECONDS_FORMAT.format(0);
        }
    }
}
