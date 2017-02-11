// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.RecipeSpec
import com.bnorm.barkeep.model.db.ComponentEntity
import com.bnorm.barkeep.model.db.RecipeEntity
import com.bnorm.barkeep.service.RecipeService
import javax.persistence.EntityManager

class DbRecipeService(private val em: EntityManager,
                      private val ingredientService: DbIngredientService,
                      private val userService: DbUserService) : RecipeService {

  override fun getRecipes(): List<Recipe> {
    return em.createNamedQuery("RecipeEntity.findAll", RecipeEntity::class.java).resultList
  }

  fun findRecipe(id: Long): RecipeEntity? {
    return em.find(RecipeEntity::class.java, id)
  }

  override fun getRecipe(id: Long): RecipeEntity {
    return findRecipe(id) ?: throw IllegalArgumentException("Cannot find recipe with id=$id")
  }

  override fun createRecipe(recipe: RecipeSpec): RecipeEntity {
    val owner = recipe.owner ?: throw IllegalArgumentException("Cannot create recipe without an owner")
    val userEntity = userService.findUser(owner.id) ?: throw IllegalArgumentException("Cannot create recipe with an unknown owner id=${owner.id}")

    val recipeEntity = RecipeEntity()

    recipeEntity.title = recipe.title
    recipeEntity.description = recipe.description
    recipeEntity.owner = userEntity
    recipeEntity.imageUrl = recipe.imageUrl
    recipeEntity.instructions = recipe.instructions
    recipeEntity.source = recipe.instructions
    recipeEntity.addUser(userEntity)

    val components = recipe.components
    if (components != null) {
      for (component in components) {
        val componentEntity = ComponentEntity()
        componentEntity.ingredient = ingredientService.getIngredient(component.ingredient.id)
        componentEntity.min = component.min
        componentEntity.max = component.max
        componentEntity.order = component.order
        componentEntity.componentNum = component.componentNum
        recipeEntity.addComponent(componentEntity)
      }
    }

    em.txn {
      em.persist(recipeEntity)
      userEntity.addRecipe(recipeEntity)
    }
    return recipeEntity
  }

  override fun setRecipe(id: Long, recipe: RecipeSpec): RecipeEntity {
    val recipeEntity = getRecipe(id)

    em.txn {
      with(recipe) {
        val recipeSpec: RecipeSpec = this
      }
      recipe.title?.apply { recipeEntity.title = this }
      recipe.description?.apply { recipeEntity.description = this }
      recipe.owner?.apply { recipeEntity.owner = userService.getUser(this.id) }
      recipe.imageUrl?.apply { recipeEntity.imageUrl = this }
      recipe.instructions?.apply { recipeEntity.instructions = this }
      recipe.source?.apply { recipeEntity.source = this }
      recipe.components?.apply {
        // todo is this right?
        for (component in this) {
          val componentEntity = ComponentEntity()
          componentEntity.ingredient = ingredientService.getIngredient(component.ingredient.id)
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
    em.txn {
      em.remove(getRecipe(id))
    }
  }
}
