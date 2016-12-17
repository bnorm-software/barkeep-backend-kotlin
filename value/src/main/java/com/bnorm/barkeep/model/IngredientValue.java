// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class IngredientValue implements Ingredient {

  public static Builder builder() {
    return new AutoValue_IngredientValue.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract IngredientValue build();

    public abstract Builder setId(Long id);

    public abstract Builder setTitle(String title);

    public abstract Builder setParent(Ingredient ingredient);
  }
}
