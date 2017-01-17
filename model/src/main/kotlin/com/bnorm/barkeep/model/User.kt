// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

interface UserSpec {

  @get:JsonView(User::class)
  val username: String?

  val password: String?

  @get:JsonView(HasId::class)
  val displayName: String?

  @get:JsonView(User::class)
  val email: String?
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface User : UserSpec, HasId, Comparable<User> {

  @get:JsonView(User::class)
  val bars: Set<Bar>?

  @get:JsonView(User::class)
  val books: Set<Book>?

  @get:JsonView(User::class)
  val recipes: Set<Recipe>?

  override fun compareTo(other: User): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
