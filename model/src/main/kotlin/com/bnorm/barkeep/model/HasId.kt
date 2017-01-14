package com.bnorm.barkeep.model

import com.fasterxml.jackson.annotation.JsonView
import java.util.Comparator
import java.util.function.Function

interface HasId {

  val id: Long?
    @JsonView(HasId::class) get

  companion object {

    val COMPARATOR: Comparator<HasId> = Comparator.comparing(Function<HasId, Long> { it?.id })
  }
}
