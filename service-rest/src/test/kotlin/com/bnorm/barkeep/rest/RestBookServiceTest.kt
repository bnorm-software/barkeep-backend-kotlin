// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.BookValue
import org.assertj.core.api.Assertions.assertThat
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
        service.deleteBook(book.id!!).execute()
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
    val book = BookValue.builder().setTitle("Book1").setDescription("Description1").build()

    // when
    val response = service.createBook(book).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    response.body().id!! should beValid
    response.body().title shouldBe book.title
    response.body().description shouldBe book.description
  }

  @Test
  @Throws(Exception::class)
  fun createBook_failure_badId() {
    // given
    val book = BookValue.builder()
            .setId(1L)
            .setTitle("Book1")
            .setDescription("Description1")
            .build()

    // when
    val response = service.createBook(book).execute()

    // then
    assertThat(response.isSuccessful()).isFalse()
    assertThat(response.raw().code()).isEqualTo(BAD_REQUEST)
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
                                          .build())
            .execute()
            .body()

    // when
    val response = service.getBook(book.id!!).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    response.body().id shouldBe book.id
    response.body().title shouldBe book.title
    response.body().description shouldBe book.description
  }

  @Test
  @Throws(Exception::class)
  fun getBook_failure_badId() {
    // given

    // when
    val response = service.getBook(-1).execute()

    // then
    assertThat(response.isSuccessful()).isFalse()
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
  }


  // ====================== //
  // ***** UPDATE book ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun updateBook_successful_withValidBodyId() {
    // given
    val book = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
            .execute()
            .body()

    // when
    val response = service.updateBook(book.id!!,
                                      BookValue.builder()
                                              .setTitle("Book2")
                                              .build()).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    response.body().id shouldBe book.id
    response.body().title shouldBe "Book2"
    response.body().description shouldBe book.description
  }

  @Test
  @Throws(Exception::class)
  fun updateBook_successful_withInvalidBodyId() {
    // given
    val book = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
            .execute()
            .body()

    // when
    val response = service.updateBook(book.id!!,
                                      BookValue.builder()
                                              .setTitle("Book2")
                                              .setDescription("Description2")
                                              .build()).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    response.body().id shouldBe book.id
    response.body().title shouldBe "Book2"
    response.body().description shouldBe "Description2"
  }

  @Test
  @Throws(Exception::class)
  fun updateBook_failure_badId() {
    // given

    // when
    val response = service.updateBook(-1,
                                      BookValue.builder()
                                              .setTitle("Book1")
                                              .setDescription("Description1")
                                              .build()).execute()

    // then
    assertThat(response.isSuccessful()).isFalse()
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
  }


  // ====================== //
  // ***** DELETE book ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun deleteBook_successful() {
    // given
    val book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
            .execute()
            .body()

    // when
    val response = service.deleteBook(book1.id!!).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
  }

  @Test
  @Throws(Exception::class)
  fun deleteBook_failure_badId() {
    // given

    // when
    val response = service.deleteBook(-1).execute()

    // then
    assertThat(response.isSuccessful()).isFalse()
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
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
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Book>(response.body()).isEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_emptyAfterDelete() {
    // given
    val book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
            .execute()
            .body()
    service.deleteBook(book1.id!!).execute()

    // when
    val response = service.getBooks().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Book>(response.body()).isEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_single() {
    // given
    val book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
            .execute()
            .body()

    // when
    val response = service.getBooks().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Book>(response.body()).hasSize(1)
    assertThat<Book>(response.body()).containsExactly(book1)
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_singleAfterDelete() {
    // given
    val book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
            .execute()
            .body()
    val book2 = service.createBook(BookValue.builder().setTitle("Book2").setDescription("Description2").build())
            .execute()
            .body()
    val book3 = service.createBook(BookValue.builder().setTitle("Book3").setDescription("Description3").build())
            .execute()
            .body()
    service.deleteBook(book1.id!!).execute()
    service.deleteBook(book3.id!!).execute()

    // when
    val response = service.getBooks().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Book>(response.body()).hasSize(1)
    assertThat<Book>(response.body()).containsExactly(book2)
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_multiple() {
    // given
    val book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
            .execute()
            .body()
    val book3 = service.createBook(BookValue.builder().setTitle("Book3").setDescription("Description3").build())
            .execute()
            .body()
    val book2 = service.createBook(BookValue.builder().setTitle("Book2").setDescription("Description2").build())
            .execute()
            .body()

    // when
    val response = service.getBooks().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Book>(response.body()).hasSize(3)
    assertThat<Book>(response.body()).containsExactly(book1, book3, book2)
  }

  @Test
  @Throws(Exception::class)
  fun getBooks_successful_multipleAfterDelete() {
    // given
    val book5 = service.createBook(BookValue.builder().setTitle("Book5").setDescription("Description5").build())
            .execute()
            .body()
    val book2 = service.createBook(BookValue.builder().setTitle("Book2").setDescription("Description2").build())
            .execute()
            .body()
    val book3 = service.createBook(BookValue.builder().setTitle("Book3").setDescription("Description3").build())
            .execute()
            .body()
    val book4 = service.createBook(BookValue.builder().setTitle("Book4").setDescription("Description4").build())
            .execute()
            .body()
    val book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
            .execute()
            .body()
    service.deleteBook(book4.id!!).execute()
    service.deleteBook(book2.id!!).execute()

    // when
    val response = service.getBooks().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Book>(response.body()).hasSize(3)
    assertThat<Book>(response.body()).containsExactly(book5, book3, book1)
  }
}
