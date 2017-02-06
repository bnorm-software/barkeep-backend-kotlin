// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.bean

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User

class UserBean : User {

  override var id: Long = -1
  override var username: String? = null
  override var password: String? = null
  override var displayName: String? = null
  override var email: String? = null
  override var bars: Set<Bar> = emptySet()
  override var books: Set<Book> = emptySet()
  override var recipes: Set<Recipe> = emptySet()
}
