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

package au.com.ps4impact.madcow.config

import au.com.ps4impact.madcow.report.IJSONSerializable

import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import org.xml.sax.SAXParseException
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.commons.lang3.StringUtils
import au.com.ps4impact.madcow.MadcowProject

/**
 *  The MadcowConfig class which holds all the initialisation information
 *
 * @author Gavin Bunney
 */
class MadcowConfig implements IJSONSerializable {

    public Node execution;
    public Node environment;

    public int threads = 10;

    public int retryCount = 0;

    public boolean parallel = true;

    public String stepRunner;
    public HashMap<String, String> stepRunnerParameters;

    public static MadcowConfig SHARED_CONFIG;

    MadcowConfig(String envName = null, String madcowConfigFile = MadcowProject.CONFIGURATION_FILE) {

        String xmlFileContents = ResourceFinder.locateFilesOnClasspath(this.getClass().getClassLoader(), madcowConfigFile).first().text;

        validateConfigFormat(xmlFileContents);
        parseConfig(xmlFileContents, envName);
    }

    /**
     * Validate the Config file against the madcow-config.xsd
     */
    public void validateConfigFormat(String configXML) throws FileNotFoundException, SAXParseException {

        def xsd = ResourceFinder.locateResourceOnClasspath(this.getClass().getClassLoader(), 'madcow-config.xsd');

        def schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(xsd.inputStream));
        def validator = schema.newValidator();

        // SAXParseExceptions are thrown if it fails xsd validation
        validator.validate(new StreamSource(new StringReader(configXML)));
    }

    /**
     * Parse the configuration xml.
     */
    public void parseConfig(String configXML, String envName) {

        this.stepRunner = null;
        this.stepRunnerParameters = new HashMap<String, String>();

        def configData = new XmlParser().parseText(configXML);
        this.execution = configData.execution[0];

        // get the default execution params and step runner to use
        this.stepRunner = this.execution.runner.type.text();

        if (StringUtils.isEmpty(this.stepRunner))
            throw new Exception("<runner> needs to be specified!");

        NodeList runnerParams = (this.execution.runner as NodeList).parameters as NodeList;
        if ((runnerParams != null) && (!runnerParams.isEmpty())) {
            runnerParams.first().children().each { child ->
                Node node = child as Node;
                if (StringUtils.isEmpty(node.text()))
                    throw new Exception("Runner parameter '${node.name() as String}' defined without content!");
                this.stepRunnerParameters.put(node.name() as String, node.text());
            }
        }

        // get the default environment and use it if none is set
        def defaultEnvironment = this.execution."env.default".text();
        if (defaultEnvironment != null && envName == null) {
            envName = defaultEnvironment;
        }

        //get the default parallel run setting if set
        def parallelText = this.execution."parallel"?.text() ?: '';
        if (parallelText != '') {
            try {
                parallel = !parallelText.equals("false") as boolean;
            } catch (ignored) {
                throw new Exception('Invalid "parallel" field specified - only true or false is allowed');
            }
        }

        // get the default retry count and use it if none is set
        def retriesText = this.execution."retries".text() ?: '1';
        if (retriesText != '') {
            try {
                retryCount = retriesText as int;
            } catch (ignored) {
                throw new Exception('Invalid "retries" specified - only positive integers are allowed');
            }
        }

        // get the default thread count and use it if none is set
        def threadsText = this.execution."threads".text() ?: '';
        if (threadsText != '') {
            try {
                threads = threadsText as int;
            } catch (ignored) {
                throw new Exception('Invalid "threads" specified - only positive integers are allowed');
            }
        }
        
        // setup the environment to use
        this.environment = configData.environments.environment.find {it.'@name' == envName} as Node;
        
        if (this.environment == null && StringUtils.isNotEmpty(envName))
            throw new Exception("Environment '$envName' specified, but not found in config!");
    }

    @Override
    Map toJSON() {

        def environment = this.environment.attributes()

        def environmentChildren = [:];
        this.environment.children()?.each { Node child ->
            environmentChildren[child.name().toString()] = child.text();

            if (child.children()) {
                def environmentGrandchildren = [:];
                child.children()?.each { Node grandchild ->
                    environmentGrandchildren[grandchild.name().toString()] = grandchild.text();
                }

                environmentChildren[child.name().toString()] = environmentGrandchildren;
            }
        }

        environment['children'] = environmentChildren;

        return [
                threads: this.threads,
                retryCount: this.retryCount,
                parallel: this.parallel,
                stepRunner: this.stepRunner,
                stepRunnerParameters: this.stepRunnerParameters.clone(),
                environment: environment
        ]
    }
}