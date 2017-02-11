// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.IngredientSpec
import com.bnorm.barkeep.model.db.IngredientEntity
import com.bnorm.barkeep.service.IngredientService
import javax.persistence.EntityManager

class DbIngredientService(private val em: EntityManager) : IngredientService {

  override fun getIngredients(): List<Ingredient> {
    return em.createNamedQuery("IngredientEntity.findAll", IngredientEntity::class.java).resultList
  }

  fun findIngredient(id: Long): IngredientEntity? {
    return em.find(IngredientEntity::class.java, id)
  }

  override fun getIngredient(id: Long): IngredientEntity {
    return findIngredient(id) ?: throw IllegalArgumentException("Cannot find ingredient with id=$id")
  }

  override fun createIngredient(ingredient: IngredientSpec): IngredientEntity {
    val ingredientEntity = IngredientEntity()
    ingredient.title?.apply { ingredientEntity.title = this }
    ingredient.parent?.apply { ingredientEntity.parent = getIngredient(this.id) }

    em.txn {
      em.persist(ingredientEntity)
    }
    return ingredientEntity
  }

  override fun setIngredient(id: Long, ingredient: IngredientSpec): IngredientEntity {
    val ingredientEntity = getIngredient(id)

    em.txn {
      ingredient.title?.apply { ingredientEntity.title = this }
      ingredient.parent?.apply { ingredientEntity.parent = getIngredient(this.id) }
    }
    return ingredientEntity
  }

  override fun deleteIngredient(id: Long) {
    em.txn {
      em.remove(getIngredient(id))
    }
  }
}
