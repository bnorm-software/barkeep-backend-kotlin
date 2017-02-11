// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import com.bnorm.barkeep.service.db.DbUserService
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.UserSpec
import com.bnorm.barkeep.security.UserEntityDetails
import com.bnorm.barkeep.service.UserService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class RestUserService(private val userService: DbUserService) : UserService {

  fun currentId(): Long = (SecurityContextHolder.getContext().authentication.principal as UserEntityDetails).user.id

  private fun assertAccess(id: Long) {
    if (id != currentId()) throw NotFound("Unable to find user with id=$id")
  }

  @JsonView(User::class)
  @GetMapping("/{userId}")
  override fun getUser(@PathVariable("userId") id: Long): User {
    assertAccess(id)
    return userService.getUser(id)
  }

  @JsonView(User::class)
  @GetMapping("/current")
  fun getCurrentUser(): User {
    return userService.getUser(currentId())
  }

  @JsonView(User::class)
  @PostMapping
  override fun createUser(@RequestBody user: UserSpec): User {
    return userService.createUser(user)
  }

  @JsonView(User::class)
  @PutMapping("/{userId}")
  override fun setUser(@PathVariable("userId") id: Long, @RequestBody user: UserSpec): User {
    assertAccess(id)
    return userService.setUser(id, user)
  }
}
