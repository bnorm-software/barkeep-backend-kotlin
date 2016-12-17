package com.bnorm.barkeep.db;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
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

  @Column(name = "displayName", nullable = false)
  private String displayName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createTime", updatable = false)
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modifyTime", updatable = false)
  private Date modifyTime;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "lkpUsersBooks",
             joinColumns = @JoinColumn(name = "user"),
             inverseJoinColumns = @JoinColumn(name = "book"))
  @MapKey(name = "id")
  private Map<Long, BookEntity> books = new LinkedHashMap<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "lkpUsersBars",
             joinColumns = @JoinColumn(name = "user"),
             inverseJoinColumns = @JoinColumn(name = "bar"))
  @MapKey(name = "id")
  private Map<Long, BarEntity> bars = new LinkedHashMap<>();

  public UserEntity() {
  }

  @Override
  public long getId() {
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

  @Override
  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  @Override
  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }

  public Map<Long, BookEntity> getBookMap() {
    return books;
  }

  public void addBook(BookEntity bookEntity) {
    books.put(bookEntity.getId(), bookEntity);
  }

  public Map<Long, BarEntity> getBarMap() {
    return bars;
  }

  public void addBar(BarEntity barEntity) {
    bars.put(barEntity.getId(), barEntity);
  }

  @Override
  public Collection<Book> getBooks() {
    return Collections.unmodifiableCollection(books.values());
  }

  @Override
  public Collection<Bar> getBars() {
    return Collections.unmodifiableCollection(bars.values());
  }
}
