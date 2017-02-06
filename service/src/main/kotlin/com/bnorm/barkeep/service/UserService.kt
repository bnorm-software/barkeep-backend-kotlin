// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.UserSpec

interface UserService {

  fun getUser(id: Long): User

  fun createUser(user: UserSpec): User

  fun setUser(id: Long, user: UserSpec): User
}
