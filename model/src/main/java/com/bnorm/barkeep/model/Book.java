package com.bnorm.barkeep.model;

import java.util.Set;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Book extends HasId, Comparable<Book> {

  @JsonView({Book.class, User.class})
  @Nullable
  String getTitle();

  @JsonView({Book.class, User.class})
  @Nullable
  String getDescription();

  @JsonView({Book.class})
  @Nullable
  User getOwner();

  @JsonView(Book.class)
  Set<Recipe> getRecipes();

  @Override
  default int compareTo(Book o) {
    return COMPARATOR.compare(this, o);
  }
}
