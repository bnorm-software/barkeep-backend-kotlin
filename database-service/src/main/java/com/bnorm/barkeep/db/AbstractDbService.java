// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.HasId;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;

abstract class AbstractDbService {

  private final EntityManager em;

  AbstractDbService(EntityManager entityManager) {
    this.em = Objects.requireNonNull(entityManager);
  }

  void transaction(Consumer<EntityManager> consumer) {
    em.getTransaction().begin();
    try {
      consumer.accept(em);
      em.getTransaction().commit();
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    }
  }

  <T> T transaction(Function<EntityManager, T> function) {
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

  private <T> T refresh(T entity) {
    if (entity != null) {
      em.refresh(entity);
    }
    return entity;
  }

  // ===== HasId ===== //

  <T> T requireExists(T entity, long id, String name) {
    if (entity == null) {
      throw new IllegalArgumentException(String.format("Cannot find %s with id=%d", name, id));
    }
    return entity;
  }

  void requireMatch(HasId hasId, long id, String name) {
    if (hasId.getId() != null && hasId.getId() != id) {
      throw new IllegalArgumentException(String.format("Cannot update %s with a different id=%d then existing id=%d",
                                                       name,
                                                       hasId.getId(),
                                                       id));
    }
  }

  // ===== User ===== //

  UserEntity find(User user) {
    if (user instanceof UserEntity) {
      return (UserEntity) user;
    } else {
      return findUser(user.getId());
    }
  }

  UserEntity findUser(Long id) {
    return id == null ? null : findUser((long) id);
  }

  UserEntity findUser(long id) {
    return em.find(UserEntity.class, id);
  }

  UserEntity getUser(long id) {
    return refresh(findUser(id));
  }

  // ===== Ingredient ===== //

  IngredientEntity find(Ingredient ingredient) {
    if (ingredient instanceof IngredientEntity) {
      return (IngredientEntity) ingredient;
    } else {
      return findIngredient(ingredient.getId());
    }
  }

  IngredientEntity findIngredient(Long id) {
    return id == null ? null : findIngredient((long) id);
  }

  IngredientEntity findIngredient(long ingredientId) {
    return em.find(IngredientEntity.class, ingredientId);
  }

  IngredientEntity getIngredient(long ingredientId) {
    return refresh(findIngredient(ingredientId));
  }

  List<Ingredient> getIngredients() {
    List<IngredientEntity> ingredientEntities = em.createNamedQuery("IngredientEntity.findAll", IngredientEntity.class)
                                                  .getResultList();
    return Collections.unmodifiableList(ingredientEntities);
  }


  // ===== Bar ===== //

  BarEntity find(Bar bar) {
    return findBar(bar.getId());
  }

  BarEntity findBar(Long barId) {
    return barId == null ? null : findBar((long) barId);
  }

  BarEntity findBar(long id) {
    return em.find(BarEntity.class, id);
  }

  BarEntity getBar(long id) {
    return refresh(findBar(id));
  }

  List<Bar> getBars() {
    List<BarEntity> barEntities = em.createNamedQuery("BarEntity.findAll", BarEntity.class).getResultList();
    return Collections.unmodifiableList(barEntities);
  }

  // ===== Book ===== //

  BookEntity find(Book book) {
    return findBook(book.getId());
  }

  BookEntity findBook(Long bookId) {
    return bookId == null ? null : findBook((long) bookId);
  }

  BookEntity findBook(long bookId) {
    return em.find(BookEntity.class, bookId);
  }

  BookEntity getBook(long bookId) {
    return refresh(findBook(bookId));
  }

  List<Book> getBooks() {
    List<BookEntity> bookEntities = em.createNamedQuery("BookEntity.findAll", BookEntity.class).getResultList();
    return Collections.unmodifiableList(bookEntities);
  }

  // ===== Recipe ===== //

  RecipeEntity find(Recipe recipe) {
    return findRecipe(recipe.getId());
  }

  RecipeEntity findRecipe(long id) {
    return em.find(RecipeEntity.class, id);
  }

  RecipeEntity findRecipe(Long id) {
    return id == null ? null : findRecipe((long) id);
  }

  List<Recipe> getRecipes() {
    List<RecipeEntity> recipeEntities = em.createNamedQuery("RecipeEntity.findAll", RecipeEntity.class).getResultList();
    return Collections.unmodifiableList(recipeEntities);
  }

  RecipeEntity getRecipe(long recipeId) {
    return refresh(findRecipe(recipeId));
  }
}
