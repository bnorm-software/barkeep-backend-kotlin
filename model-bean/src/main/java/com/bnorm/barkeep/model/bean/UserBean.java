// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean;

import java.util.Set;

import javax.annotation.Nullable;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;

public class UserBean implements User {

  private Long id;
  private String username;
  private String password;
  private String displayName;
  private String email;
  private Set<Bar> bars;
  private Set<Book> books;
  private Set<Recipe> recipes;

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
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Nullable
  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Nullable
  @Override
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @Nullable
  @Override
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Nullable
  @Override
  public Set<Bar> getBars() {
    return bars;
  }

  public void setBars(Set<Bar> bars) {
    this.bars = bars;
  }

  @Nullable
  @Override
  public Set<Book> getBooks() {
    return books;
  }

  public void setBooks(Set<Book> books) {
    this.books = books;
  }

  @Nullable
  @Override
  public Set<Recipe> getRecipes() {
    return recipes;
  }

  public void setRecipes(Set<Recipe> recipes) {
    this.recipes = recipes;
  }
}
