// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.IngredientSpec

interface IngredientService {

  fun getIngredients(): Collection<Ingredient>

  fun getIngredient(id: Long): Ingredient

  fun createIngredient(ingredient: IngredientSpec): Ingredient

  fun setIngredient(id: Long, ingredient: IngredientSpec): Ingredient

  fun deleteIngredient(id: Long)
}
