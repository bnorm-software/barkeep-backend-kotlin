// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Ingredient : HasId, Comparable<Ingredient> {

  val title: String?
  @JsonView(Ingredient::class, Recipe::class) get

  val parent: Ingredient?
  @JsonView(Ingredient::class) get

  override fun compareTo(other: Ingredient): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
