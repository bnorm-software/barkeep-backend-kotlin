package com.bnorm.barkeep.model;

import com.fasterxml.jackson.annotation.JsonView;

interface HasId {

  @JsonView(HasId.class)
  long getId();
}
