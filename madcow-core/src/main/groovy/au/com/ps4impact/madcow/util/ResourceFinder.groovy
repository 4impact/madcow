package au.com.ps4impact.madcow.util;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource
import org.apache.commons.lang3.StringUtils;

/**
 * Resource Finder utility to locate resources on the classpath.
 */
class ResourceFinder {

    /**
     * Locate a single file on the classpath for the given pattern.
     * This delegates to the locateResourceOnClasspath, but returns the file handle.
     */
    static File locateFileOnClasspath(ClassLoader classLoader, String resourcePattern, String basedir = '') throws Exception {

        def resource = locateResourceOnClasspath(classLoader, resourcePattern, basedir);
        
        try {
            if (resource.file.exists())
                return resource.file;
        } catch (e) { }
        
        throw new Exception("Unable to find a file on the classpath for '$resourcePattern'");
    }

    /**
     * Locate a single resource on the classpath for the given pattern
     */
    static Resource locateResourceOnClasspath(ClassLoader classLoader, String resourcePattern, String basedir = '') throws Exception {

        def resources = locateResourcesOnClasspath(classLoader, resourcePattern, basedir);
        if (resources.size() > 1)
            throw new Exception("Multiple resource matches found for '$resourcePattern'");

        return resources.first();
    }

    /**
     * Locate a set of files on the classpath for the given pattern.
     * This delegates to the locateResourcesOnClasspath, but returns the file handles.
     */
    static List<File> locateFilesOnClasspath(ClassLoader classLoader, String resourcePattern, String basedir = '') throws FileNotFoundException, Exception {

        Resource[] resources = locateResourcesOnClasspath(classLoader, resourcePattern, basedir);
        resources = resources.findAll { Resource r ->
            try {
                if (r.file.exists())
                    return true;
            } catch (e) {
                return false // not a file
            }
        }

        if (resources.size() == 0)
            throw new FileNotFoundException("Unable to find file matches on the classpath for '$resourcePattern'");

        return resources.collect { resource -> resource.file };
    }

    /**
     * Locate a set of resources on the classpath for the given pattern.
     */
    static Resource[] locateResourcesOnClasspath(ClassLoader classLoader, String resourcePattern, String basedir = '') throws FileNotFoundException, Exception {

        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver(classLoader);
        Resource[] resources = resourceLoader.getResources("classpath*:${StringUtils.isEmpty(basedir) ? '' : "$basedir/"}$resourcePattern");

        if (resources.size() == 0)
            throw new FileNotFoundException("Unable to find matches on the classpath for '$resourcePattern'");

        return resources;
    }

    /**
     * Add the given extension to the filename if it doesn't end with that extension.
     */
    static String addFileExtensionIfRequired(String filename, String extension) {
        return filename + (filename.endsWith(extension) ? '' : extension.startsWith('.') ? extension : ".$extension");
    }
}
