package com.bnorm.barkeep.db;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SortNatural;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;

@Entity
@Table(name = "tblBooks")
@NamedQueries({@NamedQuery(name = "BookEntity.findAll", query = "SELECT b FROM BookEntity b")})
public class BookEntity implements Book {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
  private UserEntity owner;

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

  @ManyToMany(mappedBy = "books")
  @SortNatural
  private Set<UserEntity> users = new TreeSet<>();

  @ManyToMany
  @JoinTable(name = "lkpBooksRecipes",
             joinColumns = @JoinColumn(name = "book"),
             inverseJoinColumns = @JoinColumn(name = "recipe"))
  @SortNatural
  private Set<RecipeEntity> recipes = new TreeSet<>();

  @PreRemove
  public void onRemove() {
    for (UserEntity userEntity : users) {
      userEntity.removeBook(this);
    }
  }

  public BookEntity() {
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public UserEntity getOwner() {
    return owner;
  }

  public void setOwner(UserEntity owner) {
    this.owner = owner;
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

  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }

  @Override
  public Set<Recipe> getRecipes() {
    return Collections.unmodifiableSet(recipes);
  }

  public void addRecipe(RecipeEntity recipe) {
    recipes.add(recipe);
  }
}
