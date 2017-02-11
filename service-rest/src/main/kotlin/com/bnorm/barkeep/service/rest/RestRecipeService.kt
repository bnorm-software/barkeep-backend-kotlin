// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import com.bnorm.barkeep.service.db.DbRecipeService
import com.bnorm.barkeep.model.Component
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.RecipeSpec
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.service.RecipeService
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
@RequestMapping("/recipes")
class RestRecipeService(private val recipeService: DbRecipeService,
                        private val userService: RestUserService) : RecipeService {

  fun isOwnedBy(recipe: RecipeSpec, userId: Long): Boolean = recipe.owner?.id == userId

  private fun assertAccess(recipe: Recipe) {
    val user = userService.getCurrentUser()
    val recipes = user.recipes
    if (!recipes.contains(recipe)) {
      throw NotFound(MSG_NOT_FOUND(TYPE_RECIPE, recipe.id))
    }
  }

  private fun assertOwnership(recipe: Recipe) {
    if (!isOwnedBy(recipe, userService.currentId())) {
      throw Forbidden(MSG_DO_NOT_OWN(TYPE_RECIPE, recipe.id))
    }
  }

  @JsonView(Recipe::class)
  @GetMapping
  override fun getRecipes(): Collection<Recipe> {
    val user = userService.getCurrentUser()
    return user.recipes
  }

  @JsonView(Recipe::class)
  @GetMapping("/{recipeId}")
  override fun getRecipe(@PathVariable("recipeId") id: Long): Recipe {
    val recipe = recipeService.findRecipe(id) ?: throw NotFound(MSG_NOT_FOUND(TYPE_RECIPE, id))
    assertAccess(recipe)
    return recipe
  }

  @JsonView(Recipe::class)
  @PostMapping("/{recipeId}")
  override fun createRecipe(@RequestBody recipe: RecipeSpec): Recipe {
    val currentUser = userService.getCurrentUser()
    if (recipe.owner == null) {
      return recipeService.createRecipe(RecipeWithOwner(recipe, currentUser))
    } else if (!isOwnedBy(recipe, currentUser.id)) {
      throw BadRequest("Cannot create recipe owned by another user")
    } else {
      return recipeService.createRecipe(recipe)
    }
  }

  @JsonView(Recipe::class)
  @PutMapping("/{recipeId}")
  override fun setRecipe(@PathVariable("recipeId") id: Long, @RequestBody recipe: RecipeSpec): Recipe {
    assertOwnership(getRecipe(id))
    return recipeService.setRecipe(id, recipe)
  }

  @DeleteMapping("/{recipeId}")
  override fun deleteRecipe(@PathVariable("recipeId") id: Long) {
    assertOwnership(getRecipe(id))
    recipeService.deleteRecipe(id)
  }

  private class RecipeWithOwner(private val recipe: RecipeSpec, override val owner: User) : RecipeSpec by recipe
}
