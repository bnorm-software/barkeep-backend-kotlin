package com.bnorm.barkeep.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.annotation.Nullable;

import java.time.Instant;
import java.util.Comparator;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Ingredient extends HasId, Comparable<Ingredient> {

  @JsonView({Ingredient.class, Recipe.class})
  @Nullable
  String getTitle();

  @JsonView(Ingredient.class)
  @Nullable
  Ingredient getParent();

  @Override
  default int compareTo(Ingredient o) {
    return COMPARATOR.compare(this, o);
  }
}
