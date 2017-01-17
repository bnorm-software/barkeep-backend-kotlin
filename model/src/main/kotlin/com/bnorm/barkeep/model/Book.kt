// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

interface BookSpec {

  @get:JsonView(Book::class, User::class)
  val title: String?

  @get:JsonView(Book::class, User::class)
  val description: String?

  @get:JsonView(Book::class)
  val owner: User?
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Book : BookSpec, HasId, Comparable<Book> {

  @get:JsonView(Book::class)
  val recipes: Set<Recipe>?

  override fun compareTo(other: Book): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
