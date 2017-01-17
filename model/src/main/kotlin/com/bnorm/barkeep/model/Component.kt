// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView
import java.util.Comparator
import java.util.function.Function

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Component : Comparable<Component> {

  @get:JsonView(Any::class)
  val ingredient: Ingredient?

  @get:JsonView(Any::class)
  val min: Double

  @get:JsonView(Any::class)
  val max: Double?

  @get:JsonView(Any::class)
  val componentNum: Long

  @get:JsonView(Any::class)
  val order: Long

  override fun compareTo(other: Component): Int {
    return COMPARATOR.compare(this, other)
  }

  companion object {

    val COMPARATOR: Comparator<Component> =
            Comparator.comparing(Function<Component, Long> { it?.order })
                    .thenComparing(Function<Component, Long> { it?.componentNum })
  }
}
