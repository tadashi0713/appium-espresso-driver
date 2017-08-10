package io.appium.espressoserver.lib.helpers;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;

import javax.annotation.Nullable;

/**
 * A union of ViewInteraction and DataInteraction objects
 */
public class ViewOrDataInteraction {

    private ViewInteraction viewInteraction;
    private DataInteraction dataInteraction;

    public ViewOrDataInteraction (@Nullable ViewInteraction viewInteraction) {
        this.viewInteraction = viewInteraction;
    }

    public ViewOrDataInteraction (@Nullable DataInteraction dataInteraction) {
        this.dataInteraction = dataInteraction;
    }

    public Class getInteractionType () {
        if (this.viewInteraction != null) {
            return this.viewInteraction.getClass();
        } else {
            return this.dataInteraction.getClass();
        }
    }

    public ViewInteraction perform (ViewAction... viewActions) {
        return this.viewInteraction != null ? this.viewInteraction.perform(viewActions)
                : this.dataInteraction.perform(viewActions);
    }

    public ViewInteraction check (ViewAssertion viewAssert) {
        return this.viewInteraction != null ? this.viewInteraction.check(viewAssert)
                : this.dataInteraction.check(viewAssert);
    }
}
