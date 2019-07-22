package Model.IntegrationTests;

import Model.Component;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class BaseIntegrationTest {
    private final static Logger logger = Logger.getLogger(BaseIntegrationTest.class);

    List<String> testDependencies = new ArrayList<>();
    List<TestCase> testCases = new ArrayList<>();
    Component sutComponent;
    Component depComponent;
    String filepath = "";
    String fileName = "";
    int testNumber = 0;

    BaseIntegrationTest(Component sutComponent, Component depComponent) {
        logger.info("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°");
        Random testNumberGenerator = new Random();
        this.testNumber = testNumberGenerator.nextInt(Integer.MAX_VALUE) + 1;

        this.sutComponent = sutComponent;
        this.depComponent = depComponent;

        this.fileName = "Integrationtest-" + sutComponent.getName() + "-" + depComponent.getName() + "-" + testNumber + ".ts";
        this.filepath = depComponent.getComponentSourceCodeFilepath().substring(0, depComponent.getComponentSourceCodeFilepath().lastIndexOf("\\"));

        logger.info("new Integrationtest");
        logger.info("Name: " + this.sutComponent.getName() + "-" + this.depComponent.getName() + testNumber);
        logger.info("Integrationtest-Number: " + this.testNumber);
        logger.info("Filename: " + this.fileName);
        logger.info("FilePath: " + this.filepath);
        logger.info("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°");
    }

    String getTestSuiteHeaderCode() {
        return "\n\ndescribe(\" Integration - " + this.sutComponent.getName() + "-" + this.depComponent.getName() + testNumber + "\", () => {\n\n";
    }

    String getTeardownCode() {
        return "afterEach(async () => {\n"
                + "\tTestUtils.close();\n"
                + "});\n\n";
    }

    void addClassDependency(String className, String dependencyFilePath, String dependencyFilename) {
        for (String testDependency:testDependencies) {
            if(testDependency.contains(className)) {
                return;
            }
        }
        testDependencies.add("import{" + className + "} from '"+ dependencyFilePath + dependencyFilename + "'\n");
    }


    void addDependency(String testFilePath, String filename, String className) {
        String sutComponentTestFilename = filename.substring(0,filename.indexOf(".ts"));
        this.addClassDependency(className, testFilePath, sutComponentTestFilename);
    }

    void addWholeDependencyStatement(String dependencyStatement) {
        testDependencies.add(dependencyStatement + "\n");
    }

    String getAllDependenciesCode() {
        StringBuilder allDependencyCode = new StringBuilder();
        for (String dependency:this.testDependencies) {
            allDependencyCode.append(dependency);
        }
        return allDependencyCode.toString();
    }

    String getSetupCode(Component systemUnderTest) {
        //Check if inizialization is correct zugewiesen sollte sowas sein wie: BlueBuy.tag, {sku: "t_eicher"});
        return "beforeEach(async () => {\n"
                + "\tawait TestUtils.addRenderHtml(\"" + systemUnderTest.getComponentInitializationHtml() + "\", \"" + systemUnderTest.getName() + "\");\n"
                + "});\n\n";
    }

    String getInitClassCode(String varName, String className) {
        return "const " + varName + " = new " + className + "();\n";
    }

    public void printToFile(String content) {
        System.out.println(this.filepath  + "\\integrationtest\\" + this.fileName);
        File directory = new File(this.filepath  + "\\integrationtest\\");
        if (! directory.exists()){
            directory.mkdirs();
        }

        File file = new File(this.filepath  + "\\integrationtest\\" + this.fileName);
        //Write Content
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTestNumber() {
        return testNumber;
    }

    public boolean hasTestCases() {
        return this.testCases.size() > 0;
    }
}
