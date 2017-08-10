package io.appium.espressoserver.lib.helpers.exceptions;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;

public class InvalidHamcrestExpressionException extends AppiumException {
    public InvalidHamcrestExpressionException(String reason) {
        super(reason);
    }
}
