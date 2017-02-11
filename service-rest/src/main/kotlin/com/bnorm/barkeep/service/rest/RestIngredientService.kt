// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import com.bnorm.barkeep.service.db.DbIngredientService
import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.IngredientSpec
import com.bnorm.barkeep.service.IngredientService
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
@RequestMapping("/ingredients")
class RestIngredientService(private val ingredientService: DbIngredientService) : IngredientService {

  private fun assertOwnership(ingredient: Ingredient) {
  }

  @JsonView(Ingredient::class)
  @GetMapping
  override fun getIngredients(): Collection<Ingredient> {
    return ingredientService.getIngredients()
  }

  @JsonView(Ingredient::class)
  @GetMapping("/{ingredientId}")
  override fun getIngredient(@PathVariable("ingredientId") id: Long): Ingredient {
    return ingredientService.findIngredient(id) ?: throw NotFound(MSG_NOT_FOUND(TYPE_INGREDIENT, id))
  }

  @JsonView(Ingredient::class)
  @PostMapping
  override fun createIngredient(@RequestBody ingredient: IngredientSpec): Ingredient {
    return ingredientService.createIngredient(ingredient)
  }

  @JsonView(Ingredient::class)
  @PutMapping("/{ingredientId}")
  override fun setIngredient(@PathVariable("ingredientId") id: Long,
                             @RequestBody ingredient: IngredientSpec): Ingredient {
    assertOwnership(getIngredient(id))
    return ingredientService.setIngredient(id, ingredient)
  }

  @DeleteMapping("/{ingredientId}")
  override fun deleteIngredient(@PathVariable("ingredientId") id: Long) {
    assertOwnership(getIngredient(id))
    return ingredientService.deleteIngredient(id)
  }
}
