// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.Bar

interface BarService {

  fun getBars(): Collection<Bar>

  fun getBar(id: Long): Bar?

  fun createBar(bar: Bar): Bar

  fun setBar(id: Long, bar: Bar): Bar

  fun deleteBar(id: Long)
}
