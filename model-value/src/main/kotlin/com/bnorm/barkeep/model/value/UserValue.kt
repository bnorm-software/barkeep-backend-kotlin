// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.value

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.UserSpec
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class UserSpecValue(override val username: String?,
                         override val password: String? = null,
                         override val displayName: String? = null,
                         override val email: String? = null) : UserSpec

data class UserValue(override val id: Long = -1,
                     override val username: String?,
                     override val password: String? = null,
                     override val displayName: String? = null,
                     override val email: String? = null,
                     override val bars: Set<Bar> = emptySet(),
                     override val books: Set<Book> = emptySet(),
                     override val recipes: Set<Recipe> = emptySet()) : User

object UserValueAdapter {
  @ToJson fun toJson(user: UserSpec): UserSpecValue {
    return UserSpecValue(user.username,
                         user.password,
                         user.displayName,
                         user.email)
  }

  @FromJson fun fromJson(json: UserSpecValue): UserSpec {
    return json
  }

  @ToJson fun toJson(user: User): UserValue {
    return UserValue(user.id,
                     user.username,
                     user.password,
                     user.displayName,
                     user.email,
                     user.bars,
                     user.books,
                     user.recipes)
  }

  @FromJson fun fromJson(json: UserValue): User {
    return json
  }
}
