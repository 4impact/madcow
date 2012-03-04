package au.com.ps4impact.madcow.mappings

import au.com.ps4impact.madcow.MadcowTestCase;

/**
 * Representation of Mappings files, providing static
 * loading of the mappings, and retrieval of a mapping for a particular
 * html element reference.
 */
class MadcowMappings {

    protected static Map mappings;

    static Map<String, String> getSelectorFromMapping(MadcowTestCase testCase, String mapping) {

        if (mappings == null) {
            mappings = new MappingsFileHelper().initProcessProperties(testCase);
        }

        if (mappings.containsKey(mapping)) {
            return mappings.get(mapping).clone() as Map;
        } else {
            return ["${testCase.stepRunner.defaultSelector}" : mapping];
        }
    }
}
