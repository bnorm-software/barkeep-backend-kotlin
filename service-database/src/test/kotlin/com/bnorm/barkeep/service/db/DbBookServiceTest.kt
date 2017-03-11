// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.value.BookSpecValue
import com.bnorm.barkeep.model.value.UserSpecValue
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

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
    val book = BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore)

    // when
    val response = service.createBook(book)

    // then
    assert {
      that(response.id).isAtLeast(0)
      that(response.title).isEqualTo("Book1")
      that(response.description).isEqualTo("Description1")
    }
  }


  // =================== //
  // ***** GET book ***** //
  // =================== //

  @Test
  @Throws(Exception::class)
  fun getBook_successful() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.getBook(book.id)

    // then
    assert {
      that(response.id).isEqualTo(book.id)
      that(response.title).isEqualTo(book.title)
      that(response.description).isEqualTo(book.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBook_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.getBook(-1)
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find book with id=-1")
      }
    }
  }


  // ====================== //
  // ***** UPDATE book ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun setBook_successful_withValidBodyId() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.setBook(book.id, BookSpecValue(title = "Book2"))

    // then
    assert {
      that(response.id).isEqualTo(book.id)
      that(response.title).isEqualTo("Book2")
      that(response.description).isEqualTo(book.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun setBook_successful_withInvalidBodyId() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.setBook(book.id, BookSpecValue(title = "Book2", description = "Description2"))

    // then
    assert {
      that(response.id).isEqualTo(book.id)
      that(response.title).isEqualTo("Book2")
      that(response.description).isEqualTo("Description2")
    }
  }

  @Test
  @Throws(Exception::class)
  fun setBook_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.setBook(-1, BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find book with id=-1")
      }
    }
  }


  // ====================== //
  // ***** DELETE book ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun deleteBook_successful() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    service.deleteBook(book.id)

    // then
  }

  @Test
  @Throws(Exception::class)
  fun deleteBook_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.deleteBook(-1)
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find book with id=-1")
      }
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
    assert {
      that(response).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_emptyAfterDelete() {
    // given
    val book1 = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))
    service.deleteBook(book1.id)

    // when
    val response = service.getBooks()

    // then
    assert {
      that(response).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_single() {
    // given
    val book1 = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.getBooks()

    // then
    assert {
      all(that(response)) {
        hasSize(1)
        containsExactly(book1)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_singleAfterDelete() {
    // given
    val book1 = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))
    val book2 = service.createBook(BookSpecValue(title = "Book2", description = "Description2", owner = joeTestmore))
    val book3 = service.createBook(BookSpecValue(title = "Book3", description = "Description3", owner = joeTestmore))
    service.deleteBook(book1.id)
    service.deleteBook(book3.id)

    // when
    val response = service.getBooks()

    // then
    assert {
      all(that(response)) {
        hasSize(1)
        containsExactly(book2)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_multiple() {
    // given
    val book1 = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))
    val book3 = service.createBook(BookSpecValue(title = "Book3", description = "Description3", owner = joeTestmore))
    val book2 = service.createBook(BookSpecValue(title = "Book2", description = "Description2", owner = joeTestmore))

    // when
    val response = service.getBooks()

    // then
    assert {
      all(that(response)) {
        hasSize(3)
        containsExactly(book1, book3, book2)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_multipleAfterDelete() {
    // given
    val book5 = service.createBook(BookSpecValue(title = "Book5", description = "Description5", owner = joeTestmore))
    val book2 = service.createBook(BookSpecValue(title = "Book2", description = "Description2", owner = joeTestmore))
    val book3 = service.createBook(BookSpecValue(title = "Book3", description = "Description3", owner = joeTestmore))
    val book4 = service.createBook(BookSpecValue(title = "Book4", description = "Description4", owner = joeTestmore))
    val book1 = service.createBook(BookSpecValue(title = "Book1", description = "Description1", owner = joeTestmore))
    service.deleteBook(book4.id)
    service.deleteBook(book2.id)

    // when
    val response = service.getBooks()

    // then
    assert {
      all(that(response)) {
        hasSize(3)
        containsExactly(book5, book3, book1)
      }
    }
  }

  companion object {

    private lateinit var service: DbBookService
    private lateinit var joeTestmore: User

    @BeforeClass
    @JvmStatic
    fun setupBookService() {
      val userService = DbUserService(emPool)
      service = DbBookService(emPool, DbRecipeService(emPool, DbIngredientService(emPool), userService), userService)

      joeTestmore = userService.createUser(UserSpecValue(username = "joe",
                                                         password = "testmore",
                                                         email = "joe@test.more"))
    }
  }
}
