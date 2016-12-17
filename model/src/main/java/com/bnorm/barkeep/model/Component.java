package com.bnorm.barkeep.model;

import java.util.Comparator;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Component extends Comparable<Component> {

  Comparator<Component> COMPARATOR = Comparator.comparing(Component::getOrder)
                                               .thenComparing(Component::getComponentNum);

  @JsonView(Object.class)
  @Nullable
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

  @Override
  default int compareTo(Component o) {
    return COMPARATOR.compare(this, o);
  }
}
