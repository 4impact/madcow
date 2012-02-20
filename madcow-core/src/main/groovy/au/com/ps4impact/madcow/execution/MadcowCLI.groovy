package au.com.ps4impact.madcow.execution;

import groovyjarjarcommonscli.ParseException;
import groovyjarjarcommonscli.Option
import au.com.ps4impact.madcow.MadcowTestRunner
import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Run Madcow from the Command Line.
 */
public class MadcowCLI {

    public static def parseArgs(String[] incomingArgs) throws ParseException {
        def cli = new CliBuilder(usage:'runMadcow [options]', header:'Options:')

        cli.with {
            h(longOpt : 'help', 'Show Usage Information')
            e(longOpt : 'env',  args: 1, argName: 'env-name', 'environment to load from the madcow-config.xml')
            t(longOpt : 'test', args: Option.UNLIMITED_VALUES, valueSeparator: ',', argName : 'testname', 'comma seperated list of test names')
        }

        def options = cli.parse(incomingArgs);

        if (options.help) {
            cli.usage();
        }

        return options;
    }

    /**
     * Entry point.
     */
    static main(String[] args)
    {
        def options = parseArgs(args);
        if (options.help)
            return;

        MadcowConfig config = new MadcowConfig(options.env ?: null);
        MadcowTestRunner.executeTests(options.testss as ArrayList<String>, config);
    }

}