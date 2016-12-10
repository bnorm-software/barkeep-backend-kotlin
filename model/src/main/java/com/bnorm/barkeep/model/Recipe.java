package com.bnorm.barkeep.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

import javax.annotation.Nullable;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Recipe extends HasId {

  @JsonView()
  @Override
  long getId();

  @JsonView({Recipe.class, Book.class})
  String getTitle();

  @JsonView({Recipe.class, Book.class})
  @Nullable
  String getDescription();

  @JsonView({Recipe.class, Book.class})
  @Nullable
  String getImageUrl();

  @JsonView(Recipe.class)
  @Nullable
  String getInstructions();

  @JsonView(Recipe.class)
  @Nullable
  String getSource();

  Instant getCreateTime();

  Instant getModifyTime();

  @JsonView(Recipe.class)
  List<Component> getComponents();
}
