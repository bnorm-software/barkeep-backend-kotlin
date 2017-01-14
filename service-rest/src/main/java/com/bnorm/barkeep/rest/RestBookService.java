// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bnorm.barkeep.db.DbBookService;
import com.bnorm.barkeep.db.DbUserService;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.BookService;
import com.bnorm.barkeep.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/books")
public class RestBookService extends AbstractRestService implements BookService {

  private final UserService userService;
  private final BookService bookService;

  @Autowired
  public RestBookService(DbUserService userService, DbBookService bookService) {
    this.userService = userService;
    this.bookService = bookService;
  }

  private Book findByOwner(long userId, long bookId) {
    Book book = bookService.getBook(bookId);
    if (!isOwnedBy(book, userId)) {
      throw new NotFound("Unable to find book with id=%d", bookId);
    }
    return book;
  }

  private Book findByUser(User user, long bookId) {
    Book book = bookService.getBook(bookId);
    Set<Book> books = user.getBooks();
    if (book == null || (books != null && !books.contains(book))) {
      throw new NotFound("Unable to find book with id=%d", bookId);
    }
    return book;
  }

  @JsonView(Book.class)
  @RequestMapping(method = RequestMethod.GET)
  @Override
  public Collection<Book> getBooks() {
    User user = userService.getUser(currentUser().getId());
    return user.getBooks();
  }

  @JsonView(Book.class)
  @RequestMapping(value = "/{bookId}", method = RequestMethod.GET)
  @Override
  public Book getBook(@PathVariable("bookId") long bookId) {
    User user = userService.getUser(currentUser().getId());
    return findByUser(user, bookId);
  }

  @JsonView(Recipe.class)
  @RequestMapping(value = "/{bookId}/recipes", method = RequestMethod.GET)
  public Set<Recipe> getBookRecipes(@PathVariable("bookId") long bookId) {
    Book book = getBook(bookId);
    return book.getRecipes();
  }

  @JsonView(Book.class)
  @RequestMapping(method = RequestMethod.POST)
  @Override
  public Book createBook(@RequestBody Book book) {
    if (book.getId() != null) {
      throw new BadRequest("Cannot create book with existing id=%d", book.getId());
    }
    User currentUser = currentUser();
    if (book.getOwner() == null) {
      return bookService.createBook(new BookWithOwner(book, currentUser));
    } else if (!isOwnedBy(book, currentUser.getId())) {
      throw new BadRequest("Cannot create book owned by another user");
    } else {
      return bookService.createBook(book);
    }
  }

  @JsonView(Book.class)
  @RequestMapping(value = "/{bookId}", method = RequestMethod.PUT)
  @Override
  public Book setBook(@PathVariable("bookId") long bookId, @RequestBody Book book) {
    findByOwner(currentUser().getId(), bookId);
    return bookService.setBook(bookId, book);
  }

  @RequestMapping(value = "/{bookId}", method = RequestMethod.DELETE)
  @Override
  public void deleteBook(@PathVariable("bookId") long bookId) {
    findByOwner(currentUser().getId(), bookId);
    bookService.deleteBook(bookId);
  }

  private static class BookWithOwner implements Book {

    private final Book book;
    private final User owner;

    private BookWithOwner(Book book, User owner) {
      this.book = book;
      this.owner = owner;
    }

    @Override
    public Long getId() {
      return book.getId();
    }

    @Nullable
    @Override
    public String getTitle() {
      return book.getTitle();
    }

    @Nullable
    @Override
    public String getDescription() {
      return book.getDescription();
    }

    @Override
    public User getOwner() {
      return owner;
    }

    @Override
    public Set<Recipe> getRecipes() {
      return book.getRecipes();
    }

    @Override
    public int compareTo(@NotNull Book other) {
      return Book.Companion.getCOMPARATOR().compare(this, other);
    }
  }
}
