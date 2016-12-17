package com.bnorm.barkeep.model;

import java.time.Instant;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface User extends HasId {

  @JsonView(User.class)
  long getId();

  @JsonView(User.class)
  String getUsername();

  String getPassword();

  @JsonView(User.class)
  String getDisplayName();

  @JsonView(User.class)
  String getEmail();

  Instant getCreateTime();

  Instant getModifyTime();

  @JsonView(User.class)
  Collection<Book> getBooks();

  @JsonView(User.class)
  Collection<Bar> getBars();
}
