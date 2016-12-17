// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.BookService;


public class DbBookService implements BookService {

  private final EntityManager em;

  public DbBookService(EntityManager entityManager) {
    this.em = entityManager;
  }

  private UserEntity entity(User user) {
    return em.find(UserEntity.class, user.getId());
  }

  private RecipeEntity entity(Recipe recie) {
    return em.find(RecipeEntity.class, recie.getId());
  }

  @Override
  public Collection<Book> listBooks() {
    List<BookEntity> bookEntities = em.createNamedQuery("BookEntity.findAll", BookEntity.class).getResultList();
    return Collections.unmodifiableList(bookEntities);
  }

  @Override
  public BookEntity getBook(long bookId) {
    return em.find(BookEntity.class, bookId);
  }

  @Override
  public BookEntity createBook(Book book) {
    if (book.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create book that already has an id=%d", book.getId()));
    }

    BookEntity bookEntity = new BookEntity();
    UserEntity userEntity = entity(book.getOwner());

    bookEntity.setTitle(book.getTitle());
    bookEntity.setDescription(book.getDescription());
    bookEntity.setOwner(userEntity);
    for (Recipe recipe : book.getRecipes()) {
      bookEntity.addRecipe(entity(recipe));
    }

    userEntity.addBook(bookEntity);

    em.getTransaction().begin();
    em.persist(bookEntity);
    em.merge(userEntity);
    em.getTransaction().commit();
    return bookEntity;
  }

  @Override
  public BookEntity setBook(long bookId, Book book) {
    BookEntity bookEntity = getBook(bookId);
    if (bookEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find book with id=%d", bookId));
    }
    if (book.getId() != null && book.getId() != bookId) {
      throw new IllegalArgumentException(String.format("Cannot update book with a different id=%d then existing id=%d",
                                                       book.getId(),
                                                       bookId));
    }

    if (book.getTitle() != null) {
      bookEntity.setTitle(book.getTitle());
    }
    if (book.getDescription() != null) {
      bookEntity.setDescription(book.getTitle());
    }
    if (book.getOwner() != null) {
      bookEntity.setOwner(entity(book.getOwner()));
    }
    if (book.getRecipes() != null) {
      // todo is this correct?
      for (Recipe recipe : book.getRecipes()) {
        bookEntity.addRecipe(entity(recipe));
      }
    }

    em.getTransaction().begin();
    em.merge(bookEntity);
    em.getTransaction().commit();
    return bookEntity;
  }

  @Override
  public void deleteBook(long bookId) {
    BookEntity bookEntity = getBook(bookId);
    if (bookEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot find book with id=%d", bookId));
    }
    em.remove(bookEntity);
  }
}
