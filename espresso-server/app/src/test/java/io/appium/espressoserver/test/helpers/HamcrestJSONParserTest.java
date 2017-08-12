package io.appium.espressoserver.test.helpers;

import org.junit.Before;
import org.junit.Test;

import io.appium.espressoserver.lib.helpers.HamcrestJSONParser;
import io.appium.espressoserver.lib.helpers.HamcrestObject.HamcrestTypes;

import static org.junit.Assert.assertEquals;


public class HamcrestJSONParserTest {
    @Before
    public void before() {
    }

    @Test
    public void shouldDoIntegerShorthand() throws Exception {
        HamcrestJSONParser hamcrestJSONParser = new HamcrestJSONParser("123");
        assertEquals(hamcrestJSONParser.getHamcrestObject().isA, HamcrestTypes.INTEGER);
        assertEquals(hamcrestJSONParser.getHamcrestObject().equalTo, 123);
    }

    @Test
    public void shouldDoStringShorthand() throws Exception {
        HamcrestJSONParser hamcrestJSONParser = new HamcrestJSONParser("fluff");
        assertEquals(hamcrestJSONParser.getHamcrestObject().isA, HamcrestTypes.STRING);
        assertEquals(hamcrestJSONParser.getHamcrestObject().equalTo, "fluff");
    }

    @Test
    public void shouldParseJSON() throws Exception {
        HamcrestJSONParser hamcrestJSONParser = new HamcrestJSONParser("");
        assertEquals(hamcrestJSONParser.getHamcrestObject().isA, HamcrestTypes.STRING);
        assertEquals(hamcrestJSONParser.getHamcrestObject().equalTo, "fluff");
    }
}