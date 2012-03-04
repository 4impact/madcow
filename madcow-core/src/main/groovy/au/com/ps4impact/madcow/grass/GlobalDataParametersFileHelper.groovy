package au.com.ps4impact.madcow.grass;

import org.apache.log4j.Logger
import au.com.ps4impact.madcow.MadcowProject
import au.com.ps4impact.madcow.MadcowTestCase

class GlobalDataParametersFileHelper extends AbstractGrassFileHelper {

	protected static final Logger LOG = Logger.getLogger(GlobalDataParametersFileHelper.class);
    
    Logger getLog(){
        LOG
    }
    
    String getPropertiesFilePrettyName(){
        "Global Data Parameters"
    }
    
    String getFileLocatorFilePattern(){
        return "**/*.data.grass"
    }
    
    String getFileLocatorBasedir() {
        return MadcowProject.TEMPLATES_DIRECTORY;
    }

	Map processProperties(MadcowTestCase testCase, Properties properties) {
		Map mappings = new HashMap<String, String>()
		properties.each {String key, String value ->
            mappings.put(key, value)
		}

		return mappings
	}
}
