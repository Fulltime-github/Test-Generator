import Model.Component;
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


public class Crawler {
    private static WebDriver driver;
    private static String EventTag = "CustomEvent(\"";
    private static String EventListenerTag = "addEventListener(\"";
    private static ArrayList<Component> microFrontends = new ArrayList<Component>();
    final static Logger logger = Logger.getLogger(Crawler.class);


    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        WebDriverManager.getInstance(CHROME).setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        analyseFeatures();
        mappingFeatures();

    }

    private static void mappingFeatures() {

        logger.info("\n\n\n");
        logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("Start MAPPING");
        if(microFrontends.size() > 0) {
            for (Component microFrontend:microFrontends) {
                logger.info("Start with Microfrontend: " + microFrontend.getName());
                for (Component compareWithMicrofrontend: microFrontends) {
                    logger.info(microFrontend.getName() + " -%- " + compareWithMicrofrontend.getName());
                    if(compareWithMicrofrontend.getName().equals(microFrontend.getName())) {
                        logger.info("Skip Microfrontend: " + microFrontend.getName() + " because the same");
                        continue;
                    }
                    for (String outputEvent:microFrontend.getOutputEvents()) {
                        for (String inputEvent:compareWithMicrofrontend.getInputEvents()) {
                            if(outputEvent.equals(inputEvent)) {
                                microFrontend.addNext(compareWithMicrofrontend);
                                logger.info("Added NEXT: " + compareWithMicrofrontend.getName());
                                logger.info("OUTPUT:" + microFrontend.getName() + ":" + outputEvent + " => " +
                                            " INPUT:" + compareWithMicrofrontend.getName() + ":" + inputEvent);
                            }
                        }
                    }

                    for (String inputEvent:microFrontend.getInputEvents()) {
                        for (String outputEvent:compareWithMicrofrontend.getOutputEvents()) {
                            if(inputEvent.equals(outputEvent)) {
                                microFrontend.addPrevious(compareWithMicrofrontend);
                                logger.info("Added PREVIOUS: " + compareWithMicrofrontend.getName());
                                logger.info("OUTPUT:" + compareWithMicrofrontend.getName() + ":" + outputEvent + " => " +
                                        " INPUT:" + microFrontend.getName() + ":" + inputEvent);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void analyseFeatures() {

        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(local-name(), '-')]"));

        for (WebElement element:elements) {
            logger.info("##########################################################");
            logger.info("New MicroFrontend");
            Component microFrontend = new Component();
            microFrontend.setName(element.getTagName());

            FileSearch fs = new FileSearch();
            logger.info("Collect Data for " + microFrontend.getComponentTestFilename());
            fs.searchDirectory(new File("/xampp/htdocs/Microfrontend/src"), microFrontend.getComponentTestFilename());
            int count = fs.getResult().size();
            if(count ==0) {
                logger.info("No result found!");
            } else {
                logger.info("Found " + count + " related Test");
                for (String matched : fs.getResult()) {
                    microFrontend.setComponentTestFilepath(matched);
                    logger.info("Saved Test File Filepath   : " + matched);
                    String content = null;
                    try {
                        microFrontend.setComponentTestSourceCode(Files.readString(Paths.get(matched)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            fs = new FileSearch();
            fs.searchDirectory(new File("/xampp/htdocs/Microfrontend/src"), microFrontend.getName() + ".ts");
            count = fs.getResult().size();
            if(count ==0) {
                logger.info("No result found!");
            } else {
                logger.info("Found " + count + " related Source Code");
                for (String matched : fs.getResult()) {
                    microFrontend.setComponentSourceCodeFilepath(matched);
                    logger.info("Saved Source Code Filepath : " + matched);
                    try {
                        String content = Files.readString(Paths.get(matched));
                        microFrontend.setInputEvents(findIOByString(content, EventListenerTag));
                        logger.info("Found INPUT: " + microFrontend.getInputEvents().size());
                        microFrontend.setOutputEvents(findIOByString(content, EventTag));
                        logger.info("Found OUTPUT: " + microFrontend.getOutputEvents().size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            microFrontends.add(microFrontend);
        }
    }

    private static List<String> findIOByString(String componentTest, String searchWord) {
        int start;
        ArrayList<String> results = new ArrayList();

        while(componentTest.contains(searchWord)) {
            start = componentTest.indexOf(searchWord);
            componentTest = componentTest.substring(start + searchWord.length(), componentTest.length());
            int end = componentTest.indexOf("\"");
            results.add(componentTest.substring(0, end));
        }
        return results;
    }
}
