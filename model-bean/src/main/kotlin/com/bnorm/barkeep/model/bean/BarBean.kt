// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.User

class BarBean : Bar {

  override var id: Long = -1
  override var title: String? = null
  override var description: String? = null
  override var owner: User? = null
  override var ingredients: Set<Ingredient> = emptySet()
}
