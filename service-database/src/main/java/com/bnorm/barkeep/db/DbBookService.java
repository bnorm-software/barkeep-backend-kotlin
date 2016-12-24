// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.List;

import javax.persistence.EntityManager;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.service.BookService;

public class DbBookService extends AbstractDbService implements BookService {

  public DbBookService(EntityManager entityManager) {
    super(entityManager);
  }

  @Override
  public List<Book> getBooks() {
    return super.getBooks();
  }

  @Override
  public BookEntity getBook(long bookId) {
    return super.getBook(bookId);
  }

  @Override
  public BookEntity createBook(Book book) {
    if (book.getId() != null) {
      throw new IllegalArgumentException(String.format("Cannot create book that already has an id=%d", book.getId()));
    } else if (book.getOwner() == null) {
      throw new IllegalArgumentException("Cannot create book without an owner");
    }
    UserEntity userEntity = find(book.getOwner());
    if (userEntity == null) {
      throw new IllegalArgumentException(String.format("Cannot create book with an unknown owner id=%d",
                                                       book.getOwner().getId()));
    }

    return transaction(em -> {
      BookEntity bookEntity = new BookEntity();

      bookEntity.setTitle(book.getTitle());
      bookEntity.setDescription(book.getDescription());
      bookEntity.setOwner(userEntity);
      if (book.getRecipes() != null) {
        for (Recipe recipe : book.getRecipes()) {
          bookEntity.addRecipe(find(recipe));
        }
      }

      em.persist(bookEntity);

      userEntity.addBook(bookEntity);
      return bookEntity;
    });
  }

  @Override
  public BookEntity setBook(long bookId, Book book) {
    BookEntity bookEntity = findBook(bookId);
    requireExists(bookEntity, bookId, "book");
    requireMatch(book, bookId, "book");

    return transaction(em -> {
      if (book.getTitle() != null) {
        bookEntity.setTitle(book.getTitle());
      }
      if (book.getDescription() != null) {
        bookEntity.setDescription(book.getTitle());
      }
      if (book.getOwner() != null) {
        bookEntity.setOwner(find(book.getOwner()));
      }
      if (book.getRecipes() != null) {
        // todo is this correct?
        for (Recipe recipe : book.getRecipes()) {
          bookEntity.addRecipe(find(recipe));
        }
      }

      return bookEntity;
    });
  }

  @Override
  public void deleteBook(long bookId) {
    transaction(em -> {
      BookEntity bookEntity = findBook(bookId);
      requireExists(bookEntity, bookId, "book");
      em.remove(bookEntity);
    });
  }
}
