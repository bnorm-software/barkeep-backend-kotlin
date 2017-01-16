// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.Book

interface BookService {

  fun getBooks(): Collection<Book>

  fun getBook(id: Long): Book?

  fun createBook(book: Book): Book

  fun setBook(id: Long, book: Book): Book

  fun deleteBook(id: Long)
}
