import Model.Component;
import Model.ComponentTest;
import Model.Event;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;


public class Generator {

    /**
     * CONFIG GENERATOR START
     */
    private final static String PROGRAM_DIRECTORY = "/xampp/htdocs/Microfrontend/src";
    private final static String DISPATCH_EVENT_TAG = "CustomEvent(\"";
    private final static String EVENT_LISTENER_TAG = "addEventListener(\"";
    /**
     * END CONFIG GENERATOR
     */


    private WebDriver driver;
    private ArrayList<Component> microFrontends = new ArrayList<Component>();
    private final static Logger logger = Logger.getLogger(Generator.class);


    public void start(String[] args) {

        logger.info("\n\n\n");
        logger.info("##########################################################");
        logger.info("##########################################################");
        logger.info("###############   START TEST GENERATOR ###################");
        logger.info("##########################################################");
        logger.info("##########################################################");
        logger.info("PROGRAM_DIRECTORY: " + PROGRAM_DIRECTORY);
        logger.info("DISPATCH EVENT TAG: " + DISPATCH_EVENT_TAG);
        logger.info("EVENT LISTENER TAG: " + EVENT_LISTENER_TAG);
        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        WebDriverManager.getInstance(CHROME).setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        analyseFeatures();
        mappingFeatures();

    }

    private void combineTests() {

    }

    private void mappingFeatures() {

        logger.info("\n\n\n");
        logger.info("##########################################################");
        logger.info("##########################################################");
        logger.info("###############   MAPPING FEATURES PHASE #################");
        logger.info("##########################################################");
        logger.info("##########################################################");
        logger.info("Start MAPPING");
        if(microFrontends.size() > 0) {
            for (Component microFrontend:microFrontends) {
                logger.info("##########################################################");
                logger.info("##########################################################");
                logger.info("Start with Microfrontend: " + microFrontend.getName());
                logger.info("SEARCH FOR SUCCESSOR AND PREDECESSOR: " + microFrontend.getName());
                for (Component compareWithMicrofrontend: microFrontends) {
                    logger.info("________________________________________________");
                    logger.info("COMPARE: " + microFrontend.getName() + " -%- " + compareWithMicrofrontend.getName());
                    if(compareWithMicrofrontend.getName().equals(microFrontend.getName())) {
                        logger.info("SKIP Microfrontend: " + microFrontend.getName() + " because it the same");
                        continue;
                    }
                    //Find Next of current Component
                    // Compare Output Event Dispatcher with Input Listener of second Component
                    for (Event outputEvent:microFrontend.getOutputEvents()) {
                        for (Event inputEvent:compareWithMicrofrontend.getInputEvents()) {
                            if(outputEvent.getName().equals(inputEvent.getName())) {
                                logger.info("FOUND RELATION: " + microFrontend.getName() + " ===> " + compareWithMicrofrontend.getName());
                                logger.info("ADDING SUCCESSOR: " + compareWithMicrofrontend.getName());
                                microFrontend.addNext(compareWithMicrofrontend);
                                logger.info("OUTPUT: [COMPONENT: " + microFrontend.getName() + " -- OUTPUT_EVENT:" + outputEvent + "]");
                                logger.info("INPUT: [COMPONENT: " + compareWithMicrofrontend.getName() + " INPUT EVENT:" + inputEvent + "]");
                            }
                        }
                    }

                    //Find Previous of current Component
                    //Compare Input Listener with Event Dispatcher of second Component
                    for (Event inputEvent:microFrontend.getInputEvents()) {
                        for (Event outputEvent:compareWithMicrofrontend.getOutputEvents()) {
                            if(inputEvent.getName().equals(outputEvent.getName())) {
                                logger.info("FOUND RELATION: " + compareWithMicrofrontend.getName() + " ===> " + microFrontend.getName());
                                logger.info("ADDING PREDECESSOR: " + compareWithMicrofrontend.getName());
                                microFrontend.addPrevious(compareWithMicrofrontend);
                                logger.info("OUTPUT: [COMPONENT: " + compareWithMicrofrontend.getName() + " -- OUTPUT_EVENT: " + outputEvent + "]");
                                logger.info("INPUT: [COMPONENT: " + microFrontend.getName() + " -- INPUT EVENT: " + inputEvent+ "]");
                            }
                        }
                    }
                }
            }
        }
    }

