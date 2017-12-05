package org.dvsa.testing.lib;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.lib.BrowserName.*;

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

    public static void open(String url){
        if(getDriver() == null){
            setDriver(getNewInstance(getName(System.getProperty("browser"))));
            setImplicitWait(MAX_IMPLICIT_WAIT);
        }

        getDriver().get(url);
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
