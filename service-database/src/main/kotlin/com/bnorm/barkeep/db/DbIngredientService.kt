// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.service.IngredientService
import javax.persistence.EntityManager

class DbIngredientService(entityManager: EntityManager) : AbstractDbService(entityManager), IngredientService {

  private fun parentEntity(ingredient: Ingredient): IngredientEntity? {
    return ingredient.parent?.id.let { findIngredient(it) }
  }

  override fun getIngredients(): List<Ingredient> {
    return super.getIngredients()
  }

  override fun getIngredient(id: Long): IngredientEntity? {
    return super.getIngredient(id)
  }

  override fun createIngredient(ingredient: Ingredient): IngredientEntity {
    if (ingredient.id != null) {
      throw IllegalArgumentException("Cannot create ingredient that already has an id=$ingredient.id")
    }
    val parentEntity = parentEntity(ingredient)
    // todo check parentEntity exists if needed

    val ingredientEntity = IngredientEntity()

    ingredientEntity.title = ingredient.title
    ingredientEntity.parent = parentEntity

    txn {
      em.persist(ingredientEntity)
    }

    return ingredientEntity
  }

  override fun setIngredient(id: Long, ingredient: Ingredient): IngredientEntity {
    val ingredientEntity = requireExists(findIngredient(id), id, "ingredient")
    requireMatch(ingredient, id, "ingredient")

    txn {
      if (ingredient.title != null) {
        ingredientEntity.title = ingredient.title
      }
      if (ingredient.parent != null) {
        ingredientEntity.parent = parentEntity(ingredient)
      }
    }

    return ingredientEntity
  }

  override fun deleteIngredient(id: Long) {
    txn {
      val ingredientEntity = findIngredient(id)
      requireExists(ingredientEntity, id, "ingredient")
      em.remove(ingredientEntity)
    }
  }
}
