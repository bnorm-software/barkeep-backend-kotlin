package com.bnorm.barkeep.db;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.User;

import javax.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tblUsers")
public class UserEntity implements User {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private char[] password;

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

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<BookEntity> books = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<BarEntity> bars = new ArrayList<>();

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
  public char[] getPassword() {
    return password;
  }

  public void setPassword(char[] password) {
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

  @Override
  public List<Book> getBooks() {
    return Collections.unmodifiableList(books);
  }

  @Override
  public List<Bar> getBars() {
    return Collections.unmodifiableList(bars);
  }
}
