package io.appium.espressoserver.lib.helpers;

import android.database.Cursor;
import io.appium.espressoserver.lib.helpers.exceptions.InvalidHamcrestClassMatcherException;

public class HamcrestStringParser {

    private final String hamcrestString;
    private final Class matcherClass;
    private final String expression;

    public HamcrestStringParser(String hamcrestString) throws InvalidHamcrestClassMatcherException {
        this.hamcrestString = hamcrestString;
        String[] tokens = this.hamcrestString.split(";");

        if (tokens.length == 2) {
            this.matcherClass = getMatcherClass(tokens[0]);
            this.expression = tokens[1];
        } else {
            this.matcherClass = String.class;
            this.expression = tokens[0];
        }
    }

    public static Class getMatcherClass (String className) throws InvalidHamcrestClassMatcherException {
        try {
            return Class.forName(String.format("java.lang.%s", className));
        } catch (ClassNotFoundException cnfe) {
            switch (className) {
                case "Cursor":
                    return Cursor.class;
                default:
                    throw new InvalidHamcrestClassMatcherException(String.format("Unknown class name: %s. Class must be part of 'java.lang', or 'Cursor'"));
            }
        }
    }

}
