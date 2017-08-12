package io.appium.espressoserver.lib.helpers;

import com.google.gson.Gson;

import org.hamcrest.Matcher;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;
import io.appium.espressoserver.lib.helpers.HamcrestObject.HamcrestTypes;

import static io.appium.espressoserver.lib.helpers.HamcrestExtendedObjects.*;

public class HamcrestJSONParser {

    private HamcrestObject hamcrestObject;

    public HamcrestJSONParser(String json) {

        String text = json.trim();

        // Check if provided non JSON shorthands
        if (isInteger(text)) {
            hamcrestObject = new HamcrestObjectInteger();
            hamcrestObject.isA = HamcrestTypes.INTEGER;
            hamcrestObject.equalTo = Integer.parseInt(text);
        } else if (!text.startsWith("{") && !text.endsWith("}")) {
            hamcrestObject = new HamcrestObjectString();
            hamcrestObject.isA = HamcrestTypes.STRING;
            hamcrestObject.equalTo = text;
        } else {
            hamcrestObject = (new Gson()).fromJson(json, HamcrestObject.class);
            if (hamcrestObject.isA != null) {
                Class<? extends HamcrestObject> hamcrestObjectClass = HamcrestObject.class;
                switch (hamcrestObject.isA) {
                    case INTEGER:
                        hamcrestObjectClass = HamcrestObjectInteger.class;
                        break;
                    case STRING:
                        hamcrestObjectClass = HamcrestObjectString.class;
                        break;
                    default:
                        break;
                }
                hamcrestObject = (new Gson()).fromJson(json, hamcrestObjectClass);
            }
        }
    }

    public Matcher evaluate () {
        return hamcrestObject.evaluate();
    }

    public static boolean isInteger (String text) {
        try {
           Integer.parseInt(text);
           return true;
        } catch (Exception e) {
            return false;
        }
    }

    public HamcrestObject getHamcrestObject () {
        return this.hamcrestObject;
    }

}
