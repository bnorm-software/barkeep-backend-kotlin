package com.bnorm.barkeep.model;

import java.util.Set;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Bar extends HasId, Comparable<Bar> {

  @JsonView({Bar.class, User.class})
  @Nullable
  String getTitle();

  @JsonView({Bar.class, User.class})
  @Nullable
  String getDescription();

  @JsonView({Bar.class})
  @Nullable
  User getOwner();

  @JsonView({Bar.class})
  @Nullable
  Set<Ingredient> getIngredients();

  @Override
  default int compareTo(Bar o) {
    return COMPARATOR.compare(this, o);
  }
}
