// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean;

import java.util.Set;

import javax.annotation.Nullable;

import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;

public class RecipeBean implements Recipe {

  private Long id;
  private String title;
  private String description;
  private User owner;
  private String imageUrl;
  private String instructions;
  private String source;
  private Set<Component> components;

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
  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  @Nullable
  @Override
  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  @Nullable
  @Override
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  @Nullable
  @Override
  public Set<Component> getComponents() {
    return components;
  }

  public void setComponents(Set<Component> components) {
    this.components = components;
  }
}
