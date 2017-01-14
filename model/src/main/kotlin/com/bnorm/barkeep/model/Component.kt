// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonView
import java.util.Comparator
import java.util.function.Function

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface Component : Comparable<Component> {

  val ingredient: Ingredient?
    @JsonView(Any::class) get

  val min: Double
    @JsonView(Any::class) get

  val max: Double?
    @JsonView(Any::class) get

  val componentNum: Long
    @JsonView(Any::class) get

  val order: Long
    @JsonView(Any::class) get

  override fun compareTo(other: Component): Int {
    return COMPARATOR.compare(this, other)
  }

  companion object {

    val COMPARATOR: Comparator<Component> =
            Comparator.comparing(Function<Component, Long> { it?.order })
                    .thenComparing(Function<Component, Long> { it?.componentNum })
  }
}
