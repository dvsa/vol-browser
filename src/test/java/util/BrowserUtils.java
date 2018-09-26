package util;

import activesupport.system.Properties;

public class BrowserUtils {

    public static boolean usingWebDriverManager() {
        return java.lang.Boolean.parseBoolean(Properties.get("webdriver-manager"));
    }

    public static boolean notUsingWebDriverManager() {
        return !usingWebDriverManager();
    }

}