    private void analyseFeatures() {

        logger.info("##########################################################");
        logger.info("##########################################################");
        logger.info("###############   ANALYZE FEATURES PHASE ####################");
        logger.info("##########################################################");
        logger.info("##########################################################");
        logger.info("Search for Custom Elements");
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(local-name(), '-')]"));
        logger.info("FOUND " + elements.size() + " Custom Elements");
        for (WebElement element:elements) {
            logger.info("##########################################################");
            logger.info("##########################################################");
            logger.info("Found MicroFrontend: " + element.getTagName());
            Component microFrontend = new Component();
            microFrontend.setName(element.getTagName());

            //SEARCHING FOR COMPONENT TESTS
            FileSearch fs = new FileSearch();
            logger.info("Search for Testfile: " + microFrontend.getComponentTestFilename());
            fs.searchDirectory(new File(PROGRAM_DIRECTORY), microFrontend.getComponentTestFilename());
            int count = fs.getResult().size();
            if(count ==0) {
                logger.info("No Testfile found for Test: ");
            } else {
                logger.info("Found " + count + " Test File/s");
                for (String matched : fs.getResult()) {
                    ComponentTest componentTest = new ComponentTest();
                    componentTest.setComponentTestFilepath(matched);
                    logger.info("Saved Test File Filepath: " + matched);
                    String content = null;
                    try {
                        componentTest.setComponentTestCompleteSourceCode(Files.readString(Paths.get(matched)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    microFrontend.addComponentTest(componentTest);
                }
            }

            //SEARCHING FOR SOURCE CODE
            fs = new FileSearch();
            logger.info("Search for Source Code File: " + microFrontend.getSourceCodeFileName());
            fs.searchDirectory(new File(PROGRAM_DIRECTORY), microFrontend.getSourceCodeFileName());
            count = fs.getResult().size();
            if(count ==0) {
                logger.info("No Source Code found for Source Code Filename: " + microFrontend.getSourceCodeFileName());
            } else {
                logger.info("Found " + count + " related Source Code");
                for (String matched : fs.getResult()) {
                    microFrontend.setComponentSourceCodeFilepath(matched);
                    logger.info("Saved Source Code Filepath : " + matched);
                    try {
                        String content = Files.readString(Paths.get(matched));
                        microFrontend.setComponentSourceCode(content);
                        logger.info("________________________________________________");
                        logger.info("Search for INPUT AND OUTPUT CUSTOM EVENTS");
                        logger.info("INPUT CUSTOM EVENTS: " + element.getTagName());
                        microFrontend.setInputEvents(findIOByString(microFrontend, EVENT_LISTENER_TAG));
                        logger.info("INPUT EVENTS: " + microFrontend.getInputEvents().size());
                        logger.info("OUTPUT CUSTOM EVENTS: " + element.getTagName());
                        microFrontend.setOutputEvents(findIOByString(microFrontend, DISPATCH_EVENT_TAG));
                        logger.info("OUTPUT EVENTS: " + microFrontend.getOutputEvents().size());
                        logger.info("_________________________________________________");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            microFrontends.add(microFrontend);
        }
    }

    private List<Event> findIOByString(Component microFrontend, String searchWord) {
        int start;
        ArrayList<Event> results = new ArrayList();
        String componentTest = microFrontend.getComponentSourceCode();
        while(componentTest.contains(searchWord)) {
            start = componentTest.indexOf(searchWord);
            componentTest = componentTest.substring(start + searchWord.length(), componentTest.length());
            int end = componentTest.indexOf("\"");
            String customEvent = componentTest.substring(0, end);

            Event event = new Event();
            event.setName(customEvent);
            logger.info("FOUND EVENT: " + event.getName());
            results.add(event);

        }
        return results;
    }
}
