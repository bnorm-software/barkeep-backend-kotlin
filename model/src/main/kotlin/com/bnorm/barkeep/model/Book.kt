// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Book : HasId, Comparable<Book> {

  val title: String?
    @JsonView(Book::class, User::class) get

  val description: String?
    @JsonView(Book::class, User::class) get

  val owner: User?
    @JsonView(Book::class) get

  val recipes: Set<Recipe>?
    @JsonView(Book::class) get

  override fun compareTo(other: Book): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
