package au.com.ps4impact.madcow.runner.webdriver.blade

import au.com.ps4impact.madcow.grass.GrassBlade
import au.com.ps4impact.madcow.runner.webdriver.WebDriverBladeRunner
import au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner
import au.com.ps4impact.madcow.step.MadcowStep

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import java.math.RoundingMode

/**
 * This will allow math operations
 * Created by framos on 4/27/2017.
 */
class EvalMath extends WebDriverBladeRunner{

    //Total value will be stored with a key of param.name
    public void execute(WebDriverStepRunner stepRunner, MadcowStep step) {
        Map<String, String> param = step.blade.parameters
        step.testCase.grassParser.setDataParameter("${GrassBlade.DATA_PARAMETER_KEY}${param.name}", eval(convertVarsToValue(param.value.toString(), step)).toString())
    }

    /**
     * Get the list of supported parameter types, which for table operations is a map and a string
     */
    protected List<Class> getSupportedParameterTypes() {
        return [Map.class]
    }

    //evaluate the string as math operations
    private BigDecimal eval(String str){
        ScriptEngineManager mgr = new ScriptEngineManager()
        ScriptEngine engine = mgr.getEngineByName("JavaScript")
        return new BigDecimal(engine.eval(str)).setScale(2, RoundingMode.CEILING)
    }

    protected boolean allowEmptyParameterValue() {
        return true;
    }

    protected boolean allowNullSelectorType() {
        return true;
    }

    protected Collection<GrassBlade.GrassBladeType> getSupportedBladeTypes() {
        return [GrassBlade.GrassBladeType.STATEMENT, GrassBlade.GrassBladeType.EQUATION];
    }

    //This will convert variables to their values.
    private String convertVarsToValue(String str, MadcowStep step){
        String[] values = str.split(" ")
        StringBuilder builder = new StringBuilder()
        values.each {
            if(isValidJavaIdentifier(it)){
                String v = step.testCase.grassParser.getDataParameter("${GrassBlade.DATA_PARAMETER_KEY}${it}")
                builder.append("$v ")
            }else{
                builder.append("$it ")
            }
        }
        return builder.toString()
    }

    //checks if the entered variable is valid within string
    public static boolean isValidJavaIdentifier(String s) {
        if (s.isEmpty()) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(s.charAt(0))) {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
