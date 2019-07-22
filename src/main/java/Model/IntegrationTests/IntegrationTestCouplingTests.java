package Model.IntegrationTests;

import Model.Component;
import Model.ComponentTest;
import org.apache.log4j.Logger;

public class IntegrationTestCouplingTests extends BaseIntegrationTest {

    final static Logger logger = Logger.getLogger(IntegrationTestCouplingTests.class);

    public IntegrationTestCouplingTests(Component systemUnderTest, Component dependentComponent) {
        super(systemUnderTest, dependentComponent);
    }

    /**
     * Test Cases are only coupling two ComponentTests
     * @param sutComponentTest
     * @param depComponentTest
     */
    public void addTestCaseCoupleTest(ComponentTest sutComponentTest, ComponentTest depComponentTest) {
        TestCase newTestCase = new TestCase(sutComponentTest, depComponentTest);

        //Dependencies
        this.addDependency("../spec/", depComponentTest.getComponentTestFilename(), depComponentTest.getClassName());
        this.addDependency("../../basket/spec/", sutComponentTest.getComponentTestFilename(), sutComponentTest.getClassName());
        this.addWholeDependencyStatement(depComponentTest.getTestUtilDependency());

        testCases.add(newTestCase);
        System.out.println(newTestCase.getTestCaseCode());
    }


    private String getAllTestCasesCode() {
        StringBuilder allTestCasesCode = new StringBuilder();
        for (TestCase testCase:this.testCases) {
            allTestCasesCode.append(testCase.getTestCaseCoupleTestCode());
        }
        return allTestCasesCode.toString();
    }


    public String getIntegrationTestCoupleTestsCode() {
        return this.getAllDependenciesCode()
                + this.getTestSuiteHeaderCode()
                + this.getTeardownCode()
                + this.getAllTestCasesCode()
                + "});";
    }

    public int getTestNumber() {
        return testNumber;
    }

    public boolean hasTestCases() {
        return this.testCases.size() > 0;
    }
}
