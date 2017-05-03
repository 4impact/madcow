package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.MadcowProject
import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * Created by framos on 5/2/2017.
 */
class GenerateGuidewireMappingsFile extends WebDriverBladeRunner{

    private StringBuilder mappings = new StringBuilder()

    //Find all elements that has label and Id for mapping.
    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        try {
            WebElement element = stepRunner.driver.findElement(By.xpath("//div[@id='centerPanel']"))
            List<WebElement> elements = element.findElements(By.xpath("//*[contains(@id,'inputRow')]"))
            elements.each { storeElement(it) }
            if (StringUtils.isEmpty(mappings.toString())) {
                step.result = MadcowStepResult.FAIL("Nothing to map.")
            } else {
                String fileName = MadcowProject.MAPPINGS_DIRECTORY + "/" + step.blade.parameters + ".grass"
                File file = new File(fileName)
                if (file.exists()) {
                    step.result = MadcowStepResult.FAIL("File already exists.")
                } else {
                    write(mappings.toString(), fileName)
                }
            }
        }catch (Exception e){
            step.result = MadcowStepResult.FAIL(e.message)
        }

    }

    /**
     * Get the list of supported parameter types.
     */
    protected List<Class> getSupportedParameterTypes() {
        return [String.class]
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.STATEMENT, GrassBlade.GrassBladeType.EQUATION];
    }

    protected boolean allowEmptyParameterValue() {
        return true;
    }

    protected boolean allowNullSelectorType() {
        return true;
    }

    /**
     * Find all Ids then store them in a String builder.
     * @param element
     */
    private void storeElement(WebElement element){
        String id = element.getAttribute("id").replace("-inputRow","")
        WebElement labelElement = getElement(element, id + "-labelCell")
        String label = ""
        String mapping = ""
        if(labelElement != null){
            label = labelElement.getText().replaceAll("\\W","")
        }
        WebElement input = getElement(element, id + "-inputEl")
        if(input != null){
            mapping = id + "-inputEl"
        }
        if(StringUtils.isNotEmpty(label) && StringUtils.isNotEmpty(mapping)){
            label = toLowerFirstChar(label)
            mappings.append("$label = $mapping");
            mappings.append(System.getProperty("line.separator"));
        }
    }

    /**
     * Convert the first character to lowercase.
     * @param str
     * @return
     */
    private String toLowerFirstChar(String str){
        return Character.toString(Character.toLowerCase(str.charAt(0))) + str.substring(1);
    }

    /**
     * Find the element then ignore if not found.
     * @param element
     * @param id
     * @return
     */
    private WebElement getElement(WebElement element, String id){
        try{
           return element.findElement(By.xpath("//*[@id='$id']"))
        }catch (Exception e){return null}
    }

    /**
     * This will write the mappings in a file.
     * @param content
     * @param filename
     */
    public void write(String content, String filename) {

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(filename);
            bw = new BufferedWriter(fw);
            bw.write(content);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                IOUtils.closeQuietly(bw, fw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
