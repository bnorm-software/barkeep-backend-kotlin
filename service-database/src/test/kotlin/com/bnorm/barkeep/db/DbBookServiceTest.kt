// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.BookValue
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.UserValue
import io.kotlintest.matchers.have
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException

class DbBookServiceTest : AbstractDbServiceTest() {

  @After
  @Throws(IOException::class)
  fun cleanup() {
    val books = service.getBooks()
    for (book in books) {
      service.deleteBook(book.id)
    }
  }

  // ==================== //
  // ***** POST book ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun createBook_successful() {
    // given
    val book = BookValue(title = "Book1", description = "Description1", owner = joeTestmore)

    // when
    val response = service.createBook(book)

    // then
    response.id should beValid
    response.title shouldBe "Book1"
    response.description shouldBe "Description1"
  }


  // =================== //
  // ***** GET book ***** //
  // =================== //

  @Test
  @Throws(Exception::class)
  fun getBook_successful() {
    // given
    val book = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.getBook(book.id)

    // then
    response!!
    response.id shouldBe book.id
    response.title shouldBe book.title
    response.description shouldBe book.description
  }

  @Test
  @Throws(Exception::class)
  fun getBook_failure_badId() {
    // given

    // when
    val response = service.getBook(-1)

    // then
    response shouldBe null
  }


  // ====================== //
  // ***** UPDATE book ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun setBook_successful_withValidBodyId() {
    // given
    val book = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.setBook(book.id, BookValue(title = "Book2"))

    // then
    response.id shouldBe book.id
    response.title shouldBe "Book2"
    response.description shouldBe book.description
  }

  @Test
  @Throws(Exception::class)
  fun setBook_successful_withInvalidBodyId() {
    // given
    val book = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.setBook(book.id, BookValue(title = "Book2", description = "Description2"))

    // then
    response.id shouldBe book.id
    response.title shouldBe "Book2"
    response.description shouldBe "Description2"
  }

  @Test
  @Throws(Exception::class)
  fun setBook_failure_badId() {
    // given

    try {
      // when
      service.setBook(-1, BookValue(title = "Book1", description = "Description1", owner = joeTestmore))
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "Cannot find book"
    }
  }


  // ====================== //
  // ***** DELETE book ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun deleteBook_successful() {
    // given
    val book = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    service.deleteBook(book.id)

    // then
  }

  @Test
  @Throws(Exception::class)
  fun deleteBook_failure_badId() {
    // given

    try {
      // when
      service.deleteBook(-1)
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "Cannot find book"
    }
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_empty() {
    // given

    // when
    val response = service.getBooks()

    // then
    response should haveSize(0)
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_emptyAfterDelete() {
    // given
    val book1 = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))
    service.deleteBook(book1.id)

    // when
    val response = service.getBooks()

    // then
    response should haveSize(0)
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_single() {
    // given
    val book1 = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.getBooks()

    // then
    response should haveSize(1)
    response shouldBe listOf(book1)
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_singleAfterDelete() {
    // given
    val book1 = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))
    val book2 = service.createBook(BookValue(title = "Book2", description = "Description2", owner = joeTestmore))
    val book3 = service.createBook(BookValue(title = "Book3", description = "Description3", owner = joeTestmore))
    service.deleteBook(book1.id)
    service.deleteBook(book3.id)

    // when
    val response = service.getBooks()

    // then
    response should haveSize(1)
    response shouldBe listOf(book2)
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_multiple() {
    // given
    val book1 = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))
    val book3 = service.createBook(BookValue(title = "Book3", description = "Description3", owner = joeTestmore))
    val book2 = service.createBook(BookValue(title = "Book2", description = "Description2", owner = joeTestmore))

    // when
    val response = service.getBooks()

    // then
    response should haveSize(3)
    response shouldBe listOf(book1, book3, book2)
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_multipleAfterDelete() {
    // given
    val book5 = service.createBook(BookValue(title = "Book5", description = "Description5", owner = joeTestmore))
    val book2 = service.createBook(BookValue(title = "Book2", description = "Description2", owner = joeTestmore))
    val book3 = service.createBook(BookValue(title = "Book3", description = "Description3", owner = joeTestmore))
    val book4 = service.createBook(BookValue(title = "Book4", description = "Description4", owner = joeTestmore))
    val book1 = service.createBook(BookValue(title = "Book1", description = "Description1", owner = joeTestmore))
    service.deleteBook(book4.id)
    service.deleteBook(book2.id)

    // when
    val response = service.getBooks()

    // then
    response should haveSize(3)
    response shouldBe listOf(book5, book3, book1)
  }

  companion object {

    private lateinit var service: DbBookService
    private lateinit var joeTestmore: User

    @BeforeClass
    @JvmStatic
    fun setupBookService() {
      service = DbBookService(AbstractDbServiceTest.em)

      val userService = DbUserService(AbstractDbServiceTest.em)
      joeTestmore = userService.createUser(UserValue(username = "joe", password = "testmore", email = "joe@test.more"))
    }
  }
}
