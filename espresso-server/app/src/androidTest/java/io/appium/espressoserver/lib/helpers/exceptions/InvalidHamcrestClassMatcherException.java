package io.appium.espressoserver.lib.helpers.exceptions;


import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;

public class InvalidHamcrestClassMatcherException extends AppiumException {
    public InvalidHamcrestClassMatcherException(String reason) {
        super(reason);
    }
}
