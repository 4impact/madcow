package au.com.ps4impact.madcow.util;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;

/**
 * Resource Finder utility to locate resources on the classpath.
 */
class ResourceFinder {

    /**
     * Locate a single resource on the classpath for the given pattern
     */
    static Resource locateResourceOnClasspath(ClassLoader classLoader, String resourcePattern) throws Exception {

        def resources = locateResourcesOnClasspath(classLoader, resourcePattern);
        if (resources.size() > 1)
            throw new Exception("Multiple resource matches found for $resourcePattern");

        return resources.first();
    }

    /**
     * Locate a set of resources on the classpath for the given pattern.
     */
    static Resource[] locateResourcesOnClasspath(ClassLoader classLoader, String resourcePattern) throws FileNotFoundException, Exception{

        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver(classLoader);
        Resource[] resources = resourceLoader.getResources("classpath*:$resourcePattern");

        if (resources.size() == 0)
            throw new FileNotFoundException("Unable to find matches on the classpath for $resourcePattern");
        
        return resources;
    }
}
