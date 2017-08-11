package io.appium.espressoserver.lib.handlers;

import android.support.test.espresso.NoMatchingViewException;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;
import io.appium.espressoserver.lib.handlers.exceptions.MissingCommandsException;
import io.appium.espressoserver.lib.handlers.exceptions.InvalidStrategyException;
import io.appium.espressoserver.lib.handlers.exceptions.NoSuchElementException;
import io.appium.espressoserver.lib.helpers.ViewOrDataInteraction;
import io.appium.espressoserver.lib.model.Element;
import io.appium.espressoserver.lib.model.Locator;

import static io.appium.espressoserver.lib.helpers.ViewFinder.findBy;

public class Finder implements RequestHandler<Locator, Element> {

    @Override
    public Element handle(Locator locator) throws AppiumException {
        try {
            if (locator.getUsing() == null) {
                throw new InvalidStrategyException("Locator strategy cannot be empty");
            } else if (locator.getValue() == null) {
                throw new MissingCommandsException("No locator provided");
            }
            // Test the selector
            ViewOrDataInteraction matcher = findBy(locator.getUsing(), locator.getValue());
            // matcher.check(matches(isDisplayed())); // TODO: isDisplayed probably won't work for DataInteractions

            // If we have a match, return success
            return new Element(matcher);
        } catch (NoMatchingViewException e) {
            throw new NoSuchElementException("Could not find element with strategy " + locator.getUsing() + " and selector " + locator.getValue());
        }
    }
}
