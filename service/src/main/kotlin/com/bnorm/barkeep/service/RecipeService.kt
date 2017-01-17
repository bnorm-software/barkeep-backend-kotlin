// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.RecipeSpec

interface RecipeService {

  fun getRecipes(): Collection<Recipe>

  fun getRecipe(id: Long): Recipe?

  fun createRecipe(recipe: RecipeSpec): Recipe

  fun setRecipe(id: Long, recipe: RecipeSpec): Recipe

  fun deleteRecipe(id: Long)
}
