package Model;

import java.util.ArrayList;
import java.util.List;

public class ComponentTest {

    private String testArrange;
    private String testAct;
    private String testAssert;
    private String ComponentTestFilepath;
    private String ComponentTestCompleteSourceCode;

    private List<String> nextTest = new ArrayList<>();
    private List<String> previousTest = new ArrayList<>();

    public String getTestArrange() {
        return testArrange;
    }

    public void setTestArrange(String testArrange) {
        this.testArrange = testArrange;
    }

    public String getTestAct() {
        return testAct;
    }

    public void setTestAct(String testAct) {
        this.testAct = testAct;
    }

    public String getTestAssert() {
        return testAssert;
    }

    public void setTestAssert(String testAssert) {
        this.testAssert = testAssert;
    }

    public String getComponentTestFilepath() {
        return ComponentTestFilepath;
    }

    public void setComponentTestFilepath(String componentTestFilepath) {
        ComponentTestFilepath = componentTestFilepath;
    }

    public String getComponentTestCompleteSourceCode() {
        return ComponentTestCompleteSourceCode;
    }

    public void setComponentTestCompleteSourceCode(String componentTestCompleteSourceCode) {
        ComponentTestCompleteSourceCode = componentTestCompleteSourceCode;
    }

    public List<String> getNextTest() {
        return nextTest;
    }

    public void setNextTest(List<String> nextTest) {
        this.nextTest = nextTest;
    }

    public List<String> getPreviousTest() {
        return previousTest;
    }

    public void setPreviousTest(List<String> previousTest) {
        this.previousTest = previousTest;
    }
}
