// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean

import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User

class BookBean : Book {

  override var id: Long = -1
  override var title: String? = null
  override var description: String? = null
  override var owner: User? = null
  override var recipes: Set<Recipe> = emptySet()
}
