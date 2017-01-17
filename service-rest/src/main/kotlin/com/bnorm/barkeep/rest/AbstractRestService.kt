// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.model.BarSpec
import com.bnorm.barkeep.model.BookSpec
import com.bnorm.barkeep.model.RecipeSpec
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.security.UserEntityDetails
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.ResponseStatus

abstract class AbstractRestService {

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  internal class BadRequest(s: String) : RuntimeException(s)


  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  internal class NotFound(s: String) : RuntimeException(s)

  fun currentUser(): User {
    return (SecurityContextHolder.getContext().authentication.principal as UserEntityDetails).user
  }

  fun isOwnedBy(bar: BarSpec?, userId: Long): Boolean {
    return bar?.owner?.id == userId
  }

  fun isOwnedBy(book: BookSpec?, userId: Long): Boolean {
    return book?.owner?.id == userId
  }

  fun isOwnedBy(recipe: RecipeSpec?, userId: Long): Boolean {
    return recipe?.owner?.id == userId
  }
}
