package io.appium.espressoserver.lib.helpers.exceptions;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;

public class InvalidHamcrestMethodException extends AppiumException {
    public InvalidHamcrestMethodException (String reason) {
        super(reason);
    }
}
