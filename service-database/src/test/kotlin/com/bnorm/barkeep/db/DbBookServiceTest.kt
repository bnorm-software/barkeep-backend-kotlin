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
      service.deleteBook(book.id!!)
    }
  }

  // ==================== //
  // ***** POST book ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun createBook_successful() {
    // given
    val book = BookValue.builder().setTitle("Book1").setDescription("Description1").setOwner(joeTestmore).build()

    // when
    val response = service.createBook(book)

    // then
    response.id should beValid
    response.title shouldBe "Book1"
    response.description shouldBe "Description1"
  }

  @Test
  @Throws(Exception::class)
  fun createBook_failure_badId() {
    // given
    val book = BookValue.builder()
            .setId(1L)
            .setTitle("Book1")
            .setDescription("Description1")
            .setOwner(joeTestmore)
            .build()

    try {
      // when
      service.createBook(book)
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "already has an id"
    }
  }


  // =================== //
  // ***** GET book ***** //
  // =================== //

  @Test
  @Throws(Exception::class)
  fun getBook_successful() {
    // given
    val book = service.createBook(BookValue.builder()
                                          .setTitle("Book1")
                                          .setDescription("Description1")
                                          .setOwner(joeTestmore)
                                          .build())

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
    val book = service.createBook(BookValue.builder()
                                          .setTitle("Book1")
                                          .setDescription("Description1")
                                          .setOwner(joeTestmore)
                                          .build())

    // when
    val response = service.setBook(book.id, BookValue.builder().setTitle("Book2").build())

    // then
    response.id shouldBe book.id
    response.title shouldBe "Book2"
    response.description shouldBe book.description
  }

  @Test
  @Throws(Exception::class)
  fun setBook_successful_withInvalidBodyId() {
    // given
    val book = service.createBook(BookValue.builder()
                                          .setTitle("Book1")
                                          .setDescription("Description1")
                                          .setOwner(joeTestmore)
                                          .build())

    // when
    val response = service.setBook(book.id,
                                   BookValue.builder().setTitle("Book2").setDescription("Description2").build())

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
      service.setBook(-1,
                      BookValue.builder()
                              .setTitle("Book1")
                              .setDescription("Description1")
                              .setOwner(joeTestmore)
                              .build())
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
    val book1 = service.createBook(BookValue.builder()
                                           .setTitle("Book1")
                                           .setDescription("Description1")
                                           .setOwner(joeTestmore)
                                           .build())

    // when
    service.deleteBook(book1.id)

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
    val book1 = service.createBook(BookValue.builder()
                                           .setTitle("Book1")
                                           .setDescription("Description1")
                                           .setOwner(joeTestmore)
                                           .build())
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
    val book1 = service.createBook(BookValue.builder()
                                           .setTitle("Book1")
                                           .setDescription("Description1")
                                           .setOwner(joeTestmore)
                                           .build())

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
    val book1 = service.createBook(BookValue.builder()
                                           .setTitle("Book1")
                                           .setDescription("Description1")
                                           .setOwner(joeTestmore)
                                           .build())
    val book2 = service.createBook(BookValue.builder()
                                           .setTitle("Book2")
                                           .setDescription("Description2")
                                           .setOwner(joeTestmore)
                                           .build())
    val book3 = service.createBook(BookValue.builder()
                                           .setTitle("Book3")
                                           .setDescription("Description3")
                                           .setOwner(joeTestmore)
                                           .build())
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
    val book1 = service.createBook(BookValue.builder()
                                           .setTitle("Book1")
                                           .setDescription("Description1")
                                           .setOwner(joeTestmore)
                                           .build())
    val book3 = service.createBook(BookValue.builder()
                                           .setTitle("Book3")
                                           .setDescription("Description3")
                                           .setOwner(joeTestmore)
                                           .build())
    val book2 = service.createBook(BookValue.builder()
                                           .setTitle("Book2")
                                           .setDescription("Description2")
                                           .setOwner(joeTestmore)
                                           .build())

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
    val book5 = service.createBook(BookValue.builder()
                                           .setTitle("Book5")
                                           .setDescription("Description5")
                                           .setOwner(joeTestmore)
                                           .build())
    val book2 = service.createBook(BookValue.builder()
                                           .setTitle("Book2")
                                           .setDescription("Description2")
                                           .setOwner(joeTestmore)
                                           .build())
    val book3 = service.createBook(BookValue.builder()
                                           .setTitle("Book3")
                                           .setDescription("Description3")
                                           .setOwner(joeTestmore)
                                           .build())
    val book4 = service.createBook(BookValue.builder()
                                           .setTitle("Book4")
                                           .setDescription("Description4")
                                           .setOwner(joeTestmore)
                                           .build())
    val book1 = service.createBook(BookValue.builder()
                                           .setTitle("Book1")
                                           .setDescription("Description1")
                                           .setOwner(joeTestmore)
                                           .build())
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
      joeTestmore = userService.createUser(UserValue.builder()
                                                   .setUsername("joe")
                                                   .setPassword("testmore")
                                                   .setEmail("joe@test.more")
                                                   .build())
    }
  }
}
