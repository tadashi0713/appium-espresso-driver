package io.appium.espressoserver.lib.handlers;

import javax.annotation.Nullable;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;
import io.appium.espressoserver.lib.helpers.ViewOrDataInteraction;
import io.appium.espressoserver.lib.model.AppiumParams;
import io.appium.espressoserver.lib.model.Element;

import static android.support.test.espresso.action.ViewActions.click;

public class Click implements RequestHandler<AppiumParams, Void> {

    @Override
    @Nullable
    public Void handle(AppiumParams params) throws AppiumException {
        ViewOrDataInteraction interaction = Element.getById(params.getElementId());
        interaction.perform(click());
        return null;
    }
}
