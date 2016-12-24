// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.controller;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bnorm.barkeep.db.DbBarService;
import com.bnorm.barkeep.db.DbUserService;
import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.BarService;
import com.bnorm.barkeep.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/bars")
public class RestBarService extends RestService implements BarService {

  private final UserService userService;
  private final BarService barService;

  @Autowired
  public RestBarService(DbUserService userService, DbBarService barService) {
    this.userService = userService;
    this.barService = barService;
  }

  private Bar findByOwner(long userId, long barId) {
    Bar bar = barService.getBar(barId);
    if (!isOwnedBy(bar, userId)) {
      throw new NotFound("Unable to find bar with id=%d", barId);
    }
    return bar;
  }

  private Bar findByUser(User user, long barId) {
    Bar bar = barService.getBar(barId);
    if (bar == null || (user.getBars() != null && !user.getBars().contains(bar))) {
      throw new NotFound("Unable to find bar with id=%d", barId);
    }
    return bar;
  }

  @JsonView(Bar.class)
  @RequestMapping(method = RequestMethod.GET)
  @Override
  public Collection<Bar> getBars() {
    User user = userService.getUser(currentUser().getId());
    return user.getBars();
  }

  @JsonView(Bar.class)
  @RequestMapping(value = "/{barId}", method = RequestMethod.GET)
  @Override
  public Bar getBar(@PathVariable("barId") long barId) {
    User user = userService.getUser(currentUser().getId());
    return findByUser(user, barId);
  }

  @JsonView(Recipe.class)
  @RequestMapping(value = "/{barId}/ingredients", method = RequestMethod.GET)
  public Set<Ingredient> getBookRecipes(@PathVariable("barId") long barId) {
    Bar bar = getBar(barId);
    return bar.getIngredients();
  }

  @JsonView(Bar.class)
  @RequestMapping(method = RequestMethod.POST)
  @Override
  public Bar createBar(@RequestBody Bar bar) {
    if (bar.getId() != null) {
      throw new BadRequest("Cannot create bar with existing id=%d", bar.getId());
    }
    User currentUser = currentUser();
    if (bar.getOwner() == null) {
      return barService.createBar(new BarWithOwner(bar, currentUser));
    } else if (!isOwnedBy(bar, currentUser.getId())) {
      throw new BadRequest("Cannot create bar owned by another user");
    } else {
      return barService.createBar(bar);
    }
  }

  @JsonView(Bar.class)
  @RequestMapping(value = "/{barId}", method = RequestMethod.PUT)
  @Override
  public Bar setBar(@PathVariable("barId") long barId, @RequestBody Bar bar) {
    findByOwner(currentUser().getId(), barId);
    return barService.setBar(barId, bar);
  }

  @RequestMapping(value = "/{barId}", method = RequestMethod.DELETE)
  @Override
  public void deleteBar(@PathVariable("barId") long barId) {
    findByOwner(currentUser().getId(), barId);
    barService.deleteBar(barId);
  }

  private static class BarWithOwner implements Bar {

    private final Bar bar;
    private final User owner;

    private BarWithOwner(Bar bar, User owner) {
      this.bar = bar;
      this.owner = owner;
    }

    @Override
    public Long getId() {
      return bar.getId();
    }

    @Nullable
    @Override
    public String getTitle() {
      return bar.getTitle();
    }

    @Nullable
    @Override
    public String getDescription() {
      return bar.getDescription();
    }

    @Override
    public User getOwner() {
      return owner;
    }

    @Override
    public Set<Ingredient> getIngredients() {
      return bar.getIngredients();
    }
  }
}
