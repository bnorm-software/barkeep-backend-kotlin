// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service;

import java.util.Collection;

import com.bnorm.barkeep.model.Book;

public interface BookService {

  Collection<Book> listBooks();

  Book getBook(long bookId);

  Book createBook(Book book);

  Book setBook(long bookId, Book book);

  void deleteBook(long bookId);
}
