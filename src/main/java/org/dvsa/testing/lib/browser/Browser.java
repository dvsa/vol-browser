package org.dvsa.testing.lib.browser;

import activesupport.system.Properties;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.dvsa.testing.lib.browser.enums.BrowserEnum;
import org.dvsa.testing.lib.browser.exceptions.UninitialisedDriverException;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Browser {

    private static String unsupportedBrowserTemplate = "%s is not a supported browser.";
    private static WebDriver driver;

    private static final Thread CLOSE_THREAD = new Thread(() -> {
        if (isOpen())
            getDriver().close();
    });

    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    public static WebDriver getDriver() {
        if (Browser.driver == null) {
            throw new UninitialisedDriverException();
        }

        return Browser.driver;
    }

    public static void setDriver(WebDriver driver) {
        Browser.driver = driver;
    }

    public static void open(@NotNull URL URL) {
        open(URL.toString());
    }

    public static void open(@NotNull String URL) {
        setBrowserOnFirstRunOrAfterClosure();
        getDriver().get(URL);
    }

    public static void go(@NotNull String URL) {
        open(URL);
    }

    public static void go(@NotNull URL URL) {
        open(URL);
    }

    private static void setBrowserOnFirstRunOrAfterClosure() {
        if (Browser.driver == null || isClosed())
            setDriver(getNewInstance(BrowserEnum.getEnum(Properties.get("browser", true))));
    }

    public static boolean isClosed() {
        boolean isBrowserClosed = true;
        if (org.dvsa.testing.lib.browser.Browser.driver != null && !getDriver().toString().contains("null")){
            isBrowserClosed = false;
        }
        return isBrowserClosed;
    }

    public static boolean isOpen(){
        return !isClosed();
    }

    public static void quit(){
        if(isOpen()){
            if (Thread.currentThread() != CLOSE_THREAD) {
                throw new UnsupportedOperationException("You shouldn't quit this WebDriver. It's shared and will quit when the JVM exits.");
            }
            getDriver().quit();
        }
    }

    private static WebDriver getNewInstance(BrowserEnum browser){
        WebDriver driver;

        switch(browser){
            case CHROME:
                ChromeDriverManager.getInstance().setup();
                driver = new ChromeDriver();
                break;
            case FIREFOX:
                FirefoxDriverManager.getInstance().setup();
                driver = new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException(String.format(unsupportedBrowserTemplate, browser));
        }

        return driver;
    }

    public static String getURL() {
        return getDriver().getCurrentUrl();
    }

    public static String getPageTitle() {
        return getDriver().getTitle();
    }

    public static void deleteCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public static void refresh() {
        getDriver().navigate().refresh();
    }

    public static void setImplicitWait(int seconds) {
        setImplicitWait(seconds, TimeUnit.SECONDS);
    }

    public static void setImplicitWait(int time, TimeUnit timeUnit) {
        Browser.getDriver().manage().timeouts().implicitlyWait(time, timeUnit);
    }

}
