// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.service.UserService
import javax.persistence.EntityManager

class DbUserService(entityManager: EntityManager) : AbstractDbService(entityManager), UserService {

  override fun getUser(id: Long): UserEntity? {
    return super.getUser(id)
  }

  override fun createUser(user: User): UserEntity {
    if (user.id != null) {
      throw IllegalArgumentException(String.format("Cannot create user that already has an id=%d", user.id))
    }

    val userEntity = UserEntity()
    userEntity.username = user.username
    userEntity.password = user.password
    userEntity.email = user.email
    userEntity.displayName = user.displayName
    if (user.bars != null) {
      for (bar in user.bars!!) {
        userEntity.addBar(find(bar)!!)
      }
    }
    if (user.books != null) {
      for (book in user.books!!) {
        userEntity.addBook(find(book)!!)
      }
    }

    txn {
      em.persist(userEntity)
    }
    return userEntity
  }

  override fun setUser(userId: Long, user: User): UserEntity {
    val userEntity = requireExists(getUser(userId), userId, "user")
    requireMatch(user, userId, "user")

    txn {
      if (user.username != null) {
        userEntity.username = user.username
      }
      if (user.password != null) {
        userEntity.password = user.password
      }
      if (user.email != null) {
        userEntity.email = user.email
      }
      if (user.displayName != null) {
        userEntity.displayName = user.displayName
      }
      if (user.bars != null) {
        // todo is this correct?
        for (bar in user.bars!!) {
          userEntity.addBar(find(bar)!!)
        }
      }
      if (user.books != null) {
        // todo is this correct?
        for (book in user.books!!) {
          userEntity.addBook(find(book)!!)
        }
      }

      em.merge(userEntity)
    }
    return userEntity
  }
}
