package Model.IntegrationTests;

import Model.ComponentTest;
import org.apache.log4j.Logger;

import java.util.Random;

public class TestCase {

    private ComponentTest depComponentTest;
    private String dependentTestClassVarName;

    private ComponentTest sutComponentTest;
    private String sutTestClassVarName;

    private int testCaseNumber = 0;
    private final static Logger logger = Logger.getLogger(TestCase.class);

    public TestCase(ComponentTest sutComponentTest, ComponentTest depComponentTest) {
        logger.info("==================================");
        Random testNumberGenerator = new Random();
        this.testCaseNumber = testNumberGenerator.nextInt(Integer.MAX_VALUE) + 1;

        this.depComponentTest = depComponentTest;
        this.dependentTestClassVarName = this.depComponentTest.getClassName() + testCaseNumber;

        this.sutComponentTest = sutComponentTest;
        this.sutTestClassVarName = this.sutComponentTest.getClassName() + testCaseNumber;

        logger.info("Create new TestCase");
        logger.info("Name: " + this.dependentTestClassVarName);
        logger.info("Case-No: " + this.testCaseNumber);
        logger.info("==================================");
    }

    public TestCase(ComponentTest componentTest) {
        logger.info("==================================");
        Random testNumberGenerator = new Random();
        this.testCaseNumber = testNumberGenerator.nextInt(Integer.MAX_VALUE) + 1;

        this.depComponentTest = componentTest;
        this.dependentTestClassVarName = this.depComponentTest.getClassName() + testCaseNumber;

        logger.info("Create new TestCase");
        logger.info("Name: " + this.dependentTestClassVarName);
        logger.info("Case-No: " + this.testCaseNumber);
        logger.info("==================================");
    }


    private String getInitClassCode(String varName, String className) {
        return "const " + varName + " = new " + className + "();\n";
    }

    private String getHeader() {
        return "it(\"" + this.depComponentTest.getEvent().getName() + testCaseNumber + "\", async () => {\n";
    }

    private String getSetupCode(String variableName) {
        return "await " + variableName + ".setup();\n";
    }

    private String getArrangeCode(String variableName) {
        return "await " + variableName + ".arrange();\n";
    }

    private String getActPhaseCode(String variableName) {
        return "await " + variableName + ".act()\n";
    }

    private String getAssertCode(String variableName) {
        return "await " + variableName + ".assert();\n";
    }

    public String getErrorAssertCode() {
        return "\nexpect(noError).toBe(true);\n";
    }

    String getTestCaseCode() {
        return  this.getHeader()
                + "let noError = true;\n"
                + "try {\n"
                + this.getInitClassCode(this.dependentTestClassVarName, this.depComponentTest.getClassName())
                + this.getSetupCode(this.dependentTestClassVarName)
                + this.getArrangeCode(this.dependentTestClassVarName)
                + this.getActPhaseCode(this.dependentTestClassVarName)
                + "} catch(err) { \nnoError = false;\n }\n"
                + this.getErrorAssertCode()
                + "})\n";
    }

    public String getTestCaseCoupleTestCode() {
        return  this.getHeader()
                + "let noError = true;\n"
                + "try {\n"
                + this.getInitClassCode(this.dependentTestClassVarName, this.depComponentTest.getClassName())
                + this.getInitClassCode(this.sutTestClassVarName, this.sutComponentTest.getClassName())
                + this.getSetupCode(this.dependentTestClassVarName)
                + this.getSetupCode(this.sutTestClassVarName)
                + this.getArrangeCode(this.dependentTestClassVarName)
                + this.getActPhaseCode(this.dependentTestClassVarName)
                + this.getActPhaseCode(this.sutTestClassVarName)
                + "} catch(err) { \nnoError = false;\n }\n"
                + this.getAssertCode(sutTestClassVarName)
                + this.getErrorAssertCode()
                + "})\n";
    }
}
