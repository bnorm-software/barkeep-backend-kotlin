// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.UserService;

public class DbUserService implements UserService {

  private final EntityManager em;

  public DbUserService(EntityManager entityManager) {
    this.em = entityManager;
  }

  private BookEntity entity(Book book) {
    return em.find(BookEntity.class, book.getId());
  }

  private BarEntity entity(Bar bar) {
    return em.find(BarEntity.class, bar.getId());
  }

  @Override
  public UserEntity getUser(long userId) {
    return em.find(UserEntity.class, userId);
  }

  @Override
  public UserEntity createUser(User user) {
    if (user.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create user that already has an id=%d", user.getId()));
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(user.getUsername());
    userEntity.setPassword(user.getPassword());
    userEntity.setEmail(user.getEmail());
    userEntity.setDisplayName(user.getDisplayName());
    for (Bar bar : user.getBars()) {
      userEntity.addBar(entity(bar));
    }
    for (Book book : user.getBooks()) {
      userEntity.addBook(entity(book));
    }

    em.getTransaction().begin();
    em.persist(userEntity);
    em.getTransaction().commit();
    return userEntity;
  }

  @Override
  public UserEntity setUser(long userId, User user) {
    UserEntity userEntity = getUser(userId);
    if (userEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find user with id=%d", userId));
    }
    if (user.getId() != null && user.getId() != userId) {
      throw new IllegalArgumentException(String.format("Cannot update user with a different id=%d then existing id=%d",
                                                       user.getId(),
                                                       userId));
    }

    if (user.getUsername() != null) {
      userEntity.setUsername(user.getUsername());
    }
    if (user.getPassword() != null) {
      userEntity.setPassword(user.getPassword());
    }
    if (user.getEmail() != null) {
      userEntity.setEmail(user.getEmail());
    }
    if (user.getDisplayName() != null) {
      userEntity.setDisplayName(user.getDisplayName());
    }
    if (user.getBars() != null) {
      // todo is this correct?
      for (Bar bar : user.getBars()) {
        userEntity.addBar(entity(bar));
      }
    }
    if (user.getBooks() != null) {
      // todo is this correct?
      for (Book book : user.getBooks()) {
        userEntity.addBook(entity(book));
      }
    }

    em.getTransaction().begin();
    em.merge(userEntity);
    em.getTransaction().commit();
    return userEntity;
  }
}
