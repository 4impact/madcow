/*
 * Copyright 2012 4impact, Brisbane, Australia
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package au.com.ps4impact.madcow.execution

import au.com.ps4impact.madcow.MadcowTestCaseException
import fj.Effect
import fj.P
import fj.control.parallel.Actor
import fj.control.parallel.Strategy
import fj.data.Option
import fj.P2
import org.apache.log4j.Logger
import static fj.data.Option.none
import static fj.data.Option.some
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.report.IMadcowReport
import au.com.ps4impact.madcow.logging.MadcowLog

/**
 * Parallel Test Runner is used to run multiple tests in parallel.
 *
 * Constructor generates a list of Actors which are invoked through the act function.
 *
 * @author Gavin Bunney
 */
class ParallelTestCaseRunner {

    protected static final Logger LOG = Logger.getLogger(ParallelTestCaseRunner.class);

    private final Actor<Option<Exception>> callback;
    private final Actor<P2<MadcowTestCase, List<IMadcowReport>>> parallelActor;

    def ParallelTestCaseRunner(final Strategy strategy, final def callback) {
        this.callback = callback;

        this.parallelActor = Actor.actor(strategy, { P2<MadcowTestCase, List<IMadcowReport>> parameters ->

            MadcowTestCase testCase = parameters._1();
            Throwable exception = null;

                try {

                    if (testCase instanceof MadcowTestCaseException) {
                        testCase.logInfo("Failed attempting to run ${testCase.name}");
                    } else {
                        testCase.logInfo("Running ${testCase.name}");
                        try {
                            testCase.execute();
                            testCase.logInfo("Test ${testCase.name} Passed");
                        } catch (e) {
                            testCase.testCaseError = true;
                            testCase.logError("Test ${testCase.name} Failed!\nException: $e");
                            return callback.act(P.p(testCase, some(e)));
                        }
                    }
                    callback.act(P.p(testCase, none()));
                } catch (error) {
                    LOG.error("${testCase.name} threw an unexpected exception:\n$error")
                    //capture the error and create report on it
                    exception = error
                    parameters._2().each { reporter -> reporter.createErrorTestCaseReport(testCase.name, error) }
                    callback.act(P.p(testCase, some(error)));
                } finally {
                    MadcowLog.shutdownLogging(testCase);
                    //if there was no unexpected errors then report on it
                    if (!exception) {
                        parameters._2().each { reporter -> reporter.createTestCaseReport(testCase) }
                    }
                }

        } as Effect);
    }

    def act(P2<MadcowTestCase, List<IMadcowReport>> parameters) {
        parallelActor.act(parameters)
    }
}