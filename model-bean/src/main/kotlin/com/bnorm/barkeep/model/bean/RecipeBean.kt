// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean

import com.bnorm.barkeep.model.Component
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User

class RecipeBean : Recipe {

  override var id: Long = -1
  override var title: String? = null
  override var description: String? = null
  override var owner: User? = null
  override var imageUrl: String? = null
  override var instructions: String? = null
  override var source: String? = null
  override var components: Set<Component>? = null
}
