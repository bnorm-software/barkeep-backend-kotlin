// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import org.jetbrains.annotations.NotNull;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class IngredientValue implements Ingredient {

  public static Builder builder() {
    return new AutoValue_IngredientValue.Builder();
  }

  public static JsonAdapter<IngredientValue> jsonAdapter(Moshi moshi) {
    return new AutoValue_IngredientValue.MoshiJsonAdapter(moshi);
  }

  @Override
  public int compareTo(@NotNull Ingredient other) {
    return Ingredient.Companion.getCOMPARATOR().compare(this, other);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract IngredientValue build();

    public abstract Builder setId(Long id);

    public abstract Builder setTitle(String title);

    public abstract Builder setParent(Ingredient ingredient);
  }
}
