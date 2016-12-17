// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class UserValue implements User {

  public static Builder builder() {
    return new AutoValue_UserValue.Builder();
  }

  @Redacted
  @Nullable
  @Override
  public abstract String getPassword();

  @Override
  public abstract ImmutableSet<Bar> getBars();

  @Override
  public abstract ImmutableSet<Book> getBooks();

  @Override
  public abstract ImmutableSet<Recipe> getRecipes();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract UserValue build();

    public abstract Builder setId(Long id);

    public abstract Builder setUsername(String username);

    public abstract Builder setPassword(String password);

    public abstract Builder setDisplayName(String displayName);

    public abstract Builder setEmail(String email);

    abstract ImmutableSet.Builder<Bar> barsBuilder();

    public Builder addBar(Bar bar) {
      barsBuilder().add(bar);
      return this;
    }

    abstract ImmutableSet.Builder<Book> booksBuilder();

    public Builder addBook(Book book) {
      booksBuilder().add(book);
      return this;
    }

    abstract ImmutableSet.Builder<Recipe> recipesBuilder();

    public Builder addRecipe(Recipe recipe) {
      recipesBuilder().add(recipe);
      return this;
    }
  }
}
