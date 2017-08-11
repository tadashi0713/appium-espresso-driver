package io.appium.espressoserver.lib.helpers;

import com.google.gson.Gson;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.CombinableMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class HamcrestObject<T> {

    public static class Entry {
        public HamcrestObject key;
        public HamcrestObject value;
    }

    public String isA; // TODO: Make this an enum
    public List<HamcrestObject<T>> allOf;
    public List<HamcrestObject<T>> anyOf;
    public boolean anything = false;
    public String startsWith;
    public String endsWith;
    public T equalTo;

    public Integer arrayWithSize;

    public double[] closeTo;
    public String containsString;
    public boolean empty = false;

    public Entry hasEntry;

    public Matcher<T> evaluate() {
        List<BaseMatcher<T>> matchers = new ArrayList<>();
        if (this.equalTo != null) {
            matchers.add((BaseMatcher<T>) equalTo(equalTo));
        }
        if (this.startsWith != null) {
            matchers.add((BaseMatcher<T>) startsWith(startsWith));
        }
        if (this.endsWith != null) {
            matchers.add((BaseMatcher<T>) endsWith(endsWith));
        }
        if (this.allOf != null) {
            List<Matcher<? super T>> list = new ArrayList<>();
            for(int i=0; i<allOf.size(); i++) {
                list.add(allOf.get(i).evaluate());
            }
            matchers.add((BaseMatcher<T>) allOf(list));
        }
        if (this.anyOf != null) {
            List<Matcher<? super T>> list = new ArrayList<>();
            for(int i=0; i<anyOf.size(); i++) {
                list.add(anyOf.get(i).evaluate());
            }
            matchers.add(anyOf(list));
        }
        if (this.anything) {
            matchers.add((BaseMatcher<T>) anything());
        }

        if (this.arrayWithSize != null) {
            matchers.add((BaseMatcher <T>) arrayWithSize(arrayWithSize));
        }

        if (this.closeTo != null && this.closeTo.length > 0) {
            double operand = closeTo[0];
            double error = closeTo.length == 1 ? 0 : closeTo[1];
            matchers.add((BaseMatcher<T>) closeTo(operand, error));
        }

        if (this.containsString != null) {
            matchers.add((BaseMatcher<T>) containsString(containsString));
        }

        if (this.empty) {
            matchers.add((BaseMatcher<T>) empty());
        }

        if (this.hasEntry != null) {
            matchers.add((BaseMatcher<T>) hasEntry(hasEntry.key.evaluate(), hasEntry.value.evaluate()));
        }

        return allOf((Iterable) matchers);
    }
}