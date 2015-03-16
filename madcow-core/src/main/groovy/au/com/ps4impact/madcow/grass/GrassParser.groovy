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

package au.com.ps4impact.madcow.grass;

import org.apache.log4j.Logger
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.MadcowProject

/**
 * The Grass Parser.
 *
 * @author Gavin Bunney
 */
class GrassParser {

    private static final Logger LOG = Logger.getLogger(GrassParser.class);

    protected static Map<String, String> globalDataParameters = [:];

    protected Map<String, String> dataParameters;

    public MadcowTestCase testCase;

    static {
        globalDataParameters = new GlobalDataParametersFileHelper().initProcessProperties(null)
        LOG.debug("Global data parameters: $globalDataParameters");
    }

    public GrassParser(MadcowTestCase testCase) {
        this.testCase = testCase;
        clearDataParameters();
    }

    /**
     * Processing entry point.
     */
    public processScriptForTestCase(List<String> grassScript) {
        clearDataParameters();
        processScript(grassScript);
    }

    /**
     * Recursive function to process a grass script. If the parentStep is supplied,
     * such as when processing an importTemplate command, the created steps are given
     * that parent.
     */
    protected void processScript(List<String> grassScript, MadcowStep parentStep = null) {
        if (grassScript == null)
            return;

        grassScript.each { String lineWithWhiteSpace ->

            String line = StringUtils.strip(lineWithWhiteSpace);

            // ignore comment and empty lines
            if (line.startsWith('#') || StringUtils.isEmpty(line))
                return;

            // ignore test case
            if (line.equalsIgnoreCase('madcow.ignore') || line.equalsIgnoreCase('madcow.skip')){
                testCase.ignoreTestCase = true;
                return;
            }

            MadcowStep step = new MadcowStep(testCase, new GrassBlade(line, this), parentStep);

            // verify the executable blade can actually be executed by the configured blade runner
            try {
                if (step.blade.executable() && !testCase.stepRunner.hasBladeRunner(step.blade)) {
                    throw new GrassParseException("Unsupported operation '${step.blade.operation}'");
                }
            } catch (GrassParseException gpe) {
                throw new GrassParseException(step.blade.line, "Error processing blade: ${step.blade.toString()}\n${gpe.message}");
            }


            if (parentStep != null)
                parentStep.children.add(step);
            else
                testCase.steps.add(step);

            if (step.blade.type == GrassBlade.GrassBladeType.IMPORT)
            {
                // recursive callback to self for the template file
                def template = ResourceFinder.locateFileOnClasspath(this.class.classLoader, "**/" + ResourceFinder.addFileExtensionIfRequired(step.blade.parameters, '.grass'), MadcowProject.TEMPLATES_DIRECTORY);
                processScript(template.readLines(), step);
            }
        }
    }

    /**
     * Reset the collection of data parameters.
     * A copy of the global parameters are added to the empty list.
     */
    public void clearDataParameters() {
        dataParameters = new HashMap<String, String>();
        dataParameters.putAll(globalDataParameters.clone() as Map);
    }

    /**
     * Set the data parameter into the list of data parameters.
     */
    void setDataParameter(String key, String value) {
        
        key = key ?: '';
        if (key == '')
            throw new GrassParseException("Unable to set a data parameter without a name!");

        LOG.debug "Setting data parameter ${key} : ${value}"
        dataParameters.put key, value
    }

    /**
     * Determine if the given data parameter exists.
     */
    public boolean hasDataParameter(String parameterName) {
        return dataParameters[parameterName] != null;
    }

    /**
     * Retrieve the named data parameter.
     */
    public String getDataParameter(String parameterName) {
        def parameterValue;

        if (hasDataParameter(parameterName)) {
            parameterValue = dataParameters[parameterName];
        } else if (hasDataParameter("${parameterName}.default")) {
            parameterValue = dataParameters["${parameterName}.default"];
        } else {
            throw new GrassParseException("Data parameter not set '${parameterName}'");
        }

        LOG.debug "Retrieve data parameter '$parameterName' with value '$parameterValue'";
        return parameterValue;
    }
}
