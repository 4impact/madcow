package au.com.ps4impact.madcow.config


/**
 *
 */
class MadcowConfig {

    static Node ConfigData;
    static String StepRunner = "au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner";

    public static void parseConfig(String configXML) {
        ConfigData = new XmlParser().parseText(configXML);
        StepRunner = ConfigData.execution.runner.text();
        // TODO - validate config data
    }
}
