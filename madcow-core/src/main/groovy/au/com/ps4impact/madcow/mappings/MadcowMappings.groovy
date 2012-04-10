package au.com.ps4impact.madcow.mappings

import au.com.ps4impact.madcow.MadcowTestCase;

/**
 * Representation of Mappings files, providing static
 * loading of the mappings, and retrieval of a mapping for a particular
 * html element reference.
 *
 * @author Gavin Bunney
 */
class MadcowMappings {

    protected static Map<String, Map<String, String>> mappings;

    /**
     * Initialise the mappings if it hasn't already been.
     */
    protected static initMappings(MadcowTestCase testCase) {
        if (mappings == null) {
            mappings = new MappingsFileHelper().initProcessProperties(testCase);
        }
    }

    /**
     * Get a selector Map from a mapping.
     */
    static Map<String, String> getSelectorFromMapping(MadcowTestCase testCase, String mapping) {

        initMappings(testCase);

        if (mappings.containsKey(mapping)) {
            return mappings.get(mapping).clone() as Map;
        } else {
            return ["${testCase.stepRunner.defaultSelector}" : mapping];
        }
    }

    /**
     * Add a mapping into the mappings collection.
     */
    static addMapping(MadcowTestCase testCase, String key, Map value) {
        initMappings(testCase);
        mappings.put(key, value);
    }
}
