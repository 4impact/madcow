package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep
import au.com.ps4impact.madcow.step.MadcowStepResult
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import org.apache.commons.lang3.StringUtils

/**
 * Usage:
 * 1. waitUntilExists = [value:'textToFind', seconds:'5']
 * or
 * 2. MappingId.waitUntilExists = 10
 */
class WaitUntilExists extends WebDriverBladeRunner{

    private int timeout = 0

    /**
     * Wait for the element to show within specified time.
     * @param stepRunner
     * @param step
     */
    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        try{
            findElementInPage(stepRunner, step)

        }catch (Exception e){
            step.result = MadcowStepResult.FAIL("Element not found within specified timeout of ${timeout} seconds.")
        }

    }

    /**
     * Allow verifying the element is empty.
     */
    protected boolean allowEmptyParameterValue() {
        return true;
    }

    /**
     * Allow null selectors.
     */
    protected boolean allowNullSelectorType() {
        return true;
    }

    /**
     * Supported types of blades.
     */
    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.EQUATION];
    }

    protected List<Class> getSupportedParameterTypes() {
        return [String.class, Map.class]
    }

    /**
     * Actual search and wait implementation.
     * @param stepRunner
     * @param id
     * @param txt
     * @param timeout
     */
    private void waitTilExists(WebDriverStepRunner stepRunner, String id, String txt, int timeout){
        String xpath = ""
        if(StringUtils.isEmpty(id)){
            xpath = "//*[text() = '$txt']"
        }else{
            xpath = "//*[@id='$id']"
        }
        WebDriverWait wait = new WebDriverWait(stepRunner.driver, timeout, 2000);
        wait.until( new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.xpath(xpath)) != null
            }
        })
    }

    /**
     * Determine if the script has id and value in it.
     * @param stepRunner
     * @param step
     */
    private void findElementInPage(WebDriverStepRunner stepRunner, MadcowStep step){
        String id = step.blade.mappingSelectorValue != null ? step.blade.mappingSelectorValue : ""
        String txt = ""
        int sec = 0
        if (Map.class.isInstance(step.blade.parameters)) {
            Map<String, String> map = step.blade.parameters as Map
            sec = map.seconds != null ? Integer.parseInt(map.seconds) : 0
            timeout = sec
            txt = map.value != null ? map.value : ""
            if(StringUtils.isEmpty(map.seconds) || StringUtils.isEmpty(map.value)){
                step.result = MadcowStepResult.FAIL("seconds and value are required in a Map parameter. waitUntilExists =[seconds:'', value:'' ]")
            }else{
                waitTilExists(stepRunner,null,txt, sec)
            }
        }else{
            sec = step.blade.parameters as int
            timeout = sec
            if(StringUtils.isEmpty(id) || StringUtils.isEmpty(step.blade.parameters)){
                step.result = MadcowStepResult.FAIL("mapping is required. Page_element.waitUntilExists = 2")
            }else{
                waitTilExists(stepRunner,id,null, sec)
            }
        }
    }


}
