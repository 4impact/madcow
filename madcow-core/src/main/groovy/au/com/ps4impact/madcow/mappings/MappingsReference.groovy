package au.com.ps4impact.madcow.mappings

import au.com.ps4impact.madcow.MadcowTestCase
import groovy.text.GStringTemplateEngine
import org.springframework.core.io.Resource
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import au.com.ps4impact.madcow.util.ResourceFinder
import org.apache.commons.io.FileUtils
import au.com.ps4impact.madcow.MadcowProject
import org.apache.log4j.Logger

/**
 * 
 *
 * @author: Gavin Bunney
 */
class MappingsReference {

    protected static final Logger LOG = Logger.getLogger(MappingsReference.class);
    protected MappingsFileHelper mappingsFileHelper = new MappingsFileHelper();

    protected File rootOutputDirectory = new File(MadcowProject.MAPPINGS_REFERENCE_DIRECTORY);
    protected File mappingsOutputDirectory = new File("${MadcowProject.MAPPINGS_REFERENCE_DIRECTORY}/mappings");

    public void generate(MadcowTestCase stubTestCase) {

        def resources = mappingsFileHelper.getAllMappingsFromClasspath();
        def mappingsMap = [:]
        def menuMap = [:]
        resources.each { resource ->
            Properties properties = new Properties()
            properties.load(resource.getInputStream())
            mappingsFileHelper.applyMappingNamespace(resource, properties)
            appendTreeStructureToMappings(resource, properties, mappingsMap)
            appendTreeStructureToMappings(resource, properties, menuMap, true)
        }

        if (rootOutputDirectory.exists())
            rootOutputDirectory.delete();
        rootOutputDirectory.mkdirs();
        mappingsOutputDirectory.mkdirs();

        // generate the menu tree structure using the menu template
        def menuTemplateParams = [menuItems : generateMenuItems(menuMap) ]
        def templateEngine = new GStringTemplateEngine()
        def menuHtml = templateEngine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'reference/menu.gtemplate').URL).make(menuTemplateParams);
        new File("${rootOutputDirectory.canonicalPath}/menu.html").text = menuHtml.toString();

        walkTheMappingsTableStyle(mappingsMap)

        // process the index and about page templates
        def indexHtml = templateEngine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'reference/index.gtemplate').URL).make();
        new File("${rootOutputDirectory.canonicalPath}/index.html").text = indexHtml.toString();
        def aboutHtml = templateEngine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'reference/about.gtemplate').URL).make();
        new File("${rootOutputDirectory.canonicalPath}/about.html").text = aboutHtml.toString();

        // copy the assets if they are available
        File assetsDir = new File('./.madcow/assets/mappings-reference');
        if (assetsDir.exists()) {
            LOG.info("Copying assets for Madcow Report...");
            FileUtils.copyDirectory(assetsDir, new File("${rootOutputDirectory.canonicalPath}/resources"));
        } else {
            LOG.warn("No assets found for Madcow Report...");
        }
    }

    def appendTreeStructureToMappings(Resource resource, Properties properties, def mappings, boolean generateMenu = false) {

        // create initialised nested map structure
        properties.each { prop ->
            def key = removeMappingKeywords(prop.key)
            def keyTokens = key.tokenize('_')
            def keyBuilder
            keyTokens.each { token ->
                if (!keyBuilder){
                    keyBuilder = token
                }
                else {

                    if (Eval.x(mappings, "if (!(x.$keyBuilder instanceof Map)) return true else return false"))
                        return

                    keyBuilder += ".$token"
                }
                Eval.x mappings, "if (!x.$keyBuilder) x.$keyBuilder=[:]"
            }
        }

        // populate map structure using Eval resulting in the executation lines such as
        // mappings.testsite.create = testsite_create_button1
        properties.each { prop ->
            def key
            if (!generateMenu) {
                key = removeMappingKeywords(prop.key)
            } else {
                String cleanKey = removeMappingKeywords(prop.key)
                key = cleanKey.substring(0, cleanKey.lastIndexOf('_') > 0 ? cleanKey.lastIndexOf('_') : cleanKey.length())
            }

            def mapKey = key.replace('_', '.')
            def value = "${key}"

            if (!generateMenu)
                Eval.xy mappings, value, "x.$mapKey=y"
            else
                Eval.x mappings, "x.$mapKey='mappings/${key}.html'"
        }
    }

    def removeMappingKeywords(def key){
//        key = key.replace('.xpath', '')
//        key = key.replace('.htmlId', '')
//        key = key.replace('.name', '')
//        key = key.replace('.href', '')
//        key

        return StringUtils.substringBefore(key, '.');
    }

    def generateMenuItems(def menuMap) {

        def list = []
        walkTheMappingsMenuStyle(menuMap, list)

        def html = ""
        list.each { line -> html += "$line \n" }
        return html
    }

    static String deCamelCase(String s) {

        String[] camelCaseSplit = StringUtils.splitByCharacterTypeCamelCase(s)
        String deCamel = ''
        camelCaseSplit.eachWithIndex { word, idx ->
            deCamel += idx == 0 ? StringUtils.capitalize(word) : " $word"
        }
        return deCamel
    }

    def walkTheMappingsMenuStyle(def mappings, def htmlList = [], def i = 1){

        mappings.each { key, value ->

            key = deCamelCase(key)

            if (value instanceof String || value instanceof GString){
                htmlList.add "{'$key' : '$value'},"
            } else {
                i++
                htmlList.add "{'$key' : ["
                walkTheMappingsMenuStyle(value, htmlList, i)
                htmlList.add "]},"
            }
            i--
        }
    }

    def walkTheMappingsTableStyle(def mappings, String filename = '', String tableOfContents = '', String body = '', def i = 1){
        boolean creatingNewFile = false

        mappings = mappings.sort()
        mappings.each { key, value ->

            if (value instanceof String || value instanceof GString) {

                String readableKey = deCamelCase(key)

                if (!creatingNewFile) {
                    creatingNewFile = true
                }

                tableOfContents += "<li><a href=\"#${key}\">$readableKey</a></li>\n"
                body += "<h2 id=\"${key}\">$readableKey</h2>\n"
                body += "<p>$value</p>"
            } else {
                i++
                filename += filename != '' ? "_$key" : key
                filename = walkTheMappingsTableStyle(value, filename, '', body, i)
            }
            i--
        }

        if (creatingNewFile) {

            // make a friendly breadcrumb esq title; testsite_create becomes Testsite >> Create
            def pageTitle = WordUtils.capitalize(filename.replaceAll('_', ' &#187; '))

            def templateParams = [title : pageTitle,
                    tableOfContents : tableOfContents,
                    body : body]
            def templateEngine = new GStringTemplateEngine()
            def templateHtml = templateEngine.createTemplate(ResourceFinder.locateResourceOnClasspath(this.class.classLoader, 'reference/mapping.gtemplate').URL).make(templateParams);
            new File("${mappingsOutputDirectory.canonicalPath}/${filename}.html").text = templateHtml
        }

        // chop off the last part of the filename, so if there are more maps in the parent it wont keep
        // appending their siblings
        def lastIdx = filename.lastIndexOf('_')
        filename = filename.substring(0, lastIdx > 0 ? lastIdx : 0 )
        return filename
    }
}
