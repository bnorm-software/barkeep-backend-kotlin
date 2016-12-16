package com.bnorm.barkeep.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.Instant;
import java.util.List;

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
  List<Book> getBooks();

  @JsonView(User.class)
  List<Bar> getBars();
}
