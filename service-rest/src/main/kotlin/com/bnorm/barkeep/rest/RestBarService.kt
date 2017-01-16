// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.db.DbBarService
import com.bnorm.barkeep.db.DbUserService
import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.service.BarService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bars")
class RestBarService(private val userService: DbUserService,
                     private val barService: DbBarService) : AbstractRestService(), BarService {

  private fun findByOwner(userId: Long, barId: Long): Bar? {
    val bar = barService.getBar(barId)
    if (!isOwnedBy(bar, userId)) {
      throw NotFound("Unable to find bar with id=$barId")
    }
    return bar
  }

  private fun findByUser(user: User?, barId: Long): Bar {
    val bar = barService.getBar(barId)
    val bars = user?.bars
    if (bar == null || bars != null && !bars.contains(bar)) {
      throw NotFound("Unable to find bar with id=$barId")
    }
    return bar
  }

  @JsonView(Bar::class)
  @GetMapping
  override fun getBars(): Collection<Bar> {
    val user = userService.getUser(currentUser().id!!)
    return user!!.bars
  }

  @JsonView(Bar::class)
  @GetMapping("/{barId}")
  override fun getBar(@PathVariable("barId") id: Long): Bar? {
    val user = userService.getUser(currentUser().id!!)
    return findByUser(user, id)
  }

  @JsonView(Recipe::class)
  @GetMapping("/{barId}/ingredients")
  fun getBookRecipes(@PathVariable("barId") barId: Long): Set<Ingredient> {
    val bar = getBar(barId)
    return bar!!.ingredients!!
  }

  @JsonView(Bar::class)
  @PostMapping
  override fun createBar(@RequestBody bar: Bar): Bar {
    if (bar.id != null) {
      throw BadRequest("Cannot create bar with existing id=$bar.id")
    }
    val currentUser = currentUser()
    if (bar.owner == null) {
      return barService.createBar(BarWithOwner(bar, currentUser))
    } else if (!isOwnedBy(bar, currentUser.id!!)) {
      throw BadRequest("Cannot create bar owned by another user")
    } else {
      return barService.createBar(bar)
    }
  }

  @JsonView(Bar::class)
  @PutMapping("/{barId}")
  override fun setBar(@PathVariable("barId") id: Long, @RequestBody bar: Bar): Bar {
    findByOwner(currentUser().id!!, id)
    return barService.setBar(id, bar)
  }

  @DeleteMapping("/{barId}")
  override fun deleteBar(@PathVariable("barId") id: Long) {
    findByOwner(currentUser().id!!, id)
    barService.deleteBar(id)
  }

  private class BarWithOwner(private val bar: Bar, override val owner: User) : Bar {
    override val id: Long?
      get() = bar.id
    override val title: String?
      get() = bar.title
    override val description: String?
      get() = bar.description
    override val ingredients: Set<Ingredient>?
      get() = bar.ingredients
  }
}
