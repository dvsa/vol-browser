package org.dvsa.testing.lib.browser;

import activesupport.system.Properties;
import activesupport.system.out.Output;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.dvsa.testing.lib.browser.enums.BrowserName;
import org.dvsa.testing.lib.browser.exceptions.UninitialisedDriverException;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.lib.browser.enums.BrowserName.CHROME;
import static org.dvsa.testing.lib.browser.enums.BrowserName.FIREFOX;

public class Browser {

    private static final int MAX_IMPLICIT_WAIT = 15;

    private static String unsupportedBrowserTemplate = "%s is not a supported browser.";
    private static WebDriver driver;


    public static WebDriver getDriver() throws UninitialisedDriverException {
        if (Browser.driver == null) {
            throw new UninitialisedDriverException();
        }

        return Browser.driver;
    }

    public static void setDriver(WebDriver driver) {
        Browser.driver = driver;
    }

    public static void open(@NotNull String URL) throws UninitialisedDriverException {
        loadConfigBeforeCreatingDriver();
        setBrowserOnFirstRunOrAfterClosure();
        getDriver().get(URL);
    }

    private static void setBrowserOnFirstRunOrAfterClosure(){
        // This exception is handled as this method throws an exception on the first run as driver won't be set
        try {
            getDriver();
        } catch (UninitialisedDriverException e) {
            setDriver(getNewInstance(getName(Properties.get("browser"))));
        }

        // Sets a new driver instance if the current one has been closed. Note that closing a driver only alters the
        // state of the driver object and doesn't delete it. Browser#isClosed checks which state the driver is in
        if(isClosed()){
            setDriver(getNewInstance(getName(Properties.get("browser"))));
        }
    }

    public static boolean isClosed() {
        boolean isBrowserClosed = true;

        try {
            getDriver();
            isBrowserClosed = getDriver().toString().contains("null");
        } catch (UninitialisedDriverException e) {
            System.out.println(Output.printColoredLog("[WARNING] Driver was never initialised/set"));
        }

        return isBrowserClosed;
    }

    public static boolean isNotClosed(){
        return !isClosed();
    }

    private static void loadConfigBeforeCreatingDriver(){
        try {
            getDriver();
        } catch (UninitialisedDriverException exception) {
            // Adds properties specified in properties/config.properties into system properties
            if(java.nio.file.Files.exists(Paths.get("properties/config.properties"))) {
                try {
                    Properties.loadConfigPropertiesFromFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void go(@NotNull String URL) throws UninitialisedDriverException {
        open(URL);
    }

    public static void quit(){
        if(!isClosed()){
            try {
                getDriver().quit();
            } catch (UninitialisedDriverException e) {
                e.printStackTrace();
            }
        }
    }

    private static WebDriver getNewInstance(BrowserName browserName){
        WebDriver driver;

        switch(browserName){
            case CHROME:
                ChromeDriverManager.getInstance().setup();
                driver = new ChromeDriver();
                break;
            case FIREFOX:
                FirefoxDriverManager.getInstance().setup();
                driver = new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException(String.format(unsupportedBrowserTemplate, browserName));
        }

        return driver;
    }

    private static BrowserName getName(@NotNull String name){
        name = name.toLowerCase().trim();
        BrowserName browserName;

        switch(name){
            case "chrome":
                browserName = CHROME;
                break;
            case "firefox":
                browserName = FIREFOX;
                break;
            default:
                throw new IllegalArgumentException(String.format(unsupportedBrowserTemplate, name));
        }
        return browserName;
    }

    public static String getURL() throws UninitialisedDriverException {
        return getDriver().getCurrentUrl();
    }

    public static String getPageTitle() throws UninitialisedDriverException {
        return getDriver().getTitle();
    }

    public static void setImplicitWait(int seconds) throws UninitialisedDriverException {
        setImplicitWait(seconds, TimeUnit.SECONDS);
    }

    public static void setImplicitWait(int time, TimeUnit timeUnit) throws UninitialisedDriverException {
        Browser.getDriver().manage().timeouts().implicitlyWait(time, timeUnit);
    }

}
