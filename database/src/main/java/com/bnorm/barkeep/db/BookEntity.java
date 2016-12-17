package com.bnorm.barkeep.db;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;

@Entity
@Table(name = "tblBooks")
//@NamedQueries({@NamedQuery(name = "BookEntity.findByUser", //
//                           query = "SELECT b FROM BookEntity b " //
//                                   + "INNER JOIN lkpUsersBooks fk ON fk.book = b.id " //
//                                   + "INNER JOIN UserEntity u ON u.id = fk.user " //
//                                   + "where u.id = :id")})
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

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "lkpUsersBooks",
             joinColumns = @JoinColumn(name = "book"),
             inverseJoinColumns = @JoinColumn(name = "user"))
  private List<UserEntity> users;

  @OneToMany(mappedBy = "key.book", cascade = CascadeType.ALL)
  @MapKey(name = "number")
  private Map<Long, BookRecipeEntity> bookRecipes = new LinkedHashMap<>();

  public BookEntity() {
  }

  @Override
  public long getId() {
    return id;
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

  @Override
  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  @Override
  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }

  public List<UserEntity> getUsers() {
    return users;
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
