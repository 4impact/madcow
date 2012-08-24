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
class MadcowConfig {

    public Node execution;
    public Node environment;

    public String stepRunner;
    public HashMap<String, String> stepRunnerParameters;

    MadcowConfig(String envName = null) {

        String xmlFileContents = ResourceFinder.locateFilesOnClasspath(this.getClass().getClassLoader(), MadcowProject.CONFIGURATION_FILE).first().text;

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
        
        // setup the environment to use
        this.environment = configData.environments.environment.find {it.'@name' == envName} as Node;
        
        if (this.environment == null && StringUtils.isNotEmpty(envName))
            throw new Exception("Environment '$envName' specified, but not found in config!");
    }

}