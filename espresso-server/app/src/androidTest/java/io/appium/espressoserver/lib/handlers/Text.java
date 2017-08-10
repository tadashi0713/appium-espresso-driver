package io.appium.espressoserver.lib.handlers;

import javax.annotation.Nullable;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;
import io.appium.espressoserver.lib.helpers.ViewOrDataInteraction;
import io.appium.espressoserver.lib.model.AppiumParams;
import io.appium.espressoserver.lib.model.Element;
import io.appium.espressoserver.lib.viewaction.ViewTextGetter;

public class Text implements RequestHandler<AppiumParams, CharSequence> {

    @Override
    @Nullable
    public CharSequence handle(AppiumParams params) throws AppiumException {
        ViewOrDataInteraction interaction = Element.getById(params.getElementId());
        return (new ViewTextGetter()).get(interaction);
    }
}
