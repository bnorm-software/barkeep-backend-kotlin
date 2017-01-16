// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.service.BookService
import javax.persistence.EntityManager

class DbBookService(entityManager: EntityManager) : AbstractDbService(entityManager), BookService {

  override fun getBooks(): List<Book> {
    return super.getBooks()
  }

  override fun getBook(id: Long): BookEntity? {
    return super.getBook(id)
  }

  override fun createBook(book: Book): BookEntity {
    if (book.id != null) {
      throw IllegalArgumentException("Cannot create book that already has an id=$book.id")
    } else if (book.owner == null) {
      throw IllegalArgumentException("Cannot create book without an owner")
    }
    val userEntity = find(book.owner!!) ?: throw IllegalArgumentException("Cannot create book with an unknown owner id=$book.owner!!.id")

    val bookEntity = BookEntity()
    bookEntity.title = book.title
    bookEntity.description = book.description
    bookEntity.owner = userEntity
    if (book.recipes != null) {
      for (recipe in book.recipes!!) {
        bookEntity.addRecipe(find(recipe)!!)
      }
    }

    txn {
      em.persist(bookEntity)
      userEntity.addBook(bookEntity)
    }
    return bookEntity
  }

  override fun setBook(id: Long, book: Book): BookEntity {
    val bookEntity = requireExists(findBook(id), id, "book")
    requireMatch(book, id, "book")

    txn {
      if (book.title != null) {
        bookEntity.title = book.title
      }
      if (book.description != null) {
        bookEntity.description = book.description
      }
      if (book.owner != null) {
        bookEntity.owner = find(book.owner!!)
      }
      if (book.recipes != null) {
        // todo is this correct?
        for (recipe in book.recipes!!) {
          bookEntity.addRecipe(find(recipe)!!)
        }
      }
    }

    return bookEntity
  }

  override fun deleteBook(id: Long) {
    txn {
      val bookEntity = findBook(id)
      requireExists(bookEntity, id, "book")
      em.remove(bookEntity)
    }
  }
}
