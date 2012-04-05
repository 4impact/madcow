package au.com.ps4impact.madcow

import org.apache.commons.lang3.StringUtils;

/**
 * A collection of madcow test cases.
 */
class MadcowTestSuite {

    String name;
    ArrayList<MadcowTestCase> testCases;
    MadcowTestSuite parent;
    ArrayList<MadcowTestSuite> children;

    MadcowTestSuite(String name, MadcowTestSuite parent = null, ArrayList<MadcowTestCase> testCases = new ArrayList<MadcowTestCase>()) {
        this.name       = name;
        this.testCases  = testCases;
        this.parent     = parent;
        this.children   = new ArrayList<MadcowTestSuite>();
    }

    public int size() {
        return getTestCasesRecusively().size();
    }

    public String fullyQualifiedName() {
        String parentFQN = parent != null ? parent.fullyQualifiedName() : '';
        return parentFQN != '' ? "${parentFQN}.${name}" : name;
    }

    public String toString() {
        return fullyQualifiedName();
    }

    /**
     * Get all children and children's children children children's children...
     */
    public ArrayList<MadcowTestCase> getTestCasesRecusively() {

        ArrayList<MadcowTestCase> allTestCases = new ArrayList<MadcowTestCase>()

        if (testCases.size() > 0)
            allTestCases.addAll(testCases);

        children.each { child ->
            def childTestCases = child.getTestCasesRecusively();
            if (childTestCases.size() > 0)
                allTestCases.addAll(childTestCases);
        }

        return allTestCases;
    }

    /**
     * Locate a suite for the full qualified test name.
     */
    public MadcowTestSuite locateSuite(String fullyQualifiedTestName) {

        if (!fullyQualifiedTestName.contains("."))
            return this;

        // get all the packages, excluding the test name
        String fqSuiteName = StringUtils.left(fullyQualifiedTestName, fullyQualifiedTestName.lastIndexOf("."));

        if (this.name == fqSuiteName)
            return this;

        for (child in children) {
            if (child.fullyQualifiedName() == fqSuiteName)
                return child;

            def childOfChild = child.locateSuite(fullyQualifiedTestName);
            if (childOfChild != null)
                return childOfChild;
        }

        return null;
    }
}