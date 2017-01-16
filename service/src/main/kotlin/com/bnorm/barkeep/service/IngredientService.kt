// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.Ingredient

interface IngredientService {

  fun getIngredients(): Collection<Ingredient>

  fun getIngredient(id: Long): Ingredient?

  fun createIngredient(ingredient: Ingredient): Ingredient

  fun setIngredient(id: Long, ingredient: Ingredient): Ingredient

  fun deleteIngredient(id: Long)
}
