// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class ComponentValue(override val ingredient: Ingredient? = null,
                          override val min: Double = 0.0,
                          override val max: Double? = null,
                          override val componentNum: Long = 0,
                          override val order: Long = 0) : Component

object ComponentValueAdapter {
  @ToJson fun toJson(component: Component): ComponentValue {
    return ComponentValue(component.ingredient, component.min, component.max, component.componentNum, component.order)
  }

  @FromJson fun fromJson(json: ComponentValue): Component {
    return json
  }
}
