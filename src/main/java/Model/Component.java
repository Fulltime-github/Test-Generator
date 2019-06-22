package Model;

import java.util.ArrayList;
import java.util.List;

public class Component {

    private String name;
    private String ComponentSourceCodeFilepath;
    private String ComponentSourceCode;
    private List<Event> InputEvents = new ArrayList<>();
    private List<Event> OutputEvents = new ArrayList<>();
    private List<Component> previous = new ArrayList<>();
    private List<Component> next = new ArrayList<>();
    private List<ComponentTest> componentTests = new ArrayList<>();
    private List<ComponentTest> integrationTests = new ArrayList<>();

    public List<ComponentTest> getIntegrationTests() {
        return integrationTests;
    }

    public void setIntegrationTests(List<ComponentTest> integrationTests) {
        this.integrationTests = integrationTests;
    }

    public List<ComponentTest> getComponentTests() {
        return componentTests;
    }

    public void setComponentTests(List<ComponentTest> componentTests) {
        this.componentTests = componentTests;
    }

    public void addComponentTest(ComponentTest componentTest) {
        this.componentTests.add(componentTest);
    }

    public String getComponentSourceCodeFilepath() {
        return ComponentSourceCodeFilepath;
    }

    public void setComponentSourceCodeFilepath(String componentSourceCodeFilepath) {
        ComponentSourceCodeFilepath = componentSourceCodeFilepath;
    }

    public String getComponentSourceCode() {
        return ComponentSourceCode;
    }

    public void setComponentSourceCode(String componentSourceCode) {
        ComponentSourceCode = componentSourceCode;
    }

    public List<Component> getPrevious() {
        return previous;
    }

    public void setPrevious(List<Component> previous) {
        this.previous = previous;
    }

    public List<Component> getNext() {
        return next;
    }

    public void setNext(List<Component> next) {
        this.next = next;
    }


    public void addNext(Component next) {
        this.next.add(next);
    }

    public void addPrevious(Component next) {
        this.previous.add(next);
    }

    public String getComponentTestFilename() {
        return name + "-test.ts";
    }

    public String getName() {
        return name;
    }


    public String getSourceCodeFileName() {
        return name +  ".ts";
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getInputEvents() {
        return InputEvents;
    }

    public void setInputEvents(List<Event> inputEvents) {
        InputEvents = inputEvents;
    }

    public List<Event> getOutputEvents() {
        return OutputEvents;
    }

    public void setOutputEvents(List<Event> outputEvents) {
        OutputEvents = outputEvents;
    }
}
