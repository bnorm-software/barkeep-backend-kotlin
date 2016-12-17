// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.RecipeService;


public class DbRecipeService implements RecipeService {

  private final EntityManager em;

  public DbRecipeService(EntityManager entityManager) {
    this.em = entityManager;
  }

  private UserEntity entity(User user) {
    return em.find(UserEntity.class, user.getId());
  }

  private IngredientEntity entity(Ingredient ingredient) {
    return em.find(IngredientEntity.class, ingredient.getId());
  }

  @Override
  public Collection<Recipe> listRecipes() {
    List<RecipeEntity> recipeEntities = em.createNamedQuery("RecipeEntity.findAll", RecipeEntity.class).getResultList();
    return Collections.unmodifiableList(recipeEntities);
  }

  @Override
  public RecipeEntity getRecipe(long recipeId) {
    return em.find(RecipeEntity.class, recipeId);
  }

  @Override
  public RecipeEntity createRecipe(Recipe recipe) {
    if (recipe.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create recipe that already has an id=%d",
                                                       recipe.getId()));
    }

    RecipeEntity recipeEntity = new RecipeEntity();
    UserEntity userEntity = entity(recipe.getOwner());

    recipeEntity.setTitle(recipe.getTitle());
    recipeEntity.setDescription(recipe.getDescription());
    recipeEntity.setOwner(userEntity);
    recipeEntity.setImageUrl(recipe.getImageUrl());
    recipeEntity.setInstructions(recipe.getInstructions());
    recipeEntity.setSource(recipe.getInstructions());

    for (com.bnorm.barkeep.model.Component component : recipe.getComponents()) {
      ComponentEntity componentEntity = new ComponentEntity();
      componentEntity.setIngredient(entity(component.getIngredient()));
      componentEntity.setMin(component.getMin());
      componentEntity.setMax(component.getMax());
      componentEntity.setOrder(component.getOrder());
      componentEntity.setComponentNum(component.getComponentNum());
      recipeEntity.addComponent(componentEntity);
    }

    em.getTransaction().begin();
    em.persist(recipeEntity);
    em.getTransaction().commit();
    return recipeEntity;
  }

  @Override
  public RecipeEntity setRecipe(long recipeId, Recipe recipe) {
    RecipeEntity recipeEntity = getRecipe(recipeId);
    if (recipeEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find recipe with id=%d", recipeId));
    }
    if (recipe.getId() != null && recipe.getId() != recipeId) {
      throw new IllegalArgumentException(String.format("Cannot update recipe with a different id=%d then existing id=%d",
                                                       recipe.getId(),
                                                       recipeId));
    }

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
      for (com.bnorm.barkeep.model.Component component : recipe.getComponents()) {
        ComponentEntity componentEntity = new ComponentEntity();
        componentEntity.setIngredient(entity(component.getIngredient()));
        componentEntity.setMin(component.getMin());
        componentEntity.setMax(component.getMax());
        componentEntity.setOrder(component.getOrder());
        componentEntity.setComponentNum(component.getComponentNum());
        recipeEntity.addComponent(componentEntity);
      }
    }

    em.getTransaction().begin();
    em.merge(recipeEntity);
    em.getTransaction().commit();
    return recipeEntity;
  }

  @Override
  public void deleteRecipe(long recipeId) {
    RecipeEntity recipeEntity = getRecipe(recipeId);
    if (recipeEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find recipe with id=%d", recipeId));
    }
    em.remove(recipeEntity);
  }
}
