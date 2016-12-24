// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

public class ImmutableSetAdapter<T> extends JsonAdapter<ImmutableSet<T>> {

  public static final Factory FACTORY = new Factory() {
    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
      Class<?> rawType = Types.getRawType(type);
      if (!annotations.isEmpty()) {
        return null;
      }
      if (rawType == ImmutableSet.class) {
        return newImmutableSetAdapter(type, moshi).nullSafe();
      }
      return null;
    }
  };

  private final JsonAdapter<T> elementAdapter;

  private ImmutableSetAdapter(JsonAdapter<T> elementAdapter) {
    this.elementAdapter = elementAdapter;
  }

  static <T> JsonAdapter<ImmutableSet<T>> newImmutableSetAdapter(Type type, Moshi moshi) {
    Type elementType = Types.collectionElementType(type, ImmutableSet.class);
    JsonAdapter<T> elementAdapter = moshi.adapter(elementType);
    return new ImmutableSetAdapter<>(elementAdapter);
  }

  @Override
  public ImmutableSet<T> fromJson(JsonReader reader) throws IOException {
    ImmutableSet.Builder<T> builder = ImmutableSet.builder();
    reader.beginArray();
    while (reader.hasNext()) {
      builder.add(elementAdapter.fromJson(reader));
    }
    reader.endArray();
    return builder.build();
  }

  @Override
  public void toJson(JsonWriter writer, ImmutableSet<T> value) throws IOException {
    writer.beginArray();
    for (T element : value) {
      elementAdapter.toJson(writer, element);
    }
    writer.endArray();
  }
}