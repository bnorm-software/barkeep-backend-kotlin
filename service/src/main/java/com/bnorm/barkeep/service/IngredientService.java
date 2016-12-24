// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service;

import java.util.Collection;

import com.bnorm.barkeep.model.Ingredient;

public interface IngredientService {

  Collection<Ingredient> getIngredients();

  Ingredient getIngredient(long ingredientId);

  Ingredient createIngredient(Ingredient ingredient);

  Ingredient setIngredient(long ingredientId, Ingredient ingredient);

  void deleteIngredient(long ingredientId);
}
