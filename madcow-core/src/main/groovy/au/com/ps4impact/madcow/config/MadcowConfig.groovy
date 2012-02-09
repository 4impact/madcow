package au.com.ps4impact.madcow.config


/**
 *  The MadcowConfig class which holds all the initialisation information
 */
class MadcowConfig {

    public Node execution;
    public Node environment;

    public String stepRunner;

    MadcowConfig(String envName = null, String configPath = null)
    {
        //read in file
        def xmlFile = new File('./madcow-core/src/resources/project-template/conf/madcow-config.xml');
        //parse xml config file
        this.parseConfig(xmlFile.text, envName);
    }

    protected void parseConfig(String configXML, String envName) {
        def configData = new XmlParser().parseText(configXML);
        //get the default execution params and step runner to use
        this.execution = configData.execution[0];
        this.stepRunner = this.execution.runner.text();
        //get the default environment and use it if none is set
        def defaultEnvironment = this.execution."default.env".text()
        if (defaultEnvironment!=null
            && envName == null)
        {
            envName = defaultEnvironment;
        }
        //setup the environment to use
        this.environment = configData.environments.environment.find{it.'@name'== envName} as Node;

        // TODO - validate config data
    }

}
