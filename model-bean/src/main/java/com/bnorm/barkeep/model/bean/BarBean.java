// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean;

import java.util.Set;

import javax.annotation.Nullable;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.User;

public class BarBean implements Bar {

  private Long id;
  private String title;
  private String description;
  private User owner;
  private Set<Ingredient> ingredients;

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
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Nullable
  @Override
  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  @Nullable
  @Override
  public Set<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(Set<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }
}
