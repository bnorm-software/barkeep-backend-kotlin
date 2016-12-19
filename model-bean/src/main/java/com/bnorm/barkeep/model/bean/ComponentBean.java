// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean;

import javax.annotation.Nullable;

import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Ingredient;

public class ComponentBean implements Component {

  private Ingredient ingredient;
  private double min;
  private Double max;
  private long componentNum;
  private long order;

  @Nullable
  @Override
  public Ingredient getIngredient() {
    return ingredient;
  }

  public void setIngredient(Ingredient ingredient) {
    this.ingredient = ingredient;
  }

  @Override
  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  @Nullable
  @Override
  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }

  @Override
  public long getComponentNum() {
    return componentNum;
  }

  public void setComponentNum(long componentNum) {
    this.componentNum = componentNum;
  }

  @Override
  public long getOrder() {
    return order;
  }

  public void setOrder(long order) {
    this.order = order;
  }
}