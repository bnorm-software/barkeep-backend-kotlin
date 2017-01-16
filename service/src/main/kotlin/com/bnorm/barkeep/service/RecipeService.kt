// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.Recipe

interface RecipeService {

  fun getRecipes(): Collection<Recipe>

  fun getRecipe(id: Long): Recipe?

  fun createRecipe(recipe: Recipe): Recipe

  fun setRecipe(id: Long, recipe: Recipe): Recipe

  fun deleteRecipe(id: Long)
}
