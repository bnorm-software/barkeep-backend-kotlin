package com.bnorm.barkeep.model;

import java.util.Set;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface User extends HasId, Comparable<User> {

  @JsonView(User.class)
  @Nullable
  String getUsername();

  @Nullable
  String getPassword();

  @JsonView(User.class)
  @Nullable
  String getDisplayName();

  @JsonView(User.class)
  @Nullable
  String getEmail();

  @JsonView(User.class)
  Set<Bar> getBars();

  @JsonView(User.class)
  Set<Book> getBooks();

  @JsonView(User.class)
  Set<Recipe> getRecipes();

  @Override
  default int compareTo(User o) {
    return COMPARATOR.compare(this, o);
  }
}
