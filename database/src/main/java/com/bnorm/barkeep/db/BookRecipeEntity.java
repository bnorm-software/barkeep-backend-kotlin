package com.bnorm.barkeep.db;

import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Recipe;

import javax.annotation.Nullable;
import javax.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "lkpBooksRecipes")
@AssociationOverrides({@AssociationOverride(name = "key.book", joinColumns = @JoinColumn(name = "book")),
                       @AssociationOverride(name = "key.recipe", joinColumns = @JoinColumn(name = "recipe"))})
public class BookRecipeEntity implements Recipe {

  @EmbeddedId
  private BookRecipeEntityKey key;

  @Column(name = "number", nullable = false)
  private long number;

  public BookRecipeEntityKey getKey() {
    return key;
  }

  public void setKey(BookRecipeEntityKey id) {
    this.key = id;
  }

  public long getNumber() {
    return number;
  }

  public void setNumber(long number) {
    this.number = number;
  }

  @Override
  public long getId() {
    return key.getRecipe().getId();
  }

  @Override
  public String getTitle() {
    return key.getRecipe().getTitle();
  }

  @Nullable
  @Override
  public String getDescription() {
    return key.getRecipe().getDescription();
  }

  @Nullable
  @Override
  public String getImageUrl() {
    return key.getRecipe().getImageUrl();
  }

  @Nullable
  @Override
  public String getInstructions() {
    return key.getRecipe().getInstructions();
  }

  @Nullable
  @Override
  public String getSource() {
    return key.getRecipe().getSource();
  }

  @Override
  public Instant getCreateTime() {
    return key.getRecipe().getCreateTime();
  }

  @Override
  public Instant getModifyTime() {
    return key.getRecipe().getModifyTime();
  }

  @Override
  public List<Component> getComponents() {
    return key.getRecipe().getComponents();
  }
}
