// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.service.IngredientService;


public class DbIngredientService implements IngredientService {

  private final EntityManager em;

  public DbIngredientService(EntityManager entityManager) {
    this.em = entityManager;
  }

  private IngredientEntity parentEntity(Ingredient ingredient) {
    return Optional.ofNullable(ingredient)
                   .map(Ingredient::getParent)
                   .map(Ingredient::getId)
                   .map(this::getIngredient)
                   .orElse(null);
  }

  @Override
  public Collection<Ingredient> listIngredients() {
    List<IngredientEntity> ingredientEntities = em.createNamedQuery("IngredientEntity.findAll", IngredientEntity.class)
                                                  .getResultList();
    return Collections.unmodifiableList(ingredientEntities);
  }

  @Override
  public IngredientEntity getIngredient(long ingredientId) {
    return em.find(IngredientEntity.class, ingredientId);
  }

  @Override
  public IngredientEntity createIngredient(Ingredient ingredient) {
    if (ingredient.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create ingredient that already has an id=%d",
                                                       ingredient.getId()));
    }

    IngredientEntity ingredientEntity = new IngredientEntity();
    IngredientEntity parentEntity = parentEntity(ingredient);

    ingredientEntity.setTitle(ingredient.getTitle());
    ingredientEntity.setParent(parentEntity);

    em.getTransaction().begin();
    em.persist(ingredientEntity);
    em.getTransaction().commit();
    return ingredientEntity;
  }

  @Override
  public IngredientEntity setIngredient(long ingredientId, Ingredient ingredient) {
    IngredientEntity ingredientEntity = getIngredient(ingredientId);
    if (ingredientEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find ingredient with id=%d", ingredientId));
    }
    if (ingredient.getId() != null && ingredient.getId() != ingredientId) {
      throw new IllegalArgumentException(String.format(
              "Cannot update ingredient with a different id=%d then existing id=%d",
              ingredient.getId(),
              ingredientId));
    }

    if (ingredient.getTitle() != null) {
      ingredientEntity.setTitle(ingredient.getTitle());
    }
    if (ingredient.getParent() != null) {
      ingredientEntity.setParent(parentEntity(ingredient));
    }

    em.getTransaction().begin();
    em.merge(ingredientEntity);
    em.getTransaction().commit();
    return ingredientEntity;
  }

  @Override
  public void deleteIngredient(long ingredientId) {
    IngredientEntity ingredientEntity = getIngredient(ingredientId);
    if (ingredientEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find ingredient with id=%d", ingredientId));
    }
    em.remove(ingredientEntity);
  }
}
