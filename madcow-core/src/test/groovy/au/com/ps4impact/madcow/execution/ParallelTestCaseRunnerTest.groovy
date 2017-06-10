package au.com.ps4impact.madcow.execution

import au.com.ps4impact.madcow.MadcowTestCase
import fj.Effect
import fj.P
import fj.P2
import fj.Unit
import fj.control.parallel.Actor
import fj.control.parallel.Strategy
import fj.data.Option

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class ParallelTestCaseRunnerTest extends GroovyTestCase {

    ArrayList<String> grassScript = new ArrayList<String>()

    ExecutorService pool = Executors.newFixedThreadPool(10)
    Strategy<Unit> strategy = Strategy.executorStrategy(pool)

    ConcurrentHashMap exceptions = [:]
    AtomicInteger numberOfTestsRan = new AtomicInteger(0)

    def callback = Actor.queueActor(strategy, { P2<MadcowTestCase, Option<Exception>> result ->
        numberOfTestsRan.andIncrement;
        exceptions.put(result._1(), result._2())
    } as Effect);

    void testTestCaseErrorTrue() {
        MadcowTestCase madcowTestCase = new MadcowTestCase("SingleTestCaseRunnerTest-testExecutionException", grassScript) {
            @Override
            void execute() {
                throw new Exception()
            }

            @Override
            protected void createStepRunner() {
            }
        }

        executeTest(madcowTestCase)
        assertTrue(madcowTestCase.testCaseError == true)
    }

    void testTestCaseErrorFalse() {
        MadcowTestCase madcowTestCase = new MadcowTestCase("SingleTestCaseRunnerTest-testExecutionException", grassScript) {
            @Override
            void execute() {
            }

            @Override
            protected void createStepRunner() {
            }
        }

        executeTest(madcowTestCase)
        assertTrue(madcowTestCase.testCaseError == false)
    }


    private void executeTest(MadcowTestCase madcowTestCase) {
        def parallelTestCaseRunner = new ParallelTestCaseRunner(strategy, callback)
        try {
            parallelTestCaseRunner.act(P.p(madcowTestCase, null))
        } catch(e) {}

        while (numberOfTestsRan.get() < 1) {
            Thread.sleep(500);
        }
    }
}
