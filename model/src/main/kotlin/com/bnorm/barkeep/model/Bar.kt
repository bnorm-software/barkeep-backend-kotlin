// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Bar : HasId, Comparable<Bar> {

  val title: String?
    @JsonView(Bar::class, User::class) get

  val description: String?
    @JsonView(Bar::class, User::class) get

  val owner: User?
    @JsonView(Bar::class) get

  val ingredients: Set<Ingredient>?
    @JsonView(Bar::class) get

  override fun compareTo(other: Bar): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
