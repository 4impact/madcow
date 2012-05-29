package au.com.ps4impact.madcow.mappings

import au.com.ps4impact.madcow.MadcowTestCase
import au.com.ps4impact.madcow.mock.MockMadcowConfig

/**
 * 
 *
 * @author: Gavin Bunney
 */
class MappingsReferenceTest extends GroovyTestCase {

    void testTestIt() {
        MadcowTestCase stubTestCase = new MadcowTestCase('MappingsReference', MockMadcowConfig.getMadcowConfig(), []);

        println(new MappingsFileHelper().initProcessProperties(stubTestCase));

        new MappingsReference().generate(stubTestCase);
    }
}
