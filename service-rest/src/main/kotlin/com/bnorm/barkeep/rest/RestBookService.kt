// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.db.DbBookService
import com.bnorm.barkeep.db.DbUserService
import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.BookSpec
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.service.BookService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class RestBookService(private val userService: DbUserService,
                      private val bookService: DbBookService) : AbstractRestService(), BookService {

  private fun findByOwner(userId: Long, bookId: Long): Book {
    val book = bookService.getBook(bookId)
    if (!isOwnedBy(book, userId)) {
      throw NotFound("Unable to find book with id=$bookId")
    }
    return book!!
  }

  private fun findByUser(user: User?, bookId: Long): Book {
    val book = bookService.getBook(bookId)
    val books = user?.books
    if (book == null || books != null && !books.contains(book)) {
      throw NotFound("Unable to find book with id=$bookId")
    }
    return book
  }

  @JsonView(Book::class)
  @RequestMapping(method = arrayOf(RequestMethod.GET))
  override fun getBooks(): Collection<Book> {
    val user = userService.getUser(currentUser().id)
    return user!!.books
  }

  @JsonView(Book::class)
  @RequestMapping(value = "/{bookId}", method = arrayOf(RequestMethod.GET))
  override fun getBook(@PathVariable("bookId") id: Long): Book? {
    val user = userService.getUser(currentUser().id)
    return findByUser(user, id)
  }

  @JsonView(Recipe::class)
  @RequestMapping(value = "/{bookId}/recipes", method = arrayOf(RequestMethod.GET))
  fun getBookRecipes(@PathVariable("bookId") id: Long): Set<Recipe> {
    val book = getBook(id)
    return book!!.recipes!!
  }

  @JsonView(Book::class)
  @RequestMapping(method = arrayOf(RequestMethod.POST))
  override fun createBook(@RequestBody book: BookSpec): Book {
    val currentUser = currentUser()
    if (book.owner == null) {
      return bookService.createBook(BookWithOwner(book, currentUser))
    } else if (!isOwnedBy(book, currentUser.id)) {
      throw BadRequest("Cannot create book owned by another user")
    } else {
      return bookService.createBook(book)
    }
  }

  @JsonView(Book::class)
  @RequestMapping(value = "/{bookId}", method = arrayOf(RequestMethod.PUT))
  override fun setBook(@PathVariable("bookId") id: Long, @RequestBody book: BookSpec): Book {
    findByOwner(currentUser().id, id)
    return bookService.setBook(id, book)
  }

  @RequestMapping(value = "/{bookId}", method = arrayOf(RequestMethod.DELETE))
  override fun deleteBook(@PathVariable("bookId") id: Long) {
    findByOwner(currentUser().id, id)
    bookService.deleteBook(id)
  }

  private class BookWithOwner(private val book: BookSpec, override val owner: User) : BookSpec {
    override val title: String?
      get() = book.title
    override val description: String?
      get() = book.description
  }
}
