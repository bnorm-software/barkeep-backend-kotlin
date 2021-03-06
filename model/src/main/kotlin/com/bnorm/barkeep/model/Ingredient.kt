// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView

interface IngredientSpec {

  @get:JsonView(Ingredient::class, Recipe::class)
  val title: String?

  @get:JsonView(Ingredient::class)
  @get:JsonIgnoreProperties("parent")
  val parent: Ingredient?
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Ingredient : IngredientSpec, HasId, Comparable<Ingredient> {

  override fun compareTo(other: Ingredient): Int {
    return HasId.COMPARATOR.compare(this, other)
  }
}
