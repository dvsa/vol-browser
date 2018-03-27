package org.dvsa.testing.lib.browser.exceptions;

import activesupport.system.out.Output;

public class UninitialisedDriverException extends RuntimeException {

    public UninitialisedDriverException() {
        super(Output.printColoredLog("[ERROR] Attempted using a driver that has not been initialised/set"));
    }

    public UninitialisedDriverException(String message) {
        super(message);
    }

    public UninitialisedDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public UninitialisedDriverException(Throwable cause) {
        super(cause);
    }
}
