package org.dvsa.testing.lib.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.lang.reflect.Constructor;
import java.net.URL;

public class Browser {

    private static WebDriver driver;

    private static String unsupportedBrowserTemplate = "%s is not a supported browser.";

    private static final Thread CLOSE_THREAD = new Thread(Browser::quit);

    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    public static WebDriver getDriver() {
        if (isUninitialisedOrClosed())
            throw new WebDriverException("Driver is in an unusable state, it may not be initialised or has been closed");

        return Browser.driver;
    }

    public static void setDriver(WebDriver driver) {
        Browser.driver = driver;
    }

    public static void open(@NotNull URL URL) {
        open(URL.toString());
    }

    public static void open(@NotNull String URL) {
        getDriver().get(URL);
    }

    public static void go(@NotNull String URL) {
        open(URL);
    }

    public static void go(@NotNull URL URL) {
        open(URL);
    }

    public static boolean isUninitialisedOrClosed() {
        boolean isBrowserClosed = true;
        if (Browser.driver != null && !Browser.driver.toString().contains("null")){
            isBrowserClosed = false;
        }
        return isBrowserClosed;
    }

    public static boolean isOpen(){
        return !isUninitialisedOrClosed();
    }

    public static void quit(){
        if(isOpen())
            getDriver().quit();
    }

    public static void initialise(@NotNull String browser) {
        initialise(browser, null);
    }

    public static <T extends MutableCapabilities> void initialise(@NotNull String browser, T capabilities) {
        setDriver(Browser.createDriver(browser, capabilities));
    }

    public static <T extends MutableCapabilities> void initialise(@NotNull URL remoteGridURL, T capabilities) {
        setDriver(createDriver(remoteGridURL, capabilities));
    }

    private static WebDriver createDriver(@NotNull String browser){
        return createDriver(browser, null, null);
    }

    private static <T extends MutableCapabilities> WebDriver createDriver(@NotNull String browser, T capabilities) {
        return createDriver(browser, null, capabilities);
    }

    private static <T extends MutableCapabilities> WebDriver createDriver(@NotNull URL remoteGridURL, T capabilities) {
        return createDriver(null, remoteGridURL, capabilities);
    }

    private static <T extends MutableCapabilities> WebDriver createDriver(String browser, URL remoteGridURL, T capabilities) {
        return (remoteGridURL == null) ? localWebDriver(browser, capabilities) : new RemoteWebDriver(remoteGridURL, capabilities);
    }

    private static <T extends MutableCapabilities> WebDriver localWebDriver(@NotNull String browser, T capabilities) {
        try {
            Class<? extends WebDriver> driverClass = getDriverClass(browser);
            Class<? extends MutableCapabilities> constructorParameterType;

            WebDriverManager.getInstance(driverClass).setup();

            WebDriver driver;

            if (capabilities == null) {
                driver = driverClass.newInstance();
            } else {
                constructorParameterType = getDriverParameterClass(browser);
                Constructor constructor = driverClass.getConstructor(constructorParameterType);
                driver = (WebDriver) constructor.newInstance(constructorParameterType.newInstance().merge(capabilities));
            }

            return driver;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Unable to create local WebDriver instance");
        }
    }

    private static Class<? extends MutableCapabilities> getDriverParameterClass(@NotNull String browser) {
        Class<? extends MutableCapabilities> klass;

        switch (StringUtils.deleteWhitespace(browser).toLowerCase()) {
            case "chrome":
                klass = ChromeOptions.class;
                break;
            case "firefox":
                klass = FirefoxOptions.class;
                break;
            case "edge":
                klass = EdgeOptions.class;
                break;
            case "safari":
                klass = SafariOptions.class;
                break;
            default:
                throw new IllegalArgumentException(String.format(unsupportedBrowserTemplate, browser));
        }

        return klass;
    }

    private static Class<? extends WebDriver> getDriverClass(@NotNull String browser) {
        Class<? extends WebDriver> klass;

        switch (StringUtils.deleteWhitespace(browser).toLowerCase()) {
            case "chrome":
                klass = ChromeDriver.class;
                break;
            case "firefox":
                klass = FirefoxDriver.class;
                break;
            case "edge":
                klass = EdgeDriver.class;
                break;
            case "safari":
                klass = SafariDriver.class;
                break;
            default:
                throw new IllegalArgumentException(String.format(unsupportedBrowserTemplate, browser));
        }

        return klass;
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

}
