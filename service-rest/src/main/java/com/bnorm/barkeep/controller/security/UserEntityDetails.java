// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.controller.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bnorm.barkeep.db.UserEntity;
import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;

class UserEntityDetails implements UserDetails, User {

  private final UserEntity user;

  UserEntityDetails(UserEntity user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  // User

  @Override
  public Long getId() {
    return user.getId();
  }

  @Override
  public String getDisplayName() {
    return user.getDisplayName();
  }

  @Override
  public String getEmail() {
    return user.getEmail();
  }

  @Override
  public Set<Bar> getBars() {
    return user.getBars();
  }

  @Override
  public Set<Book> getBooks() {
    return user.getBooks();
  }

  @Override
  public Set<Recipe> getRecipes() {
    return user.getRecipes();
  }
}
