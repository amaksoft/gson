/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import com.google.gson.annotations.CustomDateFormat;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This type adapter factory supports three subclasses of date: {@link Date}, {@link Timestamp}, and
 * {@link java.sql.Date}. {@link CustomDateFormat} annotation is supported.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
final class DefaultDateTypeAdapterFactory implements ParametrizedTypeAdapterFactory {

  private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

  private static List<Class> supported  = Arrays.asList(new Class[] {Date.class, Timestamp.class, java.sql.Date.class});

  private final DateFormat enUsFormat;
  private final DateFormat localFormat;

  DefaultDateTypeAdapterFactory() {
    this(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
      DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
  }

  DefaultDateTypeAdapterFactory(String datePattern) {
    this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
  }

  DefaultDateTypeAdapterFactory(int style) {
    this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
  }

  public DefaultDateTypeAdapterFactory(int dateStyle, int timeStyle) {
    this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US),
      DateFormat.getDateTimeInstance(dateStyle, timeStyle));
  }

  DefaultDateTypeAdapterFactory(DateFormat enUsFormat, DateFormat localFormat) {
    this.enUsFormat = enUsFormat;
    this.localFormat = localFormat;
  }

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    return create(gson, type, new Annotation[]{});
  }

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type, Annotation[] annotations) {

    if (!supported.contains(type.getRawType())) {
      return null;
    }

    SimpleDateFormat customFormat = null;
    for (Annotation annotation : annotations) {
      if (annotation instanceof CustomDateFormat) {
        customFormat = processDateFormatAnnotation((CustomDateFormat) annotation);
        break;
      }
    }

    if (customFormat == null && type.getRawType().isAnnotationPresent(CustomDateFormat.class)) {
      customFormat = processDateFormatAnnotation(type.getRawType().getAnnotation(CustomDateFormat.class));
    }

    return new GeneralDateTypeAdapter<T>(type, enUsFormat, localFormat, customFormat);
  }

  private static SimpleDateFormat processDateFormatAnnotation(CustomDateFormat dateFormatAnnotation) {
    String pattern = dateFormatAnnotation.value();
    Locale locale;
    if (!dateFormatAnnotation.locale().isEmpty()) {
      locale = localeFromString(dateFormatAnnotation.locale());
    } else {
      locale = Locale.getDefault();
    }
    return new  SimpleDateFormat(pattern, locale);
  }

  private static Locale localeFromString(String localeString) {
    String parts[] = localeString.split("_");
    if (parts.length == 0 || parts.length > 3)
      throw new IllegalArgumentException("Invalid locale string: \"" + localeString + "\"");
    if (parts.length == 1) return new Locale(parts[0]);
    else if (parts.length == 2
      || (parts.length == 3 && parts[2].startsWith("#")))
      return new Locale(parts[0], parts[1]);
    else return new Locale(parts[0], parts[1], parts[2]);
  }

  @Override
  public String toString() {
    return SIMPLE_NAME +
      '(' + localFormat.getClass().getSimpleName() + ')';
  }
}
