// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service

import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.BookSpec

interface BookService {

  fun getBooks(): Collection<Book>

  fun getBook(id: Long): Book

  fun createBook(book: BookSpec): Book

  fun addBookRecipe(bookId: Long, recipeId: Long)

  fun removeBookRecipe(bookId: Long, recipeId: Long)

  fun setBook(id: Long, book: BookSpec): Book

  fun deleteBook(id: Long)
}
