package au.com.ps4impact.madcow.grass;

import org.apache.log4j.Logger
import au.com.ps4impact.madcow.MadcowProject

class GlobalDataParametersFileHelper extends AbstractGrassFileHelper {

	protected static final Logger LOG = Logger.getLogger(GlobalDataParametersFileHelper.class);
    
    Logger getLog(){
        LOG
    }
    
    String getPropertiesFilePrettyName(){
        "Global Data Parameters"
    }
    
    String getResourcePatternMatchingClasspath(){
        "classpath*:${MadcowProject.TEMPLATES_DIRECTORY}/**/*.data.grass"
    }    

	Map processProperties(Properties properties) {
		Map mappings = new HashMap<String, String>()
		properties.each {String key, String value ->
            mappings.put(key, value)
		}

		return mappings
	}
}
