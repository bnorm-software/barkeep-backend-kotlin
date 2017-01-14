// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class BarValue implements Bar {

  public static Builder builder() {
    return new AutoValue_BarValue.Builder();
  }

  public static JsonAdapter<BarValue> jsonAdapter(Moshi moshi) {
    return new AutoValue_BarValue.MoshiJsonAdapter(moshi);
  }

  @Override
  @Nullable
  public abstract ImmutableSet<Ingredient> getIngredients();

  @Override
  public int compareTo(@NotNull Bar other) {
    return Bar.Companion.getCOMPARATOR().compare(this, other);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract BarValue build();

    public abstract Builder setId(Long id);

    public abstract Builder setTitle(String title);

    public abstract Builder setDescription(String description);

    public abstract Builder setOwner(User owner);

    abstract ImmutableSet.Builder<Ingredient> ingredientsBuilder();

    public Builder addIngredient(Ingredient ingredient) {
      ingredientsBuilder().add(ingredient);
      return this;
    }
  }
}
