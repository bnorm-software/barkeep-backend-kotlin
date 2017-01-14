// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface User : HasId, Comparable<User> {

  val username: String?
    @JsonView(User::class) get

  val password: String?

  val displayName: String?
    @JsonView(HasId::class) get

  val email: String?
    @JsonView(User::class) get

  val bars: Set<Bar>?
    @JsonView(User::class) get

  val books: Set<Book>?
    @JsonView(User::class) get

  val recipes: Set<Recipe>?
    @JsonView(User::class) get

  override fun compareTo(other: User): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
