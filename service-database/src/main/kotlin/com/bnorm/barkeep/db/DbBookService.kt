// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.BookSpec
import com.bnorm.barkeep.service.BookService
import javax.persistence.EntityManager

class DbBookService(private val em: EntityManager,
                    private val recipeService: DbRecipeService,
                    private val userService: DbUserService) : BookService {

  override fun getBooks(): List<Book> {
    return em.createNamedQuery("BookEntity.findAll", BookEntity::class.java).resultList
  }

  fun findBook(id: Long): BookEntity? {
    return em.find(BookEntity::class.java, id)
  }

  override fun getBook(id: Long): BookEntity {
    return findBook(id) ?: throw IllegalArgumentException("Cannot find book with id=$id")
  }

  override fun createBook(book: BookSpec): BookEntity {
    val owner = book.owner ?: throw IllegalArgumentException("Cannot create book without an owner")
    val userEntity = userService.findUser(owner.id) ?: throw IllegalArgumentException("Cannot create book with an unknown owner id=${owner.id}")

    val bookEntity = BookEntity()
    bookEntity.title = book.title
    bookEntity.description = book.description
    bookEntity.owner = userEntity
    bookEntity.addUser(userEntity)

    em.txn {
      em.persist(bookEntity)
      userEntity.addBook(bookEntity)
    }
    return bookEntity
  }

  override fun addBookRecipe(bookId: Long, recipeId: Long) {
    val bookEntity = getBook(bookId)
    val recipeEntity = recipeService.getRecipe(recipeId)

    em.txn {
      bookEntity.addRecipe(recipeEntity)
    }
  }

  override fun removeBookRecipe(bookId: Long, recipeId: Long) {
    val bookEntity = getBook(bookId)
    val recipeEntity = recipeService.getRecipe(recipeId)

    em.txn {
      bookEntity.removeRecipe(recipeEntity)
    }
  }

  override fun setBook(id: Long, book: BookSpec): BookEntity {
    val bookEntity = getBook(id)

    em.txn {
      book.title?.apply { bookEntity.title = this }
      book.description?.apply { bookEntity.description = this }
      book.owner?.apply { bookEntity.owner = userService.getUser(this.id) }
    }
    return bookEntity
  }

  override fun deleteBook(id: Long) {
    em.txn {
      em.remove(getBook(id))
    }
  }
}
