// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Recipe : HasId, Comparable<Recipe> {

  val title: String?
    @JsonView(Recipe::class, Book::class) get

  val description: String?
    @JsonView(Recipe::class, Book::class) get

  val owner: User?
    @JsonView(Recipe::class) get

  val imageUrl: String?
    @JsonView(Recipe::class, Book::class) get

  val instructions: String?
    @JsonView(Recipe::class) get

  val source: String?
    @JsonView(Recipe::class) get

  val components: Set<Component>?
    @JsonView(Recipe::class) get

  override fun compareTo(other: Recipe): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
