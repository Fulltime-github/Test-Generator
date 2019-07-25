package Model;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ComponentTest {
    private final static Logger logger = Logger.getLogger("Generator");
    private String testArrange;
    private String testAct;
    private String testAssert;
    private String componentTestFilepath;
    private String componentTestFilename;
    private String componentTestSourceCode;
    private String className;
    private Event event;
    private String testUtilDependency;
    private String renderModelDependency;

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
        return componentTestFilepath;
    }

    public void setComponentTestFilepath(String componentTestFilepath) {
        this.componentTestFilepath = componentTestFilepath;
    }

    public String getComponentTestSourceCode() {
        return componentTestSourceCode;
    }

    public void setComponentTestSourceCode(String componentTestSourceCode) {
        this.componentTestSourceCode = componentTestSourceCode.replaceAll("\n", ""   ).replaceAll("\\s+","");
        this.analyseSourceCode();
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

    private void analyseSourceCode() {
        this.setClassName(this.findClassName());
        this.setTestUtilDependency(this.findDependency("TestUtils"));
        this.setRenderModelDependency(this.findDependency("RenderModel"));
        logger.info("Found Test Classname: " + this.getClassName());
        logger.info("Found Test Util Dependency: " + this.getTestUtilDependency());
    }

    private String findClassName() {
        String startClassName = this.componentTestSourceCode.substring(this.componentTestSourceCode.indexOf("class")+"class".length());
        int endClassNamePos = 0;
        try {
            endClassNamePos = startClassName.indexOf("implements");
        } catch (Exception e) {
            endClassNamePos = startClassName.indexOf("{");
        }
        return startClassName.substring(0,endClassNamePos);
    }

    private String findDependency(String dependencyClassName) {
        String startTestUtilsDependency = this.componentTestSourceCode.substring(this.componentTestSourceCode.indexOf("import{"+ dependencyClassName + "}"));
        return startTestUtilsDependency.substring(0, startTestUtilsDependency.indexOf(";")+1);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getTestUtilDependency() {
        return testUtilDependency;
    }

    public void setTestUtilDependency(String testUtilDependency) {
        this.testUtilDependency = testUtilDependency;
    }

    public String getComponentTestFilename() {
        return componentTestFilename;
    }

    public void setComponentTestFilename(String componentTestFilename) {
        this.componentTestFilename = componentTestFilename;
    }

    public String getRenderModelDependency() {
        return renderModelDependency;
    }

    public void setRenderModelDependency(String renderModelDependency) {
        this.renderModelDependency = renderModelDependency;
    }
}
