package io.appium.espressoserver.lib.handlers;

import java.lang.reflect.InvocationTargetException;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;
import io.appium.espressoserver.lib.model.AppiumParams;

public interface RequestHandler<T extends AppiumParams, R>{
    R handle(T params) throws AppiumException;
}
