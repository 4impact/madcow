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
        this.parseConfig(xmlFile.text);
    }

    protected void parseConfig(String configXML) {
        def configData = new XmlParser().parseText(configXML);

        this.execution = configData.execution[0];
        this.stepRunner = this.execution.runner.text();

        this.environment = configData.environments.environment.find{it.'@name'=="DEV"} as Node;
        // TODO - validate config data
    }

}
