// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bnorm.barkeep.db.DbRecipeService;
import com.bnorm.barkeep.db.DbUserService;
import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.service.RecipeService;
import com.bnorm.barkeep.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/recipes")
public class RestRecipeService extends AbstractRestService implements RecipeService {

  private final UserService userService;
  private final RecipeService recipeService;

  @Autowired
  public RestRecipeService(DbUserService userService, DbRecipeService recipeService) {
    this.userService = userService;
    this.recipeService = recipeService;
  }

  private Recipe findByOwner(long userId, long recipeId) {
    Recipe recipe = recipeService.getRecipe(recipeId);
    if (!isOwnedBy(recipe, userId)) {
      throw new NotFound("Unable to find recipe with id=%d", recipeId);
    }
    return recipe;
  }

  private Recipe findByUser(User user, long recipeId) {
    Recipe recipe = recipeService.getRecipe(recipeId);
    if (!user.getRecipes().contains(recipe)) {
      throw new NotFound("Unable to find recipe with id=%d", recipeId);
    }
    return recipe;
  }

  @JsonView(Recipe.class)
  @RequestMapping(method = RequestMethod.GET)
  @Override
  public Collection<Recipe> getRecipes() {
    User user = userService.getUser(currentUser().getId());
    return user.getRecipes();
  }

  @JsonView(Recipe.class)
  @RequestMapping(value = "/{recipeId}", method = RequestMethod.GET)
  @Override
  public Recipe getRecipe(@PathVariable("recipeId") long recipeId) {
    User user = userService.getUser(currentUser().getId());
    return findByUser(user, recipeId);
  }

  @JsonView(Recipe.class)
  @RequestMapping(method = RequestMethod.POST)
  @Override
  public Recipe createRecipe(Recipe recipe) {
    User currentUser = currentUser();
    if (!isOwnedBy(recipe, currentUser.getId())) {
      throw new BadRequest("Cannot create recipe owned by another user");
    }

    if (recipe.getOwner() == null) {
      return recipeService.createRecipe(new RecipeWithOwner(recipe, currentUser));
    } else {
      return recipeService.createRecipe(recipe);
    }
  }

  @JsonView(Recipe.class)
  @RequestMapping(value = "/{recipeId}", method = RequestMethod.PUT)
  @Override
  public Recipe setRecipe(@PathVariable("recipeId") long recipeId, @RequestBody Recipe recipe) {
    findByOwner(currentUser().getId(), recipeId);
    return recipeService.setRecipe(recipeId, recipe);
  }

  @RequestMapping(value = "/{recipeId}", method = RequestMethod.DELETE)
  @Override
  public void deleteRecipe(@PathVariable("recipeId") long recipeId) {
    findByOwner(currentUser().getId(), recipeId);
    recipeService.deleteRecipe(recipeId);
  }

  private static class RecipeWithOwner implements Recipe {

    private final Recipe recipe;
    private final User owner;

    private RecipeWithOwner(Recipe recipe, User owner) {
      this.recipe = recipe;
      this.owner = owner;
    }

    @Override
    public Long getId() {
      return recipe.getId();
    }

    @Nullable
    @Override
    public String getTitle() {
      return recipe.getTitle();
    }

    @Nullable
    @Override
    public String getDescription() {
      return recipe.getDescription();
    }

    @Override
    public User getOwner() {
      return owner;
    }

    @Nullable
    @Override
    public String getImageUrl() {
      return recipe.getImageUrl();
    }

    @Nullable
    @Override
    public String getInstructions() {
      return recipe.getInstructions();
    }

    @Nullable
    @Override
    public String getSource() {
      return recipe.getSource();
    }

    @Override
    public Set<Component> getComponents() {
      return recipe.getComponents();
    }
  }
}
