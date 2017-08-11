package io.appium.espressoserver.test.helpers;

import io.appium.espressoserver.lib.helpers.HamcrestObject;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class HamcrestJSONTest {
    HamcrestObject hamcrestObject;

    @Before
    public void before() {
         hamcrestObject = new HamcrestObject();
    }

    @Test
    public void shouldUseEqualTo() throws Exception {
        hamcrestObject.equalTo = "Hello";
        assertThat("Hello", hamcrestObject.evaluate());
        assertThat("World", not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldUseStartsWith () throws Exception {
        hamcrestObject.startsWith = "Hel";
        assertThat("Hello", hamcrestObject.evaluate());
        assertThat("World", not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldUseEndsWith () throws Exception {
        hamcrestObject.endsWith = "lo";
        assertThat("Hello", hamcrestObject.evaluate());
        assertThat("World", not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldUseAllOf () throws Exception {
        hamcrestObject = new HamcrestObject<String>();
        hamcrestObject.allOf = new ArrayList<HamcrestObject<String>>();
        HamcrestObject hamcrestObjectOne = new HamcrestObject<String>();
        HamcrestObject hamcrestObjectTwo = new HamcrestObject<String>();
        hamcrestObjectOne.startsWith = "hel";
        hamcrestObjectTwo.endsWith = "lo";
        hamcrestObject.allOf.add(hamcrestObjectOne);
        hamcrestObject.allOf.add(hamcrestObjectTwo);
        assertThat("hello", hamcrestObject.evaluate());
        assertThat("helasfdasfdlo", hamcrestObject.evaluate());
        assertThat("helasfd", not(hamcrestObject.evaluate()));
        assertThat("asdflo", not(hamcrestObject.evaluate()));
        assertThat("world", not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldUseAnyOf () throws Exception {
        hamcrestObject = new HamcrestObject<String>();
        hamcrestObject.anyOf = new ArrayList<HamcrestObject<String>>();
        HamcrestObject hamcrestObjectOne = new HamcrestObject<String>();
        HamcrestObject hamcrestObjectTwo = new HamcrestObject<String>();
        hamcrestObjectOne.startsWith = "hel";
        hamcrestObjectTwo.endsWith = "lo";
        hamcrestObject.anyOf.add(hamcrestObjectOne);
        hamcrestObject.anyOf.add(hamcrestObjectTwo);
        assertThat("hello", hamcrestObject.evaluate());
        assertThat("helasfdasfdlo", hamcrestObject.evaluate());
        assertThat("helasfd", hamcrestObject.evaluate());
        assertThat("asdflo", hamcrestObject.evaluate());
        assertThat("world", not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldUseAnything () throws Exception {
        hamcrestObject = new HamcrestObject<String>();
        hamcrestObject.anything = true;
        assertThat("world", hamcrestObject.evaluate());
    }

    @Test
    public void shouldEvaluateArraySizes () throws Exception {
        hamcrestObject.arrayWithSize = 3;
        assertThat(new String[]{"foo", "bar", "hello"}, hamcrestObject.evaluate());
        assertThat(new String[]{"foo", "bar"}, not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldEvaluateCloseTo () throws Exception {
        hamcrestObject.closeTo = new double[]{ 100.0, 1 };
        assertThat(100.0, hamcrestObject.evaluate());
        assertThat(100.1, hamcrestObject.evaluate());
        assertThat(99.9, hamcrestObject.evaluate());
        assertThat(102.0, not(hamcrestObject.evaluate()));
        assertThat(98.0, not(hamcrestObject.evaluate()));
        assertThat(0, not(hamcrestObject.evaluate()));
        assertThat("String", not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldEvaluateContainsString () throws Exception {
        hamcrestObject.containsString = "ell";
        assertThat("ell", hamcrestObject.evaluate());
        assertThat("hello", hamcrestObject.evaluate());
        assertThat("", not(hamcrestObject.evaluate()));
        assertThat("world", not(hamcrestObject.evaluate()));
        assertThat("HELLO", not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldEvaluateEmpty () throws Exception {
        hamcrestObject.empty = true;
        assertThat(new ArrayList<>(), hamcrestObject.evaluate());
        assertThat(Collections.emptyList(), hamcrestObject.evaluate());
        assertThat(Collections.singletonList("hello"), not(hamcrestObject.evaluate()));
        assertThat(Collections.singletonMap("hello", "world"), not(hamcrestObject.evaluate()));
        ArrayList arrayList = new ArrayList();
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        assertThat(arrayList, not(hamcrestObject.evaluate()));
    }

    @Test
    public void shouldEvaluateHasEntrt () throws Exception {
        /*hamcrestObject.hasEntry = new HamcrestObject.Entry();
        hamcrestObject.hasEntry.key = "hello";
        hamcrestObject.hasEntry.value = "world";
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");
        assertThat(map, hamcrestObject.evaluate());
        map = new HashMap<>();
        map.put("hello", "whirl");
        assertThat(map, not(hamcrestObject.evaluate()));
        map = new HashMap<>();
        assertThat(map, not(hamcrestObject.evaluate()));*/
    }

}