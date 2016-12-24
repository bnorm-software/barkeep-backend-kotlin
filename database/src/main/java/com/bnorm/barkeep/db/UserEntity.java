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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SortNatural;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;

@Entity
@Table(name = "tblUsers")
@NamedQueries({@NamedQuery(name = "UserEntity.findByUsername",
                           query = "SELECT u FROM UserEntity u where u.username = :username"),
               @NamedQuery(name = "UserEntity.findByEmail",
                           query = "SELECT u FROM UserEntity u where u.email = :email")})
public class UserEntity implements User {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "displayName")
  private String displayName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createTime", updatable = false)
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modifyTime", updatable = false)
  private Date modifyTime;

  @OneToMany(mappedBy = "owner", orphanRemoval = true)
  @SortNatural
  private Set<BookEntity> ownedBooks = new TreeSet<>();

  @ManyToMany
  @JoinTable(name = "lkpUsersBooks",
             joinColumns = @JoinColumn(name = "user"),
             inverseJoinColumns = @JoinColumn(name = "book"))
  @SortNatural
  private Set<BookEntity> books = new TreeSet<>();

  @OneToMany(mappedBy = "owner", orphanRemoval = true)
  @SortNatural
  private Set<BarEntity> ownedBars = new TreeSet<>();

  @ManyToMany
  @JoinTable(name = "lkpUsersBars",
             joinColumns = @JoinColumn(name = "user"),
             inverseJoinColumns = @JoinColumn(name = "bar"))
  @SortNatural
  private Set<BarEntity> bars = new TreeSet<>();

  @OneToMany(mappedBy = "owner")
  @SortNatural
  private Set<RecipeEntity> recipes = new TreeSet<>();

  public UserEntity() {
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }

  @Override
  public Set<Bar> getBars() {
    return Collections.unmodifiableSet(bars);
  }

  public void addBar(BarEntity barEntity) {
    bars.add(barEntity);
  }

  public void removeBar(BarEntity barEntity) {
    bars.remove(barEntity);
    ownedBars.remove(barEntity);
  }

  @Override
  public Set<Book> getBooks() {
    return Collections.unmodifiableSet(books);
  }

  public void addBook(BookEntity bookEntity) {
    books.add(bookEntity);
  }


  public void removeBook(BookEntity barEntity) {
    books.remove(barEntity);
    ownedBooks.remove(barEntity);
  }

  @Override
  public Set<Recipe> getRecipes() {
    return Collections.unmodifiableSet(recipes);
  }

  public void addRecipe(RecipeEntity recipeEntity) {
    recipes.add(recipeEntity);
  }
}
