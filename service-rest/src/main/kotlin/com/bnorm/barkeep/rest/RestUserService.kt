// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.db.DbUserService
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.service.UserService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class RestUserService
@Autowired
constructor(val userService: DbUserService) : AbstractRestService(), UserService {

  @JsonView(User::class)
  @RequestMapping(value = "/{userId}", method = arrayOf(RequestMethod.GET))
  override fun getUser(@PathVariable("userId") id: Long): User? {
    return userService.getUser(id)
  }

  @JsonView(User::class)
  @RequestMapping(value = "/current", method = arrayOf(RequestMethod.GET))
  fun getUser(): User {
    val id = currentUser().id
    return getUser(id!!)!!
  }

  @JsonView(User::class)
  @RequestMapping(method = arrayOf(RequestMethod.POST))
  override fun createUser(@RequestBody user: User): User {
    return userService.createUser(user)
  }

  @JsonView(User::class)
  @RequestMapping(value = "/{userId}", method = arrayOf(RequestMethod.PUT))
  override fun setUser(@PathVariable("userId") userId: Long, @RequestBody user: User): User {
    if (user.id == null || user.id !== userId) {
      throw BadRequest("Cannot update user with a different id=" + user.id + " then existing id=" + userId)
    }
    return userService.setUser(userId, user)
  }
}
