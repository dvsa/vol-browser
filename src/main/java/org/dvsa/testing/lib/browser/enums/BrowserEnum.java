package org.dvsa.testing.lib.browser.enums;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public enum BrowserEnum {

    CHROME("chrome"),
    FIREFOX("firefox");

    private String name;

    BrowserEnum(String name) {
        this.name = name;
    }

    public static BrowserEnum getEnum(@NotNull String name){
        name = StringUtils.deleteWhitespace(name).toLowerCase();
        BrowserEnum browserEnum;

        switch(name) {
            case "chrome":
                browserEnum = CHROME;
                break;
            case "firefox":
                browserEnum = FIREFOX;
                break;
            default:
                throw new IllegalArgumentException("Unsupported browserEnum name: ".concat(name));
        }

        return browserEnum;
    }

    @Override
    public String toString(){
        return name;
    }

}
