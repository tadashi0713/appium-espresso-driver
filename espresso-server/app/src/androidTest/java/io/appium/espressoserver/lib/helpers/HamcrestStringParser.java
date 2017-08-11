package io.appium.espressoserver.lib.helpers;

import android.database.Cursor;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matchers;

import java.lang.reflect.InvocationTargetException;

import io.appium.espressoserver.lib.helpers.exceptions.InvalidHamcrestClassMatcherException;
import io.appium.espressoserver.lib.helpers.exceptions.InvalidHamcrestExpressionException;

public class HamcrestStringParser {

    private final String hamcrestString;
    private final Class matcherClass;
    private final String expression;

    public HamcrestStringParser(String hamcrestString) throws InvalidHamcrestClassMatcherException {
        this.hamcrestString = hamcrestString;
        String[] tokens = this.hamcrestString.split(";");

        if (tokens.length == 2) {
            this.matcherClass = matchStringToClass(tokens[0]);
            this.expression = tokens[1];
        } else {
            this.matcherClass = String.class;
            this.expression = tokens[0];
        }
    }

    private static Class matchStringToClass (String className) throws InvalidHamcrestClassMatcherException {
        try {
            return Class.forName(String.format("java.lang.%s", className));
        } catch (ClassNotFoundException cnfe) { }

        try {
            return Class.forName(String.format("java.util.%s", className));
        } catch (ClassNotFoundException cnfe) { }

        switch (className) {
            case "Cursor":
                return Cursor.class;
            default:
                throw new InvalidHamcrestClassMatcherException(String.format("Unknown class name: %s. Class must be part of 'java.lang', or 'Cursor'"));
        }
    }

    public Class getMatcherClass() {
        return matcherClass;
    }

    public BaseMatcher evaluate() throws InvalidHamcrestExpressionException {
        return (BaseMatcher) new HamcrestMatchersExpression(Matchers.class, expression).evaluate();
    }
}
