package au.com.ps4impact.madcow.grass;

import org.apache.log4j.Logger
import org.springframework.core.io.Resource
import au.com.ps4impact.madcow.util.ResourceFinder
import au.com.ps4impact.madcow.MadcowTestCase

/**
 * Abstract class for properties file parsing, for global data parameters and mappings files.
 */
abstract class AbstractGrassFileHelper {

    abstract Logger getLog()

    abstract String getPropertiesFilePrettyName()

    abstract String getFileLocatorFilePattern();
    abstract String getFileLocatorBasedir();

    abstract Map processProperties(MadcowTestCase testCase, Properties properties)

    Map initProcessProperties(MadcowTestCase testCase) {
        Properties localProperties = new Properties();

        Resource[] grassFiles;
        try {
            grassFiles = ResourceFinder.locateResourcesOnClasspath(ClassLoader.getSystemClassLoader(), getFileLocatorFilePattern(), getFileLocatorBasedir());
        } catch (FileNotFoundException fnfe) {
            getLog().info('No files found to process');
            return [:];
        }

        grassFiles.each { Resource r ->
            def properties = loadMappingProperties(r)
            getLog().debug "Parsing ${getPropertiesFilePrettyName()} File [${r.URL}] "

            List duplicateProperties = duplicateProperties(localProperties, properties)
            if (duplicateProperties.size() > 0){
                def e = new RuntimeException("${getPropertiesFilePrettyName()} File [${r.URL}] contains duplicates [$duplicateProperties] found in a previous ${getPropertiesFilePrettyName()} file")
                getLog().error(e)
                throw e
            }

            localProperties.putAll(properties)
        }
        return processProperties(testCase, localProperties)
    }

    List duplicateProperties(Properties baseProperties, Properties propertiesToCompare) {
        def duplicateProperties = []
        propertiesToCompare.each { key, value ->
            if (baseProperties.getProperty(key as String)){
                duplicateProperties.add key
            }
        }

        duplicateProperties
    }

    def loadMappingProperties(Resource resource) {
        Properties properties = new Properties()
        properties.load(resource.getInputStream())
        properties
    }
}
