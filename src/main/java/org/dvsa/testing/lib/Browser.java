package org.dvsa.testing.lib;

import activesupport.system.Properties;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.lib.BrowserName.CHROME;
import static org.dvsa.testing.lib.BrowserName.FIREFOX;

public class Browser {

    private static final int MAX_IMPLICIT_WAIT = 15;

    private static String unsupportedBrowserTemplate = "%s is not a supported browser.";
    private static WebDriver driver;


    public static WebDriver getDriver(){
        return Browser.driver;
    }

    public static void setDriver(WebDriver driver) {
        Browser.driver = driver;
    }

    public static void open(@NotNull String URL) {
        loadConfigBeforeCreatingDriver();
        setBrowserOnFirstRunOrAfterClosure();
        getDriver().get(URL);
    }

    private static void setBrowserOnFirstRunOrAfterClosure(){
        if(getDriver() == null || browserClosed()){
            setDriver(getNewInstance(getName(System.getProperty("browser"))));
        }
    }

    private static boolean browserClosed(){
        return getDriver().toString().contains("null");
    }

    private static void loadConfigBeforeCreatingDriver(){
        if(getDriver() == null){

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

    public static void go(@NotNull String URL){
        open(URL);
    }

    public static void quit(){
        getDriver().quit();
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

    public static String getURL(){
        return getDriver().getCurrentUrl();
    }

    public static String getPageTitle(){
        return getDriver().getTitle();
    }

    public static void setImplicitWait(int seconds){
        setImplicitWait(seconds, TimeUnit.SECONDS);
    }

    public static void setImplicitWait(int time, TimeUnit timeUnit){
        Browser.getDriver().manage().timeouts().implicitlyWait(time, timeUnit);
    }

}
