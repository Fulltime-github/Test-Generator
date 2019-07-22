package Model.IntegrationTests;

import Model.Component;
import Model.ComponentTest;
import org.apache.log4j.Logger;

public class IntegrationTestWeakOracle extends BaseIntegrationTest {

    final static Logger logger = Logger.getLogger(IntegrationTestWeakOracle.class);

    public IntegrationTestWeakOracle(Component systemUnderTest, Component dependentComponent) {
        super(systemUnderTest, dependentComponent);
    }

    /**
     * Test Cases are only coupling two ComponentTests
     * @param dependentComponentTest
     */
    public void addTestCase(ComponentTest dependentComponentTest) {
        TestCase newTestCase = new TestCase(dependentComponentTest);

        //Dependencies
        this.addDependency("../spec/", dependentComponentTest.getComponentTestFilename() , dependentComponentTest.getClassName());
        this.addWholeDependencyStatement(dependentComponentTest.getTestUtilDependency());
        testCases.add(newTestCase);
        System.out.println(newTestCase.getTestCaseCode());
    }

    private String getAllTestCasesCode() {
        StringBuilder allTestCasesCode = new StringBuilder();
        for (TestCase testCase:this.testCases) {
            allTestCasesCode.append(testCase.getTestCaseCode());
        }
        return allTestCasesCode.toString();
    }

    public String getIntegrationTestCode() {
        return this.getAllDependenciesCode() +
                this.getTestSuiteHeaderCode() +
                this.getSetupCode(this.sutComponent) +
                this.getTeardownCode() +
                this.getAllTestCasesCode() +
                "});";
    }
}
