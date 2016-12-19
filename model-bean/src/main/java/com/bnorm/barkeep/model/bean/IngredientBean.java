// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean;

import javax.annotation.Nullable;

import com.bnorm.barkeep.model.Ingredient;

public class IngredientBean implements Ingredient {

  private Long id;
  private String title;
  private Ingredient parent;

  @Nullable
  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Nullable
  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Nullable
  @Override
  public Ingredient getParent() {
    return parent;
  }

  public void setParent(Ingredient parent) {
    this.parent = parent;
  }
}
