// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class RecipeValue implements Recipe {

  public static Builder builder() {
    return new AutoValue_RecipeValue.Builder();
  }

  public static JsonAdapter<RecipeValue> jsonAdapter(Moshi moshi) { return new AutoValue_RecipeValue.MoshiJsonAdapter(moshi); }

  @Override
  @Nullable
  public abstract ImmutableSet<Component> getComponents();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract RecipeValue build();

    public abstract Builder setId(Long id);

    public abstract Builder setTitle(String title);

    public abstract Builder setDescription(String description);

    public abstract Builder setOwner(User owner);

    public abstract Builder setImageUrl(String imageUrl);

    public abstract Builder setInstructions(String instructions);

    public abstract Builder setSource(String source);

    abstract ImmutableSet.Builder<Component> componentsBuilder();

    public Builder addComponent(Component component) {
      componentsBuilder().add(component);
      return this;
    }

    public ComponentValueBuilder newComponentBuilder() {
      return new ComponentValueBuilder(this);
    }
  }


  public static class ComponentValueBuilder extends ComponentValue.Builder {

    private final RecipeValue.Builder parent;
    private final ComponentValue.Builder builder;

    public ComponentValueBuilder(Builder parent) {
      this.parent = parent;
      this.builder = ComponentValue.builder();
    }

    public Builder finish() {
      parent.addComponent(build());
      return parent;
    }

    @Override
    ComponentValue build() {
      return builder.build();
    }

    @Override
    public ComponentValueBuilder setIngredient(Ingredient ingredient) {
      builder.setIngredient(ingredient);
      return this;
    }

    @Override
    public ComponentValueBuilder setMin(double min) {
      builder.setMin(min);
      return this;
    }

    @Override
    public ComponentValueBuilder setMax(Double max) {
      builder.setMax(max);
      return this;
    }

    @Override
    public ComponentValueBuilder setComponentNum(long componentNum) {
      builder.setComponentNum(componentNum);
      return this;
    }

    @Override
    public ComponentValueBuilder setOrder(long order) {
      builder.setOrder(order);
      return this;
    }
  }
}
