package au.com.ps4impact.madcow.grass;

import org.apache.log4j.Logger
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * Abstract class for properties file parsing, for global data parameters and mappings files.
 */
abstract class AbstractGrassFileHelper {

    abstract Logger getLog()

    abstract String getPropertiesFilePrettyName()

    abstract String getResourcePatternMatchingClasspath()

    abstract Map processProperties(Properties properties)

    Map initProcessProperties() {
        Properties localProperties = new Properties();

        getAllMappingsFromClasspath().each {Resource r ->
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
        return processProperties(localProperties)
    }

    def getAllMappingsFromClasspath(ClassLoader classLoader = ClassLoader.getSystemClassLoader()){
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver(classLoader)
        Resource[] resources = resourceLoader.getResources(getResourcePatternMatchingClasspath())
        resources
    }

    List duplicateProperties(Properties baseProperties, Properties propertiesToCompare){
        def duplicateProperties = []
        propertiesToCompare.each { key, value ->
            if (baseProperties.getProperty(key as String)){
                duplicateProperties.add key
            }
        }

        duplicateProperties
    }

    def loadMappingProperties(Resource resource){
        Properties properties = new Properties()
        properties.load(resource.getInputStream())
        properties
    }
}
