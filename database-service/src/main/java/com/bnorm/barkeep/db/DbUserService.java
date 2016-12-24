// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.UserService;

public class DbUserService extends AbstractDbService implements UserService {

  public DbUserService(EntityManager entityManager) {
    super(entityManager);
  }

  @Override
  public UserEntity getUser(long userId) {
    return super.getUser(userId);
  }

  @Override
  public UserEntity createUser(User user) {
    if (user.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create user that already has an id=%d", user.getId()));
    }

    return transaction(em -> {
      UserEntity userEntity = new UserEntity();
      userEntity.setUsername(user.getUsername());
      userEntity.setPassword(user.getPassword());
      userEntity.setEmail(user.getEmail());
      userEntity.setDisplayName(user.getDisplayName());
      if (user.getBars() != null) {
        for (Bar bar : user.getBars()) {
          userEntity.addBar(find(bar));
        }
      }
      if (user.getBooks() != null) {
        for (Book book : user.getBooks()) {
          userEntity.addBook(find(book));
        }
      }

      em.persist(userEntity);
      return userEntity;
    });
  }

  @Override
  public UserEntity setUser(long userId, User user) {
    UserEntity userEntity = getUser(userId);
    requireExists(userEntity, userId, "user");
    requireMatch(user, userId, "user");

    return transaction(em -> {
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
          userEntity.addBar(find(bar));
        }
      }
      if (user.getBooks() != null) {
        // todo is this correct?
        for (Book book : user.getBooks()) {
          userEntity.addBook(find(book));
        }
      }

      em.merge(userEntity);
      return userEntity;
    });
  }
}
