package com.bnorm.barkeep.db;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class BookRecipeEntityKey implements Serializable {

  @ManyToOne
  @JoinColumn(name = "book")
  private BookEntity book;

  @ManyToOne
  @JoinColumn(name = "recipe")
  private RecipeEntity recipe;

  public BookEntity getBook() {
    return book;
  }

  public void setBook(BookEntity book) {
    this.book = book;
  }

  public RecipeEntity getRecipe() {
    return recipe;
  }

  public void setRecipe(RecipeEntity recipe) {
    this.recipe = recipe;
  }
}
