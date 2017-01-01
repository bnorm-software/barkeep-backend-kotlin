// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.junit.After;
import org.junit.Test;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.BookValue;

import retrofit2.Response;

import static com.bnorm.barkeep.model.BookAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class RestBookServiceTest extends AbstractRestServiceTest {

  @After
  public void cleanup() throws IOException {
    List<Book> books = service.getBooks().execute().body();
    if (books != null) {
      for (Book book : books) {
        service.deleteBook(book.getId()).execute();
      }
    }
  }

  // ==================== //
  // ***** POST book ***** //
  // ==================== //

  @Test
  public void createBook_successful() throws Exception {
    // given
    Book book = BookValue.builder().setTitle("Book1").setDescription("Description1").build();

    // when
    Response<Book> response = service.createBook(book).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).has(VALID_ID);
    assertThat(response.body()).hasTitle("Book1");
    assertThat(response.body()).hasDescription("Description1");
  }

  @Test
  public void createBook_failure_badId() throws Exception {
    // given
    Book book = BookValue.builder().setId(1L).setTitle("Book1").setDescription("Description1").build();

    // when
    Response<Book> response = service.createBook(book).execute();

    // then
    assertThat(response.isSuccessful()).isFalse();
    assertThat(response.raw().code()).isEqualTo(BAD_REQUEST);
  }


  // =================== //
  // ***** GET book ***** //
  // =================== //

  @Test
  public void getBook_successful() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();

    // when
    Response<Book> response = service.getBook(Objects.requireNonNull(book1.getId())).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasId(book1.getId());
    assertThat(response.body()).hasTitle("Book1");
    assertThat(response.body()).hasDescription("Description1");
  }

  @Test
  public void getBook_failure_badId() throws Exception {
    // given

    // when
    Response<Book> response = service.getBook(-1).execute();

    // then
    assertThat(response.isSuccessful()).isFalse();
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND);
  }


  // ====================== //
  // ***** UPDATE book ***** //
  // ====================== //

  @Test
  public void updateBook_successful_withValidBodyId() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();

    // when
    Response<Book> response = service.updateBook(Objects.requireNonNull(book1.getId()),
                                                 BookValue.builder().setTitle("Book2").build()).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasId(book1.getId());
    assertThat(response.body()).hasTitle("Book2");
    assertThat(response.body()).hasDescription("Description1");
  }

  @Test
  public void updateBook_successful_withInvalidBodyId() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();

    // when
    Response<Book> response = service.updateBook(Objects.requireNonNull(book1.getId()),
                                                 BookValue.builder()
                                                          .setTitle("Book2")
                                                          .setDescription("Description2")
                                                          .build()).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasId(book1.getId());
    assertThat(response.body()).hasTitle("Book2");
    assertThat(response.body()).hasDescription("Description2");
  }

  @Test
  public void updateBook_failure_badId() throws Exception {
    // given

    // when
    Response<Book> response = service.updateBook(-1,
                                                 BookValue.builder()
                                                          .setTitle("Book1")
                                                          .setDescription("Description1")
                                                          .build()).execute();

    // then
    assertThat(response.isSuccessful()).isFalse();
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND);
  }


  // ====================== //
  // ***** DELETE book ***** //
  // ====================== //

  @Test
  public void deleteBook_successful() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();

    // when
    Response<Void> response = service.deleteBook(Objects.requireNonNull(book1.getId())).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
  }

  @Test
  public void deleteBook_failure_badId() throws Exception {
    // given

    // when
    Response<Void> response = service.deleteBook(-1).execute();

    // then
    assertThat(response.isSuccessful()).isFalse();
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND);
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  public void getBooks_successful_empty() throws Exception {
    // given

    // when
    Response<List<Book>> response = service.getBooks().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).isEmpty();
  }

  @Test
  public void getBooks_successful_emptyAfterDelete() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();
    service.deleteBook(Objects.requireNonNull(book1.getId())).execute();

    // when
    Response<List<Book>> response = service.getBooks().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).isEmpty();
  }

  @Test
  public void getBooks_successful_single() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();

    // when
    Response<List<Book>> response = service.getBooks().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasSize(1);
    assertThat(response.body()).containsExactly(book1);
  }

  @Test
  public void getBooks_successful_singleAfterDelete() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();
    Book book2 = service.createBook(BookValue.builder().setTitle("Book2").setDescription("Description2").build())
                        .execute()
                        .body();
    Book book3 = service.createBook(BookValue.builder().setTitle("Book3").setDescription("Description3").build())
                        .execute()
                        .body();
    service.deleteBook(Objects.requireNonNull(book1.getId())).execute();
    service.deleteBook(Objects.requireNonNull(book3.getId())).execute();

    // when
    Response<List<Book>> response = service.getBooks().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasSize(1);
    assertThat(response.body()).containsExactly(book2);
  }

  @Test
  public void getBooks_successful_multiple() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();
    Book book3 = service.createBook(BookValue.builder().setTitle("Book3").setDescription("Description3").build())
                        .execute()
                        .body();
    Book book2 = service.createBook(BookValue.builder().setTitle("Book2").setDescription("Description2").build())
                        .execute()
                        .body();

    // when
    Response<List<Book>> response = service.getBooks().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasSize(3);
    assertThat(response.body()).containsExactly(book1, book3, book2);
  }

  @Test
  public void getBooks_successful_multipleAfterDelete() throws Exception {
    // given
    Book book5 = service.createBook(BookValue.builder().setTitle("Book5").setDescription("Description5").build())
                        .execute()
                        .body();
    Book book2 = service.createBook(BookValue.builder().setTitle("Book2").setDescription("Description2").build())
                        .execute()
                        .body();
    Book book3 = service.createBook(BookValue.builder().setTitle("Book3").setDescription("Description3").build())
                        .execute()
                        .body();
    Book book4 = service.createBook(BookValue.builder().setTitle("Book4").setDescription("Description4").build())
                        .execute()
                        .body();
    Book book1 = service.createBook(BookValue.builder().setTitle("Book1").setDescription("Description1").build())
                        .execute()
                        .body();
    service.deleteBook(Objects.requireNonNull(book4.getId())).execute();
    service.deleteBook(Objects.requireNonNull(book2.getId())).execute();

    // when
    Response<List<Book>> response = service.getBooks().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasSize(3);
    assertThat(response.body()).containsExactly(book5, book3, book1);
  }
}
