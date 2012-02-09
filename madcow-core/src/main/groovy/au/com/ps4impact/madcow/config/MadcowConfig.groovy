package au.com.ps4impact.madcow.config


/**
 *  The MadcowConfig class which holds all the initialisation information
 */
class MadcowConfig {

    static Node ConfigData;
    static Node EnvironmentData;
    static String StepRunner //= "au.com.ps4impact.madcow.runner";

    MadcowConfig(String envName = null, String configPath = null)
    {
        //read in file
        def xmlFile = new File('./madcow-core/src/resources/project-template/conf/madcow-config.xml');
        //parse xml config file
        this.parseConfig(xmlFile.text);
    }

    public static void parseConfig(String configXML) {
        ConfigData = new XmlParser().parseText(configXML);
        EnvironmentData = ConfigData.environments.environment.find{it.'@name'=="DEV"}
        StepRunner = ConfigData.execution.runner.text();
        // TODO - validate config data
    }

}
