// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean

import com.bnorm.barkeep.model.Ingredient

class IngredientBean : Ingredient {

  override var id: Long = -1
  override var title: String? = null
  override var parent: Ingredient? = null
}
