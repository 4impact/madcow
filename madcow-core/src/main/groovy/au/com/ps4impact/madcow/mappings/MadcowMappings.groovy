package au.com.ps4impact.madcow.mappings;

/**
 * Representation of Mappings files, providing static
 * loading of the mappings, and retrieval of a mapping for a particular
 * html element reference.
 */
class MadcowMappings {

    static Map mappings;

    static {
        def mappingsHelper = new MappingsFileHelper()
        mappings = mappingsHelper.initProcessProperties()
    }

    static Map<String, String> getMappedHtmlElementReference(String htmlElementReference) {
        if (mappings.containsKey(htmlElementReference)) {
            return mappings.get(htmlElementReference).clone() as Map
        } else {
            return ['htmlId': htmlElementReference]
        }
    }
}
