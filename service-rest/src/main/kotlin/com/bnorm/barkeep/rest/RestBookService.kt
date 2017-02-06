// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.db.DbBookService
import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.BookSpec
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.service.BookService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class RestBookService(private val bookService: DbBookService,
                      private val userService: RestUserService,
                      private val recipeService: RestRecipeService) : BookService {

  private fun isOwnedBy(book: BookSpec, userId: Long): Boolean = book.owner?.id == userId

  private fun assertAccess(book: Book) {
    val user = userService.getUser()
    val books = user.books
    if (!books.contains(book)) {
      throw NotFound(MSG_NOT_FOUND(TYPE_BOOK, book.id))
    }
  }

  private fun assertOwnership(book: Book) {
    if (!isOwnedBy(book, userService.currentId())) {
      throw Forbidden(MSG_DO_NOT_OWN(TYPE_BOOK, book.id))
    }
  }

  @JsonView(Book::class)
  @GetMapping
  override fun getBooks(): Collection<Book> {
    val user = userService.getUser()
    return user.books
  }

  @JsonView(Book::class)
  @GetMapping("/{bookId}")
  override fun getBook(@PathVariable("bookId") id: Long): Book {
    val book = bookService.findBook(id) ?: throw NotFound(MSG_NOT_FOUND(TYPE_BOOK, id))
    assertAccess(book)
    return book
  }

  @JsonView(Recipe::class)
  @GetMapping("/{bookId}/recipes")
  fun getBookRecipes(@PathVariable("bookId") id: Long): Set<Recipe> {
    val book = getBook(id)
    return book.recipes
  }

  @JsonView(Book::class)
  @PostMapping
  override fun createBook(@RequestBody book: BookSpec): Book {
    val currentUser = userService.getUser()
    if (book.owner == null) {
      return bookService.createBook(BookWithOwner(book, currentUser))
    } else if (!isOwnedBy(book, currentUser.id)) {
      throw BadRequest(MSG_CREATE_WRONG_USER("book"))
    } else {
      return bookService.createBook(book)
    }
  }

  @PostMapping("/{bookId}/recipes/{recipeId}")
  override fun addBookRecipe(@PathVariable("bookId") bookId: Long, @PathVariable("recipeId") recipeId: Long) {
    val book = getBook(bookId)
    val recipe = recipeService.getRecipe(recipeId)
    bookService.addBookRecipe(book.id, recipe.id)
  }

  @DeleteMapping("{/bookId}/recipes/{recipeId}")
  override fun removeBookRecipe(@PathVariable("bookId") bookId: Long, @PathVariable("recipeId") recipeId: Long) {
    val book = getBook(bookId)
    val recipe = recipeService.getRecipe(recipeId)
    bookService.removeBookRecipe(book.id, recipe.id)
  }

  @JsonView(Book::class)
  @PutMapping("/{bookId}")
  override fun setBook(@PathVariable("bookId") id: Long, @RequestBody book: BookSpec): Book {
    assertOwnership(getBook(id))
    return bookService.setBook(id, book)
  }

  @DeleteMapping("/{bookId}")
  override fun deleteBook(@PathVariable("bookId") id: Long) {
    assertOwnership(getBook(id))
    bookService.deleteBook(id)
  }

  private class BookWithOwner(private val book: BookSpec, override val owner: User) : BookSpec {
    override val title: String?
      get() = book.title
    override val description: String?
      get() = book.description
  }
}
