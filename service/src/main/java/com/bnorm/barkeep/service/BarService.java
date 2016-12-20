// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service;

import java.util.Collection;

import com.bnorm.barkeep.model.Bar;

public interface BarService {

  Collection<Bar> getBars();

  Bar getBar(long barId);

  Bar createBar(Bar bar);

  Bar setBar(long barId, Bar bar);

  void deleteBar(long barId);
}
