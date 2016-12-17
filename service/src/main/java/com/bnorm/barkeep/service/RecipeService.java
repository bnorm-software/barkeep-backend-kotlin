// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service;

import java.util.Collection;

import com.bnorm.barkeep.model.Recipe;

public interface RecipeService {

  Collection<Recipe> listRecipes();

  Recipe getRecipe(long recipeId);

  Recipe createRecipe(Recipe recipe);

  Recipe setRecipe(long recipeId, Recipe recipe);

  void deleteRecipe(long recipeId);
}
