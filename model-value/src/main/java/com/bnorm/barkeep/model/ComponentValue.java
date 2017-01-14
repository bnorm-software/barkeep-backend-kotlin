// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import org.jetbrains.annotations.NotNull;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class ComponentValue implements Component {

  public static Builder builder() {
    return new AutoValue_ComponentValue.Builder();
  }

  public static JsonAdapter<ComponentValue> jsonAdapter(Moshi moshi) {
    return new AutoValue_ComponentValue.MoshiJsonAdapter(moshi);
  }

  @Override
  public int compareTo(@NotNull Component other) {
    return Component.Companion.getCOMPARATOR().compare(this, other);
  }

  @AutoValue.Builder
  abstract static class Builder {

    abstract ComponentValue build();

    abstract Builder setIngredient(Ingredient ingredient);

    abstract Builder setMin(double min);

    abstract Builder setMax(Double max);

    abstract Builder setComponentNum(long componentNum);

    abstract Builder setOrder(long order);
  }
}
