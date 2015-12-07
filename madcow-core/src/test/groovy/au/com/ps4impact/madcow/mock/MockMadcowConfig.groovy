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

package au.com.ps4impact.madcow.mock

import au.com.ps4impact.madcow.config.MadcowConfig

/**
 * Mock config for setting the MockMadcowStepRunner.
 *
 * @author Gavin Bunney
 */
class MockMadcowConfig {

    static MadcowConfig getMadcowConfig(boolean alwaysPass = true, String stepRunner = 'au.com.ps4impact.madcow.mock.MockMadcowStepRunner') {
        def config = new MadcowConfig();
        config.parallel = false;
        config.stepRunner = stepRunner;
        config.stepRunnerParameters = ['alwaysPass' : alwaysPass ? 'true' : 'false',
                                       'throwsException': 'false' ];
        return config;
    }

    static MadcowConfig getMadcowConfig(boolean alwaysPass, boolean throwsException, String stepRunner = 'au.com.ps4impact.madcow.mock.MockMadcowStepRunner') {
        def config = new MadcowConfig();
        config.parallel = false;
        config.stepRunner = stepRunner;
        config.stepRunnerParameters = ['alwaysPass' : alwaysPass ? 'true' : 'false',
                                       'throwsException' : throwsException ? 'true' : 'false'];
        return config;
    }

}
