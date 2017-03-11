// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import com.bnorm.barkeep.model.value.BookSpecValue
import org.junit.After
import org.junit.Test
import java.io.IOException

class RestBookServiceTest : AbstractRestServiceTest() {

  @After
  @Throws(IOException::class)
  fun cleanup() {
    val books = service.getBooks().execute().body()
    if (books != null) {
      for (book in books) {
        service.deleteBook(book.id).execute()
      }
    }
  }

  // ==================== //
  // ***** POST book ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun createBook_successful() {
    // given
    val book = BookSpecValue(title = "Book1", description = "Description1")

    // when
    val response = service.createBook(book).execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body().id).isAtLeast(0)
      that(response.body().title).isEqualTo(book.title)
      that(response.body().description).isEqualTo(book.description)
    }
  }


  // =================== //
  // ***** GET book ***** //
  // =================== //

  @Test
  @Throws(Exception::class)
  fun getBook_successful() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()

    // when
    val response = service.getBook(book.id).execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body().id).isAtLeast(book.id)
      that(response.body().title).isEqualTo(book.title)
      that(response.body().description).isEqualTo(book.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBook_failure_badId() {
    // given

    // when
    val response = service.getBook(-1).execute()

    // then
    assert {
      that(response.isSuccessful).isFalse()
      that(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
    }
  }


  // ====================== //
  // ***** UPDATE book ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun updateBook_successful_withValidBodyId() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()

    // when
    val response = service.updateBook(book.id, BookSpecValue(title = "Book2")).execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body().id).isAtLeast(book.id)
      that(response.body().title).isEqualTo("Book2")
      that(response.body().description).isEqualTo(book.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun updateBook_successful_withInvalidBodyId() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()

    // when
    val response = service.updateBook(book.id, BookSpecValue(title = "Book2", description = "Description2")).execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body().id).isAtLeast(book.id)
      that(response.body().title).isEqualTo("Book2")
      that(response.body().description).isEqualTo("Description2")
    }
  }

  @Test
  @Throws(Exception::class)
  fun updateBook_failure_badId() {
    // given

    // when
    val response = service.updateBook(-1, BookSpecValue(title = "Book1", description = "Description1")).execute()

    // then
    assert {
      that(response.isSuccessful).isFalse()
      that(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
    }
  }


  // ====================== //
  // ***** DELETE book ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun deleteBook_successful() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()

    // when
    val response = service.deleteBook(book.id).execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
    }
  }

  @Test
  @Throws(Exception::class)
  fun deleteBook_failure_badId() {
    // given

    // when
    val response = service.deleteBook(-1).execute()

    // then
    assert {
      that(response.isSuccessful).isFalse()
      that(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
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
    val response = service.getBooks().execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body()).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_emptyAfterDelete() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()
    service.deleteBook(book.id).execute()

    // when
    val response = service.getBooks().execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body()).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_single() {
    // given
    val book = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()

    // when
    val response = service.getBooks().execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body()).hasSize(1)
      that(response.body()).containsExactly(book)
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_singleAfterDelete() {
    // given
    val book1 = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()
    val book2 = service.createBook(BookSpecValue(title = "Book2", description = "Description2")).execute().body()
    val book3 = service.createBook(BookSpecValue(title = "Book3", description = "Description3")).execute().body()
    service.deleteBook(book1.id).execute()
    service.deleteBook(book3.id).execute()

    // when
    val response = service.getBooks().execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body()).hasSize(1)
      that(response.body()).containsExactly(book2)
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_multiple() {
    // given
    val book1 = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()
    val book3 = service.createBook(BookSpecValue(title = "Book3", description = "Description3")).execute().body()
    val book2 = service.createBook(BookSpecValue(title = "Book2", description = "Description2")).execute().body()

    // when
    val response = service.getBooks().execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body()).hasSize(3)
      that(response.body()).containsExactly(book1, book3, book2)
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_multipleAfterDelete() {
    // given
    val book5 = service.createBook(BookSpecValue(title = "Book5", description = "Description5")).execute().body()
    val book2 = service.createBook(BookSpecValue(title = "Book2", description = "Description2")).execute().body()
    val book3 = service.createBook(BookSpecValue(title = "Book3", description = "Description3")).execute().body()
    val book4 = service.createBook(BookSpecValue(title = "Book4", description = "Description4")).execute().body()
    val book1 = service.createBook(BookSpecValue(title = "Book1", description = "Description1")).execute().body()
    service.deleteBook(book4.id).execute()
    service.deleteBook(book2.id).execute()

    // when
    val response = service.getBooks().execute()

    // then
    assert {
      that(response.isSuccessful).isTrue()
      that(response.raw().code()).isEqualTo(CODE_SUCCESS)
      that(response.body()).hasSize(3)
      that(response.body()).containsExactly(book5, book3, book1)
    }
  }
}
