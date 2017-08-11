package io.appium.espressoserver.test.helpers;

import io.appium.espressoserver.lib.helpers.HamcrestMatchersExpression;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.StringStartsWith;
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

    @Test
    public void shouldEvaluateHamcrestExpressions() throws Exception {
        HamcrestMatchersExpression hamcrestExpression = new HamcrestMatchersExpression(Matchers.class, "startsWith(\"Foo\")");
        BaseMatcher matcher = (BaseMatcher) hamcrestExpression.evaluate();
        assertEquals(matcher.getClass(), StringStartsWith.class);
        hamcrestExpression = new HamcrestMatchersExpression(Matchers.class, "empty()");
        matcher = (BaseMatcher) hamcrestExpression.evaluate();
        assertEquals(matcher.getClass(), IsEmptyCollection.class);
    }

    @Test
    public void shouldEvaluateNestedHamcrestExpressions() throws Exception {
        HamcrestMatchersExpression hamcrestExpression = new HamcrestMatchersExpression(Matchers.class, "allOf(startsWith(\"Foo\"), empty())");
        assertEquals(hamcrestExpression.getArgs().length, 2);
        assertEquals(hamcrestExpression.getArgs()[0].getClass(), StringStartsWith.class);
        assertEquals(hamcrestExpression.getArgs()[1].getClass(), IsEmptyCollection.class);
        BaseMatcher matcher = (BaseMatcher) hamcrestExpression.evaluate();
        assertEquals(matcher.getClass(), Matcher.class);
    }
}