// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.BarSpec

interface BarService {

  fun getBars(): Collection<Bar>

  fun getBar(id: Long): Bar

  fun createBar(bar: BarSpec): Bar

  fun addBarIngredient(barId: Long, ingredientId: Long)

  fun removeBarIngredient(barId: Long, ingredientId: Long)

  fun setBar(id: Long, bar: BarSpec): Bar

  fun deleteBar(id: Long)
}
