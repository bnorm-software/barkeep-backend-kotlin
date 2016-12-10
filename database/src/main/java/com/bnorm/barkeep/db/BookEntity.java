package com.bnorm.barkeep.db;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;

import javax.persistence.*;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "tblBooks")
public class BookEntity implements Book {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  private UserEntity user;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "active", nullable = false)
  private boolean active;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createTime", updatable = false)
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modifyTime", updatable = false)
  private Date modifyTime;

  @OneToMany(mappedBy = "key.book", cascade = CascadeType.ALL)
  @MapKey(name = "number")
  private Map<Long, BookRecipeEntity> bookRecipes = new LinkedHashMap<>();

  public BookEntity() {
  }

  @Override
  public long getId() {
    return id;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  @Override
  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }

  @Override
  public Map<Long, Recipe> getRecipes() {
    return Collections.unmodifiableMap(bookRecipes);
  }

  public void addRecipe(BookRecipeEntity recipe) {
    recipe.setNumber(bookRecipes.size() + 1);
    bookRecipes.put(recipe.getNumber(), recipe);
  }
}
