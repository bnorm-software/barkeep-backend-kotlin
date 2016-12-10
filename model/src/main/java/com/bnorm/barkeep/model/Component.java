package com.bnorm.barkeep.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.annotation.Nullable;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Component {

  @JsonView(Object.class)
  Ingredient getIngredient();

  @JsonView(Object.class)
  double getMin();

  @JsonView(Object.class)
  @Nullable
  Double getMax();

  @JsonView(Object.class)
  long getComponentNum();

  @JsonView(Object.class)
  long getOrder();
}
