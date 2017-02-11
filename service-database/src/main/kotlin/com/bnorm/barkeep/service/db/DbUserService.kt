// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.UserSpec
import com.bnorm.barkeep.model.db.UserEntity
import com.bnorm.barkeep.service.UserService
import javax.persistence.EntityManager

class DbUserService(private val em: EntityManager) : UserService {

  fun findUser(id: Long): UserEntity? {
    return em.find(UserEntity::class.java, id)
  }

  override fun getUser(id: Long): UserEntity {
    return findUser(id) ?: throw IllegalArgumentException("Cannot find user with id=%$id")
  }

  override fun createUser(user: UserSpec): UserEntity {
    val userEntity = UserEntity()
    userEntity.username = user.username
    userEntity.password = user.password
    userEntity.email = user.email
    userEntity.displayName = user.displayName

    em.txn {
      em.persist(userEntity)
    }
    return userEntity
  }

  override fun setUser(id: Long, user: UserSpec): UserEntity {
    val userEntity = getUser(id)

    em.txn {
      user.username?.apply { userEntity.username = this }
      user.password?.apply { userEntity.password = this }
      user.email?.apply { userEntity.email = this }
      user.displayName?.apply { userEntity.displayName = this }
    }
    return userEntity
  }
}
