// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.User

interface UserService {

  fun getUser(id: Long): User?

  fun createUser(user: User): User

  fun setUser(userId: Long, user: User): User
}
