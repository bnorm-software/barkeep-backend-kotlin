// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.db.DbRecipeService
import com.bnorm.barkeep.db.DbUserService
import com.bnorm.barkeep.model.Component
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.service.RecipeService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recipes")
class RestRecipeService
@Autowired
constructor(private val userService: DbUserService,
            private val recipeService: DbRecipeService) : AbstractRestService(), RecipeService {

  private fun findByOwner(userId: Long, recipeId: Long): Recipe {
    val recipe = recipeService.getRecipe(recipeId)
    if (!isOwnedBy(recipe, userId)) {
      throw NotFound("Unable to find recipe with id=$recipeId")
    }
    return recipe!!
  }

  private fun findByUser(user: User?, recipeId: Long): Recipe {
    val recipe = recipeService.getRecipe(recipeId)
    val recipes = user?.recipes
    if (recipe == null || recipes != null && !recipes.contains(recipe)) {
      throw NotFound("Unable to find recipe with id=$recipeId")
    }
    return recipe
  }

  @JsonView(Recipe::class)
  @RequestMapping(method = arrayOf(RequestMethod.GET))
  override fun getRecipes(): Collection<Recipe> {
    val user = userService.getUser(currentUser().id!!)
    return user!!.recipes
  }

  @JsonView(Recipe::class)
  @RequestMapping(value = "/{recipeId}", method = arrayOf(RequestMethod.GET))
  override fun getRecipe(@PathVariable("recipeId") id: Long): Recipe? {
    val user = userService.getUser(currentUser().id!!)
    return findByUser(user, id)
  }

  @JsonView(Recipe::class)
  @RequestMapping(method = arrayOf(RequestMethod.POST))
  override fun createRecipe(@RequestBody recipe: Recipe): Recipe {
    if (recipe.id != null) {
      throw BadRequest("Cannot create recipe with existing id=$recipe.id")
    }
    val currentUser = currentUser()
    if (recipe.owner == null) {
      return recipeService.createRecipe(RecipeWithOwner(recipe, currentUser))
    } else if (!isOwnedBy(recipe, currentUser.id!!)) {
      throw BadRequest("Cannot create recipe owned by another user")
    } else {
      return recipeService.createRecipe(recipe)
    }
  }

  @JsonView(Recipe::class)
  @RequestMapping(value = "/{recipeId}", method = arrayOf(RequestMethod.PUT))
  override fun setRecipe(@PathVariable("recipeId") id: Long, @RequestBody recipe: Recipe): Recipe {
    findByOwner(currentUser().id!!, id)
    return recipeService.setRecipe(id, recipe)
  }

  @RequestMapping(value = "/{recipeId}", method = arrayOf(RequestMethod.DELETE))
  override fun deleteRecipe(@PathVariable("recipeId") id: Long) {
    findByOwner(currentUser().id!!, id)
    recipeService.deleteRecipe(id)
  }

  private class RecipeWithOwner(private val recipe: Recipe, override val owner: User) : Recipe {
    override val id: Long?
      get() = recipe.id
    override val title: String?
      get() = recipe.title
    override val description: String?
      get() = recipe.description
    override val imageUrl: String?
      get() = recipe.imageUrl
    override val instructions: String?
      get() = recipe.instructions
    override val source: String?
      get() = recipe.source
    override val components: Set<Component>?
      get() = recipe.components
  }
}
