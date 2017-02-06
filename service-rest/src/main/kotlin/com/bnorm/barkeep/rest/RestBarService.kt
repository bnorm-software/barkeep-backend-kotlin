// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.db.DbBarService
import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.BarSpec
import com.bnorm.barkeep.model.Ingredient
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
class RestBarService(private val barService: DbBarService,
                     private val userService: RestUserService,
                     private val ingredientService: RestIngredientService) : BarService {

  private fun isOwnedBy(bar: BarSpec, userId: Long): Boolean = bar.owner?.id == userId

  private fun assertAccess(bar: Bar) {
    val user = userService.getUser()
    val bars = user.bars
    if (!bars.contains(bar)) {
      throw NotFound(MSG_NOT_FOUND(TYPE_BAR, bar.id))
    }
  }

  private fun assertOwnership(bar: Bar) {
    if (!isOwnedBy(bar, userService.currentId())) {
      throw Forbidden(MSG_DO_NOT_OWN(TYPE_BAR, bar.id))
    }
  }

  @JsonView(Bar::class)
  @GetMapping
  override fun getBars(): Collection<Bar> {
    val user = userService.getUser()
    return user.bars
  }

  @JsonView(Bar::class)
  @GetMapping("/{barId}")
  override fun getBar(@PathVariable("barId") id: Long): Bar {
    val bar = barService.findBar(id) ?: throw NotFound(MSG_NOT_FOUND(TYPE_BAR, id))
    assertAccess(bar)
    return bar
  }

  @JsonView(Ingredient::class)
  @GetMapping("/{barId}/ingredients")
  fun getBarIngredients(@PathVariable("barId") barId: Long): Set<Ingredient> {
    val bar = getBar(barId)
    return bar.ingredients
  }

  @JsonView(Bar::class)
  @PostMapping
  override fun createBar(@RequestBody bar: BarSpec): Bar {
    val currentUser = userService.getUser()
    if (bar.owner == null) {
      return barService.createBar(BarWithOwner(bar, currentUser))
    } else if (!isOwnedBy(bar, currentUser.id)) {
      throw BadRequest(MSG_CREATE_WRONG_USER("bar"))
    } else {
      return barService.createBar(bar)
    }
  }

  @PostMapping("/{barId}/ingredients/{ingredientId}")
  override fun addBarIngredient(@PathVariable("barId") barId: Long, @PathVariable("ingredientId") ingredientId: Long) {
    val bar = getBar(barId)
    val ingredient = ingredientService.getIngredient(ingredientId)
    barService.addBarIngredient(bar.id, ingredient.id)
  }

  @DeleteMapping("/{barId}/ingredients/{ingredientId}")
  override fun removeBarIngredient(@PathVariable("barId") barId: Long,
                                   @PathVariable("ingredientId") ingredientId: Long) {
    val bar = getBar(barId)
    val ingredient = ingredientService.getIngredient(ingredientId)
    barService.removeBarIngredient(bar.id, ingredient.id)
  }

  @JsonView(Bar::class)
  @PutMapping("/{barId}")
  override fun setBar(@PathVariable("barId") id: Long, @RequestBody bar: BarSpec): Bar {
    assertOwnership(getBar(id))
    return barService.setBar(id, bar)
  }

  @DeleteMapping("/{barId}")
  override fun deleteBar(@PathVariable("barId") id: Long) {
    assertOwnership(getBar(id))
    barService.deleteBar(id)
  }

  private class BarWithOwner(private val bar: BarSpec, override val owner: User) : BarSpec {
    override val title: String?
      get() = bar.title
    override val description: String?
      get() = bar.description
  }
}
