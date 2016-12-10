package com.bnorm.barkeep.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.annotation.Nullable;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Bar extends HasId {

  @JsonView({Bar.class, User.class})
  @Nullable
  String getTitle();

  @JsonView({Bar.class, User.class})
  @Nullable
  String getDescription();

  Instant getCreateTime();

  Instant getModifyTime();

  @JsonView(Bar.class)
  List<Ingredient> getIngredients();
}
