// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.value

import com.bnorm.barkeep.model.Component
import com.bnorm.barkeep.model.Ingredient
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class ComponentValue(override val ingredient: Ingredient,
                          override val min: Double,
                          override val max: Double? = null,
                          override val componentNum: Long,
                          override val order: Long) : Component

object ComponentValueAdapter {
  @ToJson fun toJson(component: Component): ComponentValue {
    return ComponentValue(component.ingredient, component.min, component.max, component.componentNum, component.order)
  }

  @FromJson fun fromJson(json: ComponentValue): Component {
    return json
  }
}
