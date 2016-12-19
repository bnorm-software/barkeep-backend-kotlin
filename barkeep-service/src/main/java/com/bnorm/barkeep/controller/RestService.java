// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.controller;

import javax.annotation.Nullable;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;

class RestService {

  static User currentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  static boolean match(@Nullable User user, long userId) {
    return user != null && user.getId() != null && user.getId() == userId;
  }

  static boolean isOwnedBy(@Nullable Bar bar, long userId) {
    return bar != null && match(bar.getOwner(), userId);
  }

  static boolean isOwnedBy(Book book, long userId) {
    return match(book.getOwner(), userId);
  }

  static boolean isOwnedBy(Recipe book, long userId) {
    return match(book.getOwner(), userId);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  static class BadRequest extends RuntimeException {

    BadRequest(String s, Object... args) {
      super(String.format(s, args));
    }
  }


  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  static class NotFound extends RuntimeException {

    NotFound(String s, Object... args) {
      super(String.format(s, args));
    }
  }
}
