// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

interface BarSpec {

  @get:JsonView(Bar::class, User::class)
  val title: String?

  @get:JsonView(Bar::class, User::class)
  val description: String?

  @get:JsonView(Bar::class)
  val owner: User?
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Bar : BarSpec, HasId, Comparable<Bar> {

  @get:JsonView(Bar::class)
  val ingredients: Set<Ingredient>?

  override fun compareTo(other: Bar): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
