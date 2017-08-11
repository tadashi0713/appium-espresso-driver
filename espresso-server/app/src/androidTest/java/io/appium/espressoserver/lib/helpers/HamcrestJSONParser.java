package io.appium.espressoserver.lib.helpers;

import com.google.gson.Gson;

import org.hamcrest.Matcher;

public class HamcrestJSONParser {

    private HamcrestObject hamcrestObject;

    public HamcrestJSONParser(String json) {
        hamcrestObject = (new Gson()).fromJson(json, HamcrestObject.class);

        // TODO: make this more efficient
        if (hamcrestObject.isA != null) {
            if (hamcrestObject.isA.equals("Integer")) {
                hamcrestObject = (new Gson()).fromJson(json, HamcrestExtendedObjects.HamcrestObjectInteger.class);
            }
        }
    }

    public Matcher evaluate () {
        return hamcrestObject.evaluate();
    }

}
