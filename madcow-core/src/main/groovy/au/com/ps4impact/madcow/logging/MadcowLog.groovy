package au.com.ps4impact.madcow.logging

import org.apache.log4j.FileAppender
import org.apache.log4j.PatternLayout
import org.apache.log4j.Logger
import org.apache.log4j.Level
import au.com.ps4impact.madcow.MadcowTestCase
import org.apache.log4j.ConsoleAppender

/**
 * Madcow Logging to create a test case log file.
 *
 * @author: Gavin Bunney
 */
public class MadcowLog {

    protected static final String LOGFILE_NAME = "madcow.log";

    public static void error(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).error(message);
    }

    public static void warn(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).warn(message);
    }

    public static void info(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).info(message);
    }

    public static void debug(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).debug(message);
    }

    public static void trace(MadcowTestCase testCase, String message) {
        Logger.getLogger(testCase.name).trace(message);
    }

    /**
     * Initialise logging for the given test case.
     */
    public static void initialiseLogging(MadcowTestCase testCase) {

        Logger logger = Logger.getLogger(testCase.name);
        logger.setLevel(Level.INFO);
        logger.setAdditivity(false);

        FileAppender fileAppender = new FileAppender(new PatternLayout("%d %p [%c] - <%m>%n"),
                                                 "${testCase.getResultDirectory().getAbsolutePath()}/${LOGFILE_NAME}",
                                                 true);
        fileAppender.setName(testCase.name);
        fileAppender.setThreshold(Level.DEBUG);
        fileAppender.activateOptions();
        logger.addAppender(fileAppender);

        ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("%d [%c] - %m%n"));
        consoleAppender.setName(testCase.name);
        consoleAppender.setThreshold(Level.INFO);
        consoleAppender.activateOptions();
        logger.addAppender(consoleAppender);
    }

    /**
     * Shutdown the test case logging.
     */
    public static void shutdownLogging(MadcowTestCase testCase) {
        Logger.getLogger(testCase.name).removeAppender(testCase.name);
    }
}
