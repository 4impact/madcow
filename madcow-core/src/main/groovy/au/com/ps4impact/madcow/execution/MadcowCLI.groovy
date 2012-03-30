package au.com.ps4impact.madcow.execution;

import groovyjarjarcommonscli.ParseException;
import groovyjarjarcommonscli.Option
import au.com.ps4impact.madcow.MadcowTestRunner
import au.com.ps4impact.madcow.config.MadcowConfig
import au.com.ps4impact.madcow.MadcowProject

/**
 * Run Madcow from the Command Line.
 */
public class MadcowCLI {

    public static def parseArgs(String[] incomingArgs) throws ParseException {
        def cli = new CliBuilder(usage:'runMadcow [options]', header:'Options:')

        cli.with {
            h(longOpt : 'help', 'Show usage information')
            e(longOpt : 'env',  args: 1, argName: 'env-name', 'Environment to load from the madcow-config.xml')
            t(longOpt : 'test', args: Option.UNLIMITED_VALUES, valueSeparator: ',', argName : 'testname', 'Comma seperated list of test names')
            a(longOpt : 'all',  'Run all tests')
        }

        def options = cli.parse(incomingArgs);

        if (options.help || incomingArgs.size() == 0) {
            cli.usage();
        }

        return options;
    }

    /**
     * Entry point.
     */
    static main(def args)
    {
        args = args ?: new String()[];
        def options = parseArgs(args);
        if (options.help || args.size() == 0)
            return;

        MadcowConfig config = new MadcowConfig(options.env ?: null);

        if (options.test)
            MadcowTestRunner.executeTests(options.testss as ArrayList<String>, config);
        else
            MadcowTestRunner.executeTests(config);
    }

}