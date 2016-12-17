// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ComponentValue implements Component {

  public static Builder builder() {
    return new AutoValue_ComponentValue.Builder();
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
