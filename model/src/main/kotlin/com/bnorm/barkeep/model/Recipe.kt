// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Recipe : HasId, Comparable<Recipe> {

  @get:JsonView(Recipe::class, Book::class)
  val title: String?

  @get:JsonView(Recipe::class, Book::class)
  val description: String?

  @get:JsonView(Recipe::class)
  val owner: User?

  @get:JsonView(Recipe::class, Book::class)
  val imageUrl: String?

  @get:JsonView(Recipe::class)
  val instructions: String?

  @get:JsonView(Recipe::class)
  val source: String?

  @get:JsonView(Recipe::class)
  val components: Set<Component>?

  override fun compareTo(other: Recipe): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
