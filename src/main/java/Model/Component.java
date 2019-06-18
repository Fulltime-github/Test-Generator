package Model;

import java.util.ArrayList;
import java.util.List;

public class Component {

    private String name;
    private String ComponentSourceCodeFilepath;
    private String ComponentSourceCode;
    private String ComponentTestFilepath;
    private String ComponentTestSourceCode;
    private List<String> InputEvents = new ArrayList<>();
    private List<String> OutputEvents = new ArrayList<>();
    private List<Component> previous = new ArrayList<>();
    private List<Component> next = new ArrayList<>();

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

    public String getComponentTestFilepath() {
        return ComponentTestFilepath;
    }

    public void setComponentTestFilepath(String componentTestFilepath) {
        ComponentTestFilepath = componentTestFilepath;
    }

    public String getComponentTestSourceCode() {
        return ComponentTestSourceCode;
    }

    public void setComponentTestSourceCode(String componentTestSourceCode) {
        ComponentTestSourceCode = componentTestSourceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInputEvents() {
        return InputEvents;
    }

    public void setInputEvents(List<String> inputEvents) {
        InputEvents = inputEvents;
    }

    public List<String> getOutputEvents() {
        return OutputEvents;
    }

    public void setOutputEvents(List<String> outputEvents) {
        OutputEvents = outputEvents;
    }
}
