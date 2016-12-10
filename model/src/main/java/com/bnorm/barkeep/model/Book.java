package com.bnorm.barkeep.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.annotation.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Book extends HasId {

  @JsonView({Book.class, User.class})
  String getTitle();

  @JsonView({Book.class, User.class})
  @Nullable
  String getDescription();

  Instant getCreateTime();

  Instant getModifyTime();

  @JsonView(Book.class)
  Map<Long, Recipe> getRecipes();
}
