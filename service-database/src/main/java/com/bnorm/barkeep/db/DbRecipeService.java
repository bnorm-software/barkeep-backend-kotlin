// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.List;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.service.RecipeService;

public class DbRecipeService extends AbstractDbService implements RecipeService {

  public DbRecipeService(EntityManager entityManager) {
    super(entityManager);
  }

  @Override
  public List<Recipe> getRecipes() {
    return super.getRecipes();
  }

  @Override
  public RecipeEntity getRecipe(long recipeId) {
    return super.getRecipe(recipeId);
  }

  @Override
  public RecipeEntity createRecipe(Recipe recipe) {
    if (recipe.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create recipe that already has an id=%d",
                                                       recipe.getId()));
    } else if (recipe.getOwner() == null) {
      throw new IllegalArgumentException("Cannot create recipe without an owner");
    }
    UserEntity userEntity = find(recipe.getOwner());
    if (userEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot create recipe with an unknown owner id=%d",
                                                       recipe.getOwner().getId()));
    }

    return transaction(em -> {
      RecipeEntity recipeEntity = new RecipeEntity();

      recipeEntity.setTitle(recipe.getTitle());
      recipeEntity.setDescription(recipe.getDescription());
      recipeEntity.setOwner(userEntity);
      recipeEntity.setImageUrl(recipe.getImageUrl());
      recipeEntity.setInstructions(recipe.getInstructions());
      recipeEntity.setSource(recipe.getInstructions());

      if (recipe.getComponents() != null) {
        for (Component component : recipe.getComponents()) {
          ComponentEntity componentEntity = new ComponentEntity();
          componentEntity.setIngredient(find(component.getIngredient()));
          componentEntity.setMin(component.getMin());
          componentEntity.setMax(component.getMax());
          componentEntity.setOrder(component.getOrder());
          componentEntity.setComponentNum(component.getComponentNum());
          recipeEntity.addComponent(componentEntity);
        }
      }

      em.persist(recipeEntity);

      userEntity.addRecipe(recipeEntity);
      return recipeEntity;
    });
  }

  @Override
  public RecipeEntity setRecipe(long recipeId, Recipe recipe) {
    RecipeEntity recipeEntity = findRecipe(recipeId);
    requireExists(recipeEntity, recipeId, "recipe");
    requireMatch(recipe, recipeId, "recipe");

    return transaction(em -> {
      if (recipe.getTitle() != null) {
        recipeEntity.setTitle(recipe.getTitle());
      }
      if (recipe.getDescription() != null) {
        recipeEntity.setDescription(recipe.getTitle());
      }
      if (recipe.getImageUrl() != null) {
        recipeEntity.setImageUrl(recipe.getImageUrl());
      }
      if (recipe.getInstructions() != null) {
        recipeEntity.setInstructions(recipe.getInstructions());
      }
      if (recipe.getSource() != null) {
        recipeEntity.setSource(recipe.getSource());
      }
      if (recipe.getComponents() != null) {
        // todo is this right?
        for (Component component : recipe.getComponents()) {
          ComponentEntity componentEntity = new ComponentEntity();
          componentEntity.setIngredient(find(component.getIngredient()));
          componentEntity.setMin(component.getMin());
          componentEntity.setMax(component.getMax());
          componentEntity.setOrder(component.getOrder());
          componentEntity.setComponentNum(component.getComponentNum());
          recipeEntity.addComponent(componentEntity);
        }
      }

      return recipeEntity;
    });
  }

  @Override
  public void deleteRecipe(long recipeId) {
    transaction(em -> {
      RecipeEntity recipeEntity = findRecipe(recipeId);
      requireExists(recipeEntity, recipeId, "recipe");
      em.remove(recipeEntity);
    });
  }
}
