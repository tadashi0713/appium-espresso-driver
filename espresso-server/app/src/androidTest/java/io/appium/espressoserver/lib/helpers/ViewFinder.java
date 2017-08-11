package io.appium.espressoserver.lib.helpers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.CursorMatchers;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;
import io.appium.espressoserver.lib.handlers.exceptions.InvalidStrategyException;
import io.appium.espressoserver.lib.handlers.exceptions.XPathLookupException;
import io.appium.espressoserver.lib.model.Strategy;
import io.appium.espressoserver.lib.viewaction.RootViewFinder;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.appium.espressoserver.lib.viewmatcher.WithXPath.withXPath;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.Is.is;

/**
 * Helper methods to find elements based on locator strategies and selectors
 */
public class ViewFinder {

    /**
     * Find one instance of an element that matches the locator criteria
     * @param strategy Locator strategy (xpath, class name, etc...)
     * @param selector Selector string
     * @return
     * @throws InvalidStrategyException
     * @throws XPathLookupException
     */
    @Nullable
    public static ViewOrDataInteraction findBy(Strategy strategy, String selector) throws AppiumException {
        List<ViewOrDataInteraction> viewInteractions = findAllBy(strategy, selector, true);
        if (viewInteractions.isEmpty()) {
            return null;
        }
        return viewInteractions.get(0);
    }

    /**
     * Find all instances of an element that matches the locator criteria
     * @param strategy Locator strategy (xpath, class name, etc...)
     * @param selector Selector string
     * @return
     * @throws InvalidStrategyException
     * @throws XPathLookupException
     */
    public static List<ViewOrDataInteraction> findAllBy(Strategy strategy, String selector) throws AppiumException {
        return findAllBy(strategy, selector, false);
    }

    ///Find By different strategies
    private static List<ViewOrDataInteraction> findAllBy(Strategy strategy, String selector, boolean findOne)
            throws AppiumException {
        List<ViewOrDataInteraction> matcher;

        switch (strategy) {
            case ID: // with ID

                // find id from target context
                Context context = InstrumentationRegistry.getTargetContext();
                int id = context.getResources().getIdentifier(selector, "Id",
                        InstrumentationRegistry.getTargetContext().getPackageName());

                matcher = getViewInteractions(withId(id), findOne);
                //Object adapter = ((AdapterView) new RootViewFinder().getView(withId(id))).getAdapter();
                break;
            case CLASS_NAME:
                // with class name
                // TODO: improve this finder with instanceOf
                matcher = getViewInteractions(withClassName(endsWith(selector)), findOne);
                break;
            case TEXT:
                // with text
                matcher = getViewInteractions(withText(selector), findOne);
                break;
            case ACCESSIBILITY_ID:
                // with content description
                matcher = getViewInteractions(withContentDescription(selector), findOne);
                break;
            case XPATH:
                // If we're only looking for one item that matches xpath, pass it index 0 or else
                // Espresso throws an AmbiguousMatcherException
                if (findOne) {
                    matcher = getViewInteractions(withXPath(selector, 0), true);
                } else {
                    matcher = getViewInteractions(withXPath(selector), false);
                }
                break;
            case ESPRESSO_HAMCREST:
                HamcrestJSONParser hamcrestJSONParser = new HamcrestJSONParser(selector);
                matcher = getDataInteractions(hamcrestJSONParser.evaluate());
                break;
            default:
                throw new InvalidStrategyException(String.format("Strategy is not implemented: %s", strategy.getStrategyName()));
        }

        return matcher;
    }

    private static List<ViewOrDataInteraction> getDataInteractions (Matcher<? extends Object> matcher) {
        return Collections.singletonList(new ViewOrDataInteraction(onData(matcher)));
    }

    private static List<ViewOrDataInteraction> getViewInteractions(Matcher<View> matcher, boolean findOne) {
        // If it's just one view we want, return a singleton list
        if (findOne) {
            return Collections.singletonList(new ViewOrDataInteraction(onView(matcher)));
        }

        // If we want all views that match the criteria, start looking for ViewInteractions by
        // index and add each match to the List. As soon as we find no match, break the loop
        // and return the list
        List<ViewOrDataInteraction> interactions = new ArrayList<>();
        int i = 0;
        do {
            ViewOrDataInteraction viewInteraction = new ViewOrDataInteraction(onView(withIndex(matcher, i)));
            try {
                viewInteraction.check(matches(isDisplayed()));
            } catch (NoMatchingViewException nme) {
                break;
            }
            interactions.add(viewInteraction);
            i++;
        } while (i < Integer.MAX_VALUE);
        return interactions;
    }

    private static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
}
