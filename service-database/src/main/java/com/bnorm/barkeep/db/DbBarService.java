// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.List;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.service.BarService;

public class DbBarService extends AbstractDbService implements BarService {

  public DbBarService(EntityManager entityManager) {
    super(entityManager);
  }

  @Override
  public List<Bar> getBars() {
    return super.getBars();
  }

  @Override
  public BarEntity getBar(long barId) {
    return super.getBar(barId);
  }

  @Override
  public BarEntity createBar(Bar bar) {
    if (bar.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create bar that already has an id=%d", bar.getId()));
    } else if (bar.getOwner() == null) {
      throw new IllegalArgumentException("Cannot create bar without an owner");
    }
    UserEntity userEntity = find(bar.getOwner());
    if (userEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot create bar with an unknown owner id=%d",
                                                       bar.getOwner().getId()));
    }

    return transaction(em -> {
      BarEntity barEntity = new BarEntity();

      barEntity.setTitle(bar.getTitle());
      barEntity.setDescription(bar.getDescription());
      barEntity.setOwner(userEntity);
      barEntity.addUser(userEntity);
      if (bar.getIngredients() != null) {
        for (Ingredient ingredient : bar.getIngredients()) {
          barEntity.addIngredient(find(ingredient));
        }
      }

      em.persist(barEntity);

      userEntity.addBar(barEntity);
      return barEntity;
    });
  }

  @Override
  public BarEntity setBar(long barId, Bar bar) {
    BarEntity barEntity = findBar(barId);
    requireExists(barEntity, barId, "bar");
    requireMatch(bar, barId, "bar");

    return transaction(em -> {
      if (bar.getTitle() != null) {
        barEntity.setTitle(bar.getTitle());
      }
      if (bar.getDescription() != null) {
        barEntity.setDescription(bar.getDescription());
      }
      if (bar.getOwner() != null) {
        barEntity.setOwner(find(bar.getOwner()));
      }
      if (bar.getIngredients() != null) {
        // todo is this correct?
        for (Ingredient ingredient : bar.getIngredients()) {
          barEntity.addIngredient(find(ingredient));
        }
      }

      return barEntity;
    });
  }

  @Override
  public void deleteBar(long barId) {
    transaction(em -> {
      BarEntity barEntity = findBar(barId);
      requireExists(barEntity, barId, "bar");
      em.remove(barEntity);
    });
  }
}
