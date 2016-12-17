package com.bnorm.barkeep.model;

import java.util.Set;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Recipe extends HasId, Comparable<Recipe> {

  @JsonView({Recipe.class, Book.class})
  @Nullable
  String getTitle();

  @JsonView({Recipe.class, Book.class})
  @Nullable
  String getDescription();

  @Nullable
  User getOwner();

  @JsonView({Recipe.class, Book.class})
  @Nullable
  String getImageUrl();

  @JsonView(Recipe.class)
  @Nullable
  String getInstructions();

  @JsonView(Recipe.class)
  @Nullable
  String getSource();

  @JsonView(Recipe.class)
  Set<Component> getComponents();

  @Override
  default int compareTo(Recipe o) {
    return COMPARATOR.compare(this, o);
  }
}
