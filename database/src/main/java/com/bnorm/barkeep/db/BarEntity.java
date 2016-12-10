package com.bnorm.barkeep.db;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Ingredient;

import javax.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tblBars")
public class BarEntity implements Bar {

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

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createTime", updatable = false)
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modifyTime", updatable = false)
  private Date modifyTime;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "lkpBarsIngredients",
             joinColumns = @JoinColumn(name = "bar"),
             inverseJoinColumns = @JoinColumn(name = "ingredient"))
  private List<IngredientEntity> ingredients = new ArrayList<>();

  public BarEntity() {
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
  public List<Ingredient> getIngredients() {
    return Collections.unmodifiableList(ingredients);
  }

  public void setIngredients(List<IngredientEntity> ingredients) {
    this.ingredients = ingredients;
  }

}
