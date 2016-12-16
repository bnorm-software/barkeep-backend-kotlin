package com.bnorm.barkeep.db;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class BarkeepRepository {

  private final EntityManager entityManager;

  public BarkeepRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


  private <E> E create(E entity) {
    entityManager.getTransaction().begin();
    entityManager.persist(entity);
    entityManager.getTransaction().commit();
    return entity;
  }

  private <E> E update(E entity) {
    entityManager.getTransaction().begin();
    entityManager.merge(entity);
    entityManager.getTransaction().commit();
    return entity;
  }

  private void delete(Object entity) {
    entityManager.getTransaction().begin();
    entityManager.remove(entity);
    entityManager.getTransaction().commit();
  }


  public UserEntity getUser(long id) {
    return entityManager.find(UserEntity.class, id);
  }

  public UserEntity findUserByUsername(String username) {
    TypedQuery<UserEntity> query = entityManager.createNamedQuery("UserEntity.findByUsername", UserEntity.class);
    List<UserEntity> results = query.setParameter("username", username).getResultList();
    return results.isEmpty() ? null : results.get(0);
  }

  public UserEntity createUser(String username, String password, String displayName, String email) {
    UserEntity user = new UserEntity();
    user.setUsername(username);
    user.setPassword(password);
    user.setDisplayName(displayName);
    user.setEmail(email);

    return create(user);
  }

  public UserEntity updateUser(UserEntity user) {
    return update(user);
  }

  public void deleteUser(UserEntity user) {
    delete(user);
  }


  public BookEntity getBook(long id) {
    return entityManager.find(BookEntity.class, id);
  }

  public BookEntity createBook(UserEntity user, String title) {
    BookEntity book = new BookEntity();
    book.setUser(user);
    book.setTitle(title);

    return create(book);
  }

  public BookEntity updateBook(BookEntity book) {
    return update(book);
  }

  public void deleteBook(BookEntity book) {
    delete(book);
  }


  public BarEntity getBar(long id) {
    return entityManager.find(BarEntity.class, id);
  }

  public BarEntity createBar(UserEntity user, String title) {
    BarEntity bar = new BarEntity();
    bar.setUser(user);
    bar.setTitle(title);

    return create(bar);
  }

  public BarEntity updateBar(BarEntity bar) {
    return update(bar);
  }

  public void deleteBar(BarEntity bar) {
    delete(bar);
  }


  public RecipeEntity getRecipe(long id) {
    return entityManager.find(RecipeEntity.class, id);
  }

  public RecipeEntity createRecipe(BookEntity book, String title, List<ComponentEntity> recipeComponents) {
    entityManager.getTransaction().begin();

    RecipeEntity recipe = new RecipeEntity();
    recipe.setTitle(title);
    recipe.setComponents(recipeComponents);

    entityManager.persist(recipe);

    BookRecipeEntityKey key = new BookRecipeEntityKey();
    key.setBook(book);
    key.setRecipe(recipe);

    BookRecipeEntity bookRecipe = new BookRecipeEntity();
    bookRecipe.setKey(key);

    book.addRecipe(bookRecipe);

    entityManager.merge(book);

    entityManager.getTransaction().commit();
    return recipe;
  }

  public RecipeEntity updateRecipe(RecipeEntity recipe) {
    return update(recipe);
  }

  public void deleteRecipe(RecipeEntity recipe) {
    delete(recipe);
  }


  public IngredientEntity getIngredient(long id) {
    return entityManager.find(IngredientEntity.class, id);
  }

  public IngredientEntity createIngredient(String title) {
    return createIngredient(title, Optional.empty());
  }

  public IngredientEntity createIngredient(String title, IngredientEntity parent) {
    return createIngredient(title, Optional.of(parent));
  }

  private IngredientEntity createIngredient(String title, Optional<IngredientEntity> parent) {
    IngredientEntity ingredient = new IngredientEntity();
    ingredient.setTitle(title);
    ingredient.setParent(parent);

    return create(ingredient);
  }

  public IngredientEntity updateIngredient(IngredientEntity ingredient) {
    return update(ingredient);
  }

  public void deleteIngredient(IngredientEntity ingredient) {
    delete(ingredient);
  }
}
