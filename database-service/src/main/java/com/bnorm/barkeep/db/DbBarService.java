// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.BarService;

public class DbBarService implements BarService {

  private final EntityManager em;

  public DbBarService(EntityManager entityManager) {
    this.em = Objects.requireNonNull(entityManager);
  }

  private void transaction(Consumer<EntityManager> consumer) {
    em.getTransaction().begin();
    try {
      consumer.accept(em);
      em.getTransaction().commit();
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    }
  }

  private <T> T transaction(Function<EntityManager, T> function) {
    em.getTransaction().begin();
    try {
      T result = function.apply(em);
      em.getTransaction().commit();
      return result;
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    }
  }

  private UserEntity entity(User user) {
    return em.find(UserEntity.class, user.getId());
  }

  private IngredientEntity entity(Ingredient ingredient) {
    return em.find(IngredientEntity.class, ingredient.getId());
  }

  @Override
  public Collection<Bar> getBars() {
    List<BarEntity> barEntities = em.createNamedQuery("BarEntity.findAll", BarEntity.class).getResultList();
    return Collections.unmodifiableList(barEntities);
  }

  @Override
  public BarEntity getBar(long barId) {
    BarEntity barEntity = em.find(BarEntity.class, barId);
    if (barEntity != null) {
      em.refresh(barEntity);
    }
    return barEntity;
  }

  @Override
  public BarEntity createBar(Bar bar) {
    if (bar.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create bar that already has an id=%d", bar.getId()));
    } else if (bar.getOwner() == null) {
      throw new IllegalArgumentException("Cannot create bar without an owner");
    }
    UserEntity userEntity = entity(bar.getOwner());
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
          barEntity.addIngredient(entity(ingredient));
        }
      }

      em.persist(barEntity);

      userEntity.addBar(barEntity);

      em.merge(barEntity);
      return barEntity;
    });
  }

  @Override
  public BarEntity setBar(long barId, Bar bar) {
    return transaction(em -> {
      BarEntity barEntity = em.find(BarEntity.class, barId);
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
        barEntity.setDescription(bar.getDescription());
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

      em.merge(barEntity);
      return barEntity;
    });
  }

  @Override
  public void deleteBar(long barId) {
    transaction(em -> {
      BarEntity barEntity = getBar(barId);
      if (barEntity == null) {
        throw new IllegalArgumentException(String.format("Cannot find bar with id=%d", barId));
      }
      em.remove(barEntity);
      em.flush();
    });
  }
}
