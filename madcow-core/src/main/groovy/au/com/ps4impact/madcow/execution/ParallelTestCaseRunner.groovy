package au.com.ps4impact.madcow.execution

import fj.Effect
import fj.control.parallel.Actor
import fj.control.parallel.Strategy
import fj.data.Option
import org.apache.log4j.Logger
import static fj.data.Option.none
import static fj.data.Option.some
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.report.JUnitTestCaseReport

/**
 * Parallel Test Runner is used to run multiple tests in parallel.
 *
 * Constructor generates a list of Actors which are invoked through the act function.
 */
public class ParallelTestCaseRunner {

    protected static final Logger LOG = Logger.getLogger(ParallelTestCaseRunner.class);

    private final Actor<Option<Exception>> callback;
    private final Actor<MadcowTestCase> parallelActor;

    def ParallelTestCaseRunner(final Strategy strategy, final def callback) {
        this.callback = callback;

        this.parallelActor = Actor.actor(strategy, { MadcowTestCase testCase ->

            LOG.info("Running ${testCase.name}");

            try {
                try {
                    testCase.execute();
                    LOG.info("Test ${testCase.name} Passed");
                } catch (e) {
                    LOG.error("Test ${testCase.name} Failed!\n\nException: $e");
                }

                JUnitTestCaseReport.createTestCaseResult(testCase);
                callback.act none();
            } catch (e) {
                callback.act some(e);
            }
        } as Effect);
    }

    def act(MadcowTestCase testCase) {
        parallelActor.act(testCase)
    }
}