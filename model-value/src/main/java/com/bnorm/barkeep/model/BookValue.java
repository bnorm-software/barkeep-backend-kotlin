// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class BookValue implements Book {

  public static Builder builder() {
    return new AutoValue_BookValue.Builder();
  }

  public static JsonAdapter<BookValue> jsonAdapter(Moshi moshi) {
    return new AutoValue_BookValue.MoshiJsonAdapter(moshi);
  }

  @Override
  @Nullable
  public abstract ImmutableSet<Recipe> getRecipes();

  @Override
  public int compareTo(@NotNull Book other) {
    return Book.Companion.getCOMPARATOR().compare(this, other);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract BookValue build();

    public abstract Builder setId(Long id);

    public abstract Builder setTitle(String title);

    public abstract Builder setDescription(String description);

    public abstract Builder setOwner(User owner);

    abstract ImmutableSet.Builder<Recipe> recipesBuilder();

    public Builder addRecipe(Recipe recipe) {
      recipesBuilder().add(recipe);
      return this;
    }
  }
}
