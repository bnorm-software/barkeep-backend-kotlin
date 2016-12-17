// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.BarService;

public class DbBarService implements BarService {

  private final EntityManager em;

  public DbBarService(EntityManager entityManager) {
    this.em = entityManager;
  }

  private UserEntity entity(User user) {
    return em.find(UserEntity.class, user.getId());
  }

  private IngredientEntity entity(Ingredient ingredient) {
    return em.find(IngredientEntity.class, ingredient.getId());
  }

  @Override
  public Collection<Bar> listBars() {
    List<BarEntity> barEntities = em.createNamedQuery("BarEntity.findAll", BarEntity.class).getResultList();
    return Collections.unmodifiableList(barEntities);
  }

  @Override
  public BarEntity getBar(long barId) {
    return em.find(BarEntity.class, barId);
  }

  @Override
  public BarEntity createBar(Bar bar) {
    if (bar.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create bar that already has an id=%d", bar.getId()));
    }

    BarEntity barEntity = new BarEntity();
    UserEntity userEntity = entity(bar.getOwner());

    barEntity.setTitle(bar.getTitle());
    barEntity.setDescription(bar.getDescription());
    barEntity.setOwner(userEntity);
    for (Ingredient ingredient : bar.getIngredients()) {
      barEntity.addIngredient(entity(ingredient));
    }

    userEntity.addBar(barEntity);

    em.getTransaction().begin();
    em.persist(barEntity);
    em.merge(userEntity);
    em.getTransaction().commit();
    return barEntity;
  }

  @Override
  public BarEntity setBar(long barId, Bar bar) {
    BarEntity barEntity = getBar(barId);
    if (barEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find bar with id=%d", barId));
    }
    if (bar.getId() != null && bar.getId() != barId) {
      throw new IllegalArgumentException(String.format("Cannot update bar with a different id=%d then existing id=%d",
                                                       bar.getId(),
                                                       barId));
    }

    if (bar.getTitle() != null) {
      barEntity.setTitle(bar.getTitle());
    }
    if (bar.getDescription() != null) {
      barEntity.setDescription(bar.getTitle());
    }
    if (bar.getOwner() != null) {
      barEntity.setOwner(entity(bar.getOwner()));
    }
    if (bar.getIngredients() != null) {
      // todo is this correct?
      for (Ingredient ingredient : bar.getIngredients()) {
        barEntity.addIngredient(entity(ingredient));
      }
    }

    em.getTransaction().begin();
    em.merge(barEntity);
    em.getTransaction().commit();
    return barEntity;
  }

  @Override
  public void deleteBar(long barId) {
    BarEntity barEntity = getBar(barId);
    if (barEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find bar with id=%d", barId));
    }
    em.remove(barEntity);
  }
}
