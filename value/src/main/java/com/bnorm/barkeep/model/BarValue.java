// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class BarValue implements Bar {

  @Override
  public abstract ImmutableSet<Ingredient> getIngredients();

  public static Builder builder() {
    return new AutoValue_BarValue.Builder();
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
