/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;

/**
 * Behaves like an ordinary {@link TypeAdapterFactory} unless field is annotated with {@link com.google.gson.annotations.Parametrized}.
 * In that case {@link Gson} calls {@link ParametrizedTypeAdapterFactory#create(Gson, TypeToken, Annotation[])} which allows
 * to produce a {@link TypeAdapter} based on field annotations.
 */
public interface ParametrizedTypeAdapterFactory extends TypeAdapterFactory {

  /**
   * Returns a type adapter for {@code type}, or null if this factory doesn't
   * support {@code type}.
   */
  <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type, Annotation[] annotations);
}
