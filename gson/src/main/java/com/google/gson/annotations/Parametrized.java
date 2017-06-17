package com.google.gson.annotations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.*;

/**
 * Makes {@link com.google.gson.Gson} use {@link com.google.gson.ParametrizedTypeAdapterFactory#create(Gson, TypeToken, Annotation[])}
 * @author Andrey Makeev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parametrized {
}
