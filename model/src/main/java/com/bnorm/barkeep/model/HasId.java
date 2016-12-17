package com.bnorm.barkeep.model;

import java.util.Comparator;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonView;

public interface HasId {

  Comparator<HasId> COMPARATOR = Comparator.comparing(HasId::getId);

  @JsonView(HasId.class)
  @Nullable
  Long getId();
}
