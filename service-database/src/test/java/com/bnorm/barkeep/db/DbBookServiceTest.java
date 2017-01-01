// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.BookValue;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.model.UserValue;

import static com.bnorm.barkeep.model.BookAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class DbBookServiceTest extends AbstractDbServiceTest {

  private static DbBookService service;
  private static User joeTestmore;

  @BeforeClass
  public static void setupBookService() {
    service = new DbBookService(em);

    DbUserService userService = new DbUserService(em);
    joeTestmore = userService.createUser(UserValue.builder()
                                                  .setUsername("joe")
                                                  .setPassword("testmore")
                                                  .setEmail("joe@test.more")
                                                  .build());
  }

  @After
  public void cleanup() throws IOException {
    Collection<Book> books = service.getBooks();
    if (books != null) {
      for (Book book : books) {
        service.deleteBook(book.getId());
      }
    }
  }

  // ==================== //
  // ***** POST book ***** //
  // ==================== //

  @Test
  public void createBook_successful() throws Exception {
    // given
    Book book = BookValue.builder().setTitle("Book1").setDescription("Description1").setOwner(joeTestmore).build();

    // when
    BookEntity response = service.createBook(book);

    // then
    assertThat(response).has(VALID_ID);
    assertThat(response).hasTitle("Book1");
    assertThat(response).hasDescription("Description1");
  }

  @Test
  public void createBook_failure_badId() throws Exception {
    // given
    Book book = BookValue.builder()
                         .setId(1L)
                         .setTitle("Book1")
                         .setDescription("Description1")
                         .setOwner(joeTestmore)
                         .build();

    try {
      // when
      service.createBook(book);
      fail("Did not fail as expected");
    } catch (IllegalArgumentException e) {
      // then
      assertThat(e).hasMessageContaining("already has an id");
    }
  }


  // =================== //
  // ***** GET book ***** //
  // =================== //

  @Test
  public void getBook_successful() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());

    // when
    BookEntity response = service.getBook(Objects.requireNonNull(book1.getId()));

    // then
    assertThat(response).hasId(book1.getId());
    assertThat(response).hasTitle("Book1");
    assertThat(response).hasDescription("Description1");
  }

  @Test
  public void getBook_failure_badId() throws Exception {
    // given

    // when
    BookEntity response = service.getBook(-1);

    // then
    assertThat(response).isNull();
  }


  // ====================== //
  // ***** UPDATE book ***** //
  // ====================== //

  @Test
  public void setBook_successful_withValidBodyId() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());

    // when
    BookEntity response = service.setBook(Objects.requireNonNull(book1.getId()),
                                          BookValue.builder().setTitle("Book2").build());

    // then
    assertThat(response).hasId(book1.getId());
    assertThat(response).hasTitle("Book2");
    assertThat(response).hasDescription("Description1");
  }

  @Test
  public void setBook_successful_withInvalidBodyId() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());

    // when
    BookEntity response = service.setBook(Objects.requireNonNull(book1.getId()),
                                          BookValue.builder().setTitle("Book2").setDescription("Description2").build());

    // then
    assertThat(response).hasId(book1.getId());
    assertThat(response).hasTitle("Book2");
    assertThat(response).hasDescription("Description2");
  }

  @Test
  public void setBook_failure_badId() throws Exception {
    // given

    try {
      // when
      service.setBook(-1,
                      BookValue.builder()
                               .setTitle("Book1")
                               .setDescription("Description1")
                               .setOwner(joeTestmore)
                               .build());
      fail("Did not fail as expected");
    } catch (IllegalArgumentException e) {
      // then
      assertThat(e).hasMessageContaining("Cannot find book");
    }
  }


  // ====================== //
  // ***** DELETE book ***** //
  // ====================== //

  @Test
  public void deleteBook_successful() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());

    // when
    service.deleteBook(Objects.requireNonNull(book1.getId()));

    // then
  }

  @Test
  public void deleteBook_failure_badId() throws Exception {
    // given

    try {
      // when
      service.deleteBook(-1);
      fail("Did not fail as expected");
    } catch (IllegalArgumentException e) {
      // then
      assertThat(e).hasMessageContaining("Cannot find book");
    }
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  public void getBooks_successful_empty() throws Exception {
    // given

    // when
    Collection<Book> response = service.getBooks();

    // then
    assertThat(response).isEmpty();
  }

  @Test
  public void getBooks_successful_emptyAfterDelete() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());
    service.deleteBook(Objects.requireNonNull(book1.getId()));

    // when
    Collection<Book> response = service.getBooks();

    // then
    assertThat(response).isEmpty();
  }

  @Test
  public void getBooks_successful_single() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());

    // when
    Collection<Book> response = service.getBooks();

    // then
    assertThat(response).hasSize(1);
    assertThat(response).containsExactly(book1);
  }

  @Test
  public void getBooks_successful_singleAfterDelete() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());
    Book book2 = service.createBook(BookValue.builder()
                                             .setTitle("Book2")
                                             .setDescription("Description2")
                                             .setOwner(joeTestmore)
                                             .build());
    Book book3 = service.createBook(BookValue.builder()
                                             .setTitle("Book3")
                                             .setDescription("Description3")
                                             .setOwner(joeTestmore)
                                             .build());
    service.deleteBook(Objects.requireNonNull(book1.getId()));
    service.deleteBook(Objects.requireNonNull(book3.getId()));

    // when
    Collection<Book> response = service.getBooks();

    // then
    assertThat(response).hasSize(1);
    assertThat(response).containsExactly(book2);
  }

  @Test
  public void getBooks_successful_multiple() throws Exception {
    // given
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());
    Book book3 = service.createBook(BookValue.builder()
                                             .setTitle("Book3")
                                             .setDescription("Description3")
                                             .setOwner(joeTestmore)
                                             .build());
    Book book2 = service.createBook(BookValue.builder()
                                             .setTitle("Book2")
                                             .setDescription("Description2")
                                             .setOwner(joeTestmore)
                                             .build());

    // when
    Collection<Book> response = service.getBooks();

    // then
    assertThat(response).hasSize(3);
    assertThat(response).containsExactly(book1, book3, book2);
  }

  @Test
  public void getBooks_successful_multipleAfterDelete() throws Exception {
    // given
    Book book5 = service.createBook(BookValue.builder()
                                             .setTitle("Book5")
                                             .setDescription("Description5")
                                             .setOwner(joeTestmore)
                                             .build());
    Book book2 = service.createBook(BookValue.builder()
                                             .setTitle("Book2")
                                             .setDescription("Description2")
                                             .setOwner(joeTestmore)
                                             .build());
    Book book3 = service.createBook(BookValue.builder()
                                             .setTitle("Book3")
                                             .setDescription("Description3")
                                             .setOwner(joeTestmore)
                                             .build());
    Book book4 = service.createBook(BookValue.builder()
                                             .setTitle("Book4")
                                             .setDescription("Description4")
                                             .setOwner(joeTestmore)
                                             .build());
    Book book1 = service.createBook(BookValue.builder()
                                             .setTitle("Book1")
                                             .setDescription("Description1")
                                             .setOwner(joeTestmore)
                                             .build());
    service.deleteBook(Objects.requireNonNull(book4.getId()));
    service.deleteBook(Objects.requireNonNull(book2.getId()));

    // when
    Collection<Book> response = service.getBooks();

    // then
    assertThat(response).hasSize(3);
    assertThat(response).containsExactly(book5, book3, book1);
  }
}
