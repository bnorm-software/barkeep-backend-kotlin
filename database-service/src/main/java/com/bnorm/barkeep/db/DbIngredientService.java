// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.service.IngredientService;


public class DbIngredientService extends AbstractDbService implements IngredientService {

  public DbIngredientService(EntityManager entityManager) {
    super(entityManager);
  }

  private IngredientEntity parentEntity(Ingredient ingredient) {
    Optional<Long> id = Optional.ofNullable(ingredient).map(Ingredient::getParent).map(Ingredient::getId);
    if (id.isPresent()) {
      IngredientEntity parent = findIngredient(id.get());
      // todo check parent is not null
      return parent;
    } else {
      return null;
    }
  }

  @Override
  public List<Ingredient> getIngredients() {
    return super.getIngredients();
  }

  @Override
  public IngredientEntity getIngredient(long ingredientId) {
    return super.getIngredient(ingredientId);
  }

  @Override
  public IngredientEntity createIngredient(Ingredient ingredient) {
    if (ingredient.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create ingredient that already has an id=%d",
                                                       ingredient.getId()));
    }
    IngredientEntity parentEntity = parentEntity(ingredient);
    // todo check parentEntity exists if needed

    return transaction(em -> {
      IngredientEntity ingredientEntity = new IngredientEntity();

      ingredientEntity.setTitle(ingredient.getTitle());
      ingredientEntity.setParent(parentEntity);

      em.persist(ingredientEntity);
      return ingredientEntity;
    });
  }

  @Override
  public IngredientEntity setIngredient(long ingredientId, Ingredient ingredient) {
    IngredientEntity ingredientEntity = findIngredient(ingredientId);
    requireExists(ingredientEntity, ingredientId, "ingredient");
    requireMatch(ingredient, ingredientId, "ingredient");

    return transaction(em -> {
      if (ingredient.getTitle() != null) {
        ingredientEntity.setTitle(ingredient.getTitle());
      }
      if (ingredient.getParent() != null) {
        ingredientEntity.setParent(parentEntity(ingredient));
      }

      return ingredientEntity;
    });
  }

  @Override
  public void deleteIngredient(long ingredientId) {
    transaction(em -> {
      IngredientEntity ingredientEntity = findIngredient(ingredientId);
      requireExists(ingredientEntity, ingredientId, "ingredient");
      em.remove(ingredientEntity);
    });
  }
}
