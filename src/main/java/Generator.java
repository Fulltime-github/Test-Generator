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
    private final static String EVENT_NAME_TAG = "@event";
    private final static String EVENT_TYPE_TAG = "@type";
    private final static String EVENT_DATA_OBJECT_TAG = "@dataObject";
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
                                logger.info("OUTPUT: [COMPONENT: " + microFrontend.getName() + " -- OUTPUT_EVENT:" + outputEvent.getName() + "]");
                                logger.info("INPUT: [COMPONENT: " + compareWithMicrofrontend.getName() + " INPUT EVENT:" + inputEvent.getName() + "]");
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
                                logger.info("OUTPUT: [COMPONENT: " + compareWithMicrofrontend.getName() + " -- OUTPUT_EVENT: " + outputEvent.getName() + "]");
                                logger.info("INPUT: [COMPONENT: " + microFrontend.getName() + " -- INPUT EVENT: " + inputEvent.getName() + "]");
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
                        componentTest.setComponentTestSourceCode(Files.readString(Paths.get(matched)));
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
                        microFrontend.setInputEvents(findInputEvents(microFrontend));
                        logger.info("INPUT EVENTS: " + microFrontend.getInputEvents().size());
                        logger.info("OUTPUT CUSTOM EVENTS: " + element.getTagName());
                        microFrontend.setOutputEvents(findOutputEvents(microFrontend));
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

    private List<Event> findOutputEvents(Component microFrontend) {
        int start;
        ArrayList<Event> results = new ArrayList();

        //String componentTest = microFrontend.getSourceCodeFileName();
        for (ComponentTest componentTest:microFrontend.getComponentTests()) {
            String componentTestSourceCode = componentTest.getComponentTestSourceCode().replaceAll("\n", ""   ).replaceAll("\\s+","");;
            if(componentTestSourceCode.contains(EVENT_NAME_TAG)) {
                while (componentTestSourceCode.contains(EVENT_NAME_TAG)) {

                    Event event = new Event();

                    //Ausgehend davon dass ein Test nur ein event Testet
                    //First Step Cut until first Event Declaration
                    componentTestSourceCode = componentTestSourceCode.substring(componentTestSourceCode.indexOf(EVENT_NAME_TAG));

                    //Second Step Cut until End of Event Declaration for having Full Event declaration separated
                    String eventText = componentTestSourceCode.substring(0, componentTestSourceCode.indexOf("*/") + 1);

                    //Third Step Cut out current Event declaration for finding eventually next declarations
                    componentTestSourceCode = componentTestSourceCode.substring(componentTestSourceCode.indexOf("*/") + 2);


                    String eventDeclarationPart = eventText.substring(eventText.indexOf(EVENT_TYPE_TAG));
                    String eventType = eventDeclarationPart.substring(eventDeclarationPart.indexOf(EVENT_TYPE_TAG) + EVENT_TYPE_TAG.length()+1, eventDeclarationPart.indexOf("*"));
                    if(eventType.equals("output")) {
                        event.setType(eventType);

                        eventDeclarationPart = eventText.substring(eventText.indexOf(EVENT_NAME_TAG));
                        String eventName = eventDeclarationPart.substring(eventDeclarationPart.indexOf(EVENT_NAME_TAG) + EVENT_NAME_TAG.length() + 1, eventDeclarationPart.indexOf("*"));
                        event.setName(eventName);


                        eventDeclarationPart = eventText.substring(eventText.indexOf(EVENT_DATA_OBJECT_TAG));
                        event.setDataObject(eventDeclarationPart.substring(eventDeclarationPart.indexOf(EVENT_DATA_OBJECT_TAG) + EVENT_DATA_OBJECT_TAG.length() + 1, eventDeclarationPart.indexOf("*")));


                        logger.info("FOUND OUTPUT EVENT: " + event.getName());
                        results.add(event);
                    }

                }
            }

        }
        return results;
    }


    private List<Event> findInputEvents(Component microFrontend) {
        int start;
        ArrayList<Event> results = new ArrayList();
        String componentTest = microFrontend.getComponentSourceCode();
        while(componentTest.contains(EVENT_LISTENER_TAG)) {
            start = componentTest.indexOf(EVENT_LISTENER_TAG);
            componentTest = componentTest.substring(start + EVENT_LISTENER_TAG.length());
            int end = componentTest.indexOf("\"");
            String customEvent = componentTest.substring(0, end);

            Event event = new Event();
            event.setName(customEvent);
            logger.info("FOUND INPUT EVENT: " + event.getName());
            results.add(event);

        }
        return results;
    }
}
