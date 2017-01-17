// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.HasId
import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User
import javax.persistence.EntityManager

abstract class AbstractDbService(val em: EntityManager) {

  fun txn(action: EntityManager.() -> Unit) {
    em.transaction.begin()
    try {
      em.action()
      em.transaction.commit()
    } catch (t: Throwable) {
      em.transaction.rollback()
      throw t
    }
  }

  private fun <T> refresh(entity: T?): T? {
    return entity?.apply { em.refresh(this) }
  }

  // ===== HasId ===== //

  fun <T> requireExists(entity: T?, id: Long, name: String): T {
    return entity ?: throw IllegalArgumentException("Cannot find $name with id=%$id")
  }

  // ===== User ===== //

  fun find(user: User): UserEntity? {
    return user as? UserEntity ?: findUser(user.id)
  }

  fun findUser(id: Long): UserEntity? {
    return em.find(UserEntity::class.java, id)
  }

  open fun getUser(id: Long): UserEntity? {
    return refresh(findUser(id))
  }

  // ===== Ingredient ===== //

  fun find(ingredient: Ingredient): IngredientEntity? {
    return ingredient as? IngredientEntity ?: findIngredient(ingredient.id)
  }

  fun findIngredient(id: Long): IngredientEntity? {
    return em.find(IngredientEntity::class.java, id)
  }

  open fun getIngredient(id: Long): IngredientEntity? {
    return refresh(findIngredient(id))
  }

  open fun getIngredients(): List<Ingredient> {
    return em.createNamedQuery("IngredientEntity.findAll", IngredientEntity::class.java).resultList
  }


  // ===== Bar ===== //

  fun find(bar: Bar): BarEntity? {
    return findBar(bar.id)
  }

  fun findBar(id: Long): BarEntity? {
    return em.find(BarEntity::class.java, id)
  }

  open fun getBar(id: Long): BarEntity? {
    return refresh(findBar(id))
  }

  open fun getBars(): List<Bar> {
    return em.createNamedQuery("BarEntity.findAll", BarEntity::class.java).resultList
  }

  // ===== Book ===== //

  fun find(book: Book): BookEntity? {
    return findBook(book.id)
  }

  fun findBook(id: Long): BookEntity? {
    return em.find(BookEntity::class.java, id)
  }

  open fun getBook(id: Long): BookEntity? {
    return refresh(findBook(id))
  }

  open fun getBooks(): List<Book> {
    return em.createNamedQuery("BookEntity.findAll", BookEntity::class.java).resultList
  }

  // ===== Recipe ===== //

  fun find(recipe: Recipe): RecipeEntity? {
    return findRecipe(recipe.id)
  }

  fun findRecipe(id: Long): RecipeEntity? {
    return em.find(RecipeEntity::class.java, id)
  }

  open fun getRecipes(): List<Recipe> {
    return em.createNamedQuery("RecipeEntity.findAll", RecipeEntity::class.java).resultList
  }

  open fun getRecipe(id: Long): RecipeEntity? {
    return refresh(findRecipe(id))
  }
}
