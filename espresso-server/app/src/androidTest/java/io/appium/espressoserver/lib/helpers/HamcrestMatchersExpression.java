package io.appium.espressoserver.lib.helpers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.lang.reflect.Method;

import io.appium.espressoserver.lib.helpers.exceptions.InvalidHamcrestExpressionException;

/**
 * Evaluate a Hamcrest Expression from a string using Java reflection
 */
public class HamcrestMatchersExpression {

    private HamcrestMatchersExpression nestedExpression;
    private Method hamcrestMethod;
    private Class[] argTypes;
    private Object[] args;
    private Class matcherClass;

    public HamcrestMatchersExpression(Class matcherClass, String hamcrestExpression) throws InvalidHamcrestExpressionException {
        this.matcherClass = matcherClass;

        // What comes before the opening parantheses is supposed to be the methodName
        int openingParanthesesIndex = hamcrestExpression.indexOf('(');
        if (openingParanthesesIndex < 0) {
            throw new InvalidHamcrestExpressionException(String.format("'%s' is not a valid Hamcrest expression", hamcrestExpression));
        }
        String methodName = hamcrestExpression.substring(0, openingParanthesesIndex).trim();

        // What comes between the parantheses are the args
        int closingParanthesesIndex = hamcrestExpression.indexOf(')');
        if (closingParanthesesIndex < 0) {
            throw new InvalidHamcrestExpressionException(String.format("%s is not a valid Hamcrest expression", hamcrestExpression));
        }
        String argsString = hamcrestExpression.substring(openingParanthesesIndex + 1, closingParanthesesIndex);

        // Get the types of the args
        String[] argsTokens = argsString.split(",");

        if (argsTokens.length > 1 || argsTokens[0].length() > 0) {
            argTypes = new Class[argsTokens.length];
            args = new Object[argsTokens.length];
            for (int i = 0; i < argTypes.length; i++) {
                argTypes[i] = getType(argsTokens[i]);
                args[i] = getArg(argTypes[i], argsTokens[i]);
            }
        } else {
            argTypes = new Class[0];
            args = new Object[0];
        }

        // Using reflection, get the hamcrest method that will be invoked by this expression
        try {
            this.hamcrestMethod = Matchers.class.getMethod(methodName, argTypes);
        } catch (NoSuchMethodException nsme) {
            throw new InvalidHamcrestExpressionException(String.format("'%s' is not a valid Hamcrest method", methodName));
        }
    }

    /**
     * Infer the type of the expression from the string
     * @param expression
     * @return
     */
    private static Class getType (String expression) {
        if (expression.charAt(0) == '"' && expression.charAt(expression.length() - 1) == '"') {
            return String.class;
        }

        if (expression.charAt(0) == '\'' && expression.charAt(expression.length() - 1) == '\'') {
            return Character.class;
        }

        if (expression.equals("true") || expression.equals("false")) {
            return Boolean.class;
        }

        try {
            Integer.parseInt(expression);
            return Integer.class;
        } catch (Exception e ) { }

        try {
            Double.parseDouble(expression);
            return Double.class;
        } catch (Exception e) { }

        // TODO: Add a List type to map arrays to this

        // If it's not a primitive, assume it's another Hamcrest expression. If it's not a valid Hamcrest
        // expression it will fail to parse.
        return Matcher.class;
    }

    private Object getArg(Class type, String arg) {
        if (type.equals(String.class)) {
            return arg.substring(1, arg.length() - 1);
        } else if (type.equals(Boolean.class)) {
            return arg.equals("true");
        } else if (type.equals(Double.class)) {
            return Double.parseDouble(arg);
        } else if (type.equals(Integer.class)) {
            return Integer.parseInt(arg);
        } else if (type.equals(Character.class)) {
            return arg.charAt(0);
        }

        return arg;
    }

    public Method getHamcrestMethod() {
        return hamcrestMethod;
    }

    public Class[] getArgTypes () {
        return argTypes;
    }

    public Object[] getArgs () {
        return args;
    }
}
