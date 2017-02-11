// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.value

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.BarSpec
import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class BarSpecValue(override val title: String?,
                        override val description: String? = null,
                        override val owner: User? = null) : BarSpec

data class BarValue(override val id: Long = -1,
                    override val title: String?,
                    override val description: String? = null,
                    override val owner: User? = null,
                    override val ingredients: Set<Ingredient> = emptySet()) : Bar

object BarValueAdapter {
  @ToJson fun toJson(bar: BarSpec): BarSpecValue {
    return BarSpecValue(bar.title, bar.description, bar.owner)
  }

  @FromJson fun fromJson(json: BarSpecValue): BarSpec {
    return json
  }

  @ToJson fun toJson(bar: Bar): BarValue {
    return BarValue(bar.id, bar.title, bar.description, bar.owner, bar.ingredients)
  }

  @FromJson fun fromJson(json: BarValue): Bar {
    return json
  }
}
