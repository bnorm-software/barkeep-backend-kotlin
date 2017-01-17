// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.UserSpec
import com.bnorm.barkeep.service.UserService
import javax.persistence.EntityManager

class DbUserService(entityManager: EntityManager) : AbstractDbService(entityManager), UserService {

  override fun getUser(id: Long): UserEntity? {
    return super.getUser(id)
  }

  override fun createUser(user: UserSpec): UserEntity {
    val userEntity = UserEntity()
    userEntity.username = user.username
    userEntity.password = user.password
    userEntity.email = user.email
    userEntity.displayName = user.displayName

    txn {
      em.persist(userEntity)
    }
    return userEntity
  }

  override fun setUser(userId: Long, user: UserSpec): UserEntity {
    val userEntity = requireExists(getUser(userId), userId, "user")

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

      em.merge(userEntity)
    }
    return userEntity
  }
}
