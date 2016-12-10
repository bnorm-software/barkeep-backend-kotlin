package com.bnorm.barkeep.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.annotation.Nullable;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Ingredient extends HasId {

  @JsonView({Ingredient.class, Recipe.class})
  String getTitle();

  @JsonView(Ingredient.class)
  @Nullable
  Ingredient getParent();

  Instant getCreateTime();

  Instant getModifyTime();
}
