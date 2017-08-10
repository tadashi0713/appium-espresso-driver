package io.appium.espressoserver.test.helpers;

import io.appium.espressoserver.lib.helpers.HamcrestMatchersExpression;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class HamcrestExpressionTest {
    @Before
    public void before() {
    }

    @Test
    public void shouldMatchMethodNamesToHamcrestMatchersMethods() throws Exception {
        HamcrestMatchersExpression hamcrestExpression = new HamcrestMatchersExpression(String.class, "startsWith(\"Foo\")");
        assertTrue(hamcrestExpression.getHamcrestMethod().equals(Matchers.class.getMethod("startsWith", String.class)));
        assertTrue(hamcrestExpression.getArgTypes().length == 1);
        assertTrue(hamcrestExpression.getArgTypes()[0].equals(String.class));
        assertTrue(hamcrestExpression.getArgs()[0].equals("Foo"));

        hamcrestExpression = new HamcrestMatchersExpression(String.class, "empty()");
        assertTrue(hamcrestExpression.getHamcrestMethod().equals(Matchers.class.getMethod("empty")));
        assertTrue(hamcrestExpression.getArgTypes().length == 0);

        hamcrestExpression = new HamcrestMatchersExpression(String.class, "contains(empty())");
        assertTrue(hamcrestExpression.getArgTypes().length == 1);
        assertTrue(hamcrestExpression.getArgTypes()[0].equals(Matcher.class));

    }
}