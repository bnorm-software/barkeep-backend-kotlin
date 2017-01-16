// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.service.RecipeService
import javax.persistence.EntityManager

class DbRecipeService(entityManager: EntityManager) : AbstractDbService(entityManager), RecipeService {

  override fun getRecipes(): List<Recipe> {
    return super.getRecipes()
  }

  override fun getRecipe(id: Long): RecipeEntity? {
    return super.getRecipe(id)
  }

  override fun createRecipe(recipe: Recipe): RecipeEntity {
    if (recipe.id != null) {
      throw IllegalArgumentException("Cannot create recipe that already has an id=$recipe.id")
    } else if (recipe.owner == null) {
      throw IllegalArgumentException("Cannot create recipe without an owner")
    }
    val userEntity = find(recipe.owner!!) ?: throw IllegalArgumentException("Cannot create recipe with an unknown owner id=$recipe.owner!!.id")

    val recipeEntity = RecipeEntity()

    recipeEntity.title = recipe.title
    recipeEntity.description = recipe.description
    recipeEntity.owner = userEntity
    recipeEntity.imageUrl = recipe.imageUrl
    recipeEntity.instructions = recipe.instructions
    recipeEntity.source = recipe.instructions

    if (recipe.components != null) {
      for (component in recipe.components!!) {
        val componentEntity = ComponentEntity()
        componentEntity.ingredient = find(component.ingredient!!)
        componentEntity.min = component.min
        componentEntity.max = component.max
        componentEntity.order = component.order
        componentEntity.componentNum = component.componentNum
        recipeEntity.addComponent(componentEntity)
      }
    }

    txn {
      em.persist(recipeEntity)
      userEntity.addRecipe(recipeEntity)
    }

    return recipeEntity
  }

  override fun setRecipe(id: Long, recipe: Recipe): RecipeEntity {
    val recipeEntity = requireExists(findRecipe(id), id, "recipe")
    requireMatch(recipe, id, "recipe")

    txn {
      if (recipe.title != null) {
        recipeEntity.title = recipe.title
      }
      if (recipe.description != null) {
        recipeEntity.description = recipe.title
      }
      if (recipe.imageUrl != null) {
        recipeEntity.imageUrl = recipe.imageUrl
      }
      if (recipe.instructions != null) {
        recipeEntity.instructions = recipe.instructions
      }
      if (recipe.source != null) {
        recipeEntity.source = recipe.source
      }
      if (recipe.components != null) {
        // todo is this right?
        for (component in recipe.components!!) {
          val componentEntity = ComponentEntity()
          componentEntity.ingredient = find(component.ingredient!!)
          componentEntity.min = component.min
          componentEntity.max = component.max
          componentEntity.order = component.order
          componentEntity.componentNum = component.componentNum
          recipeEntity.addComponent(componentEntity)
        }
      }

    }
    return recipeEntity
  }

  override fun deleteRecipe(id: Long) {
    txn {
      val recipeEntity = findRecipe(id)
      requireExists(recipeEntity, id, "recipe")
      em.remove(recipeEntity)
    }
  }
}
