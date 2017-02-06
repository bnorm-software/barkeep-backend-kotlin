// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean

import com.bnorm.barkeep.model.Component
import com.bnorm.barkeep.model.Ingredient

class ComponentBean : Component {

  override var ingredient: Ingredient = IngredientBean()
  override var min: Double = 0.0
  override var max: Double? = null
  override var componentNum: Long = 0
  override var order: Long = 0
}