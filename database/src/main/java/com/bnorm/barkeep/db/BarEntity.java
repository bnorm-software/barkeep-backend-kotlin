package com.bnorm.barkeep.db;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

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

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Ingredient;

@Entity
@Table(name = "tblBars")
@NamedQueries({@NamedQuery(name = "BarEntity.findAll", query = "SELECT b FROM BarEntity b")})
public class BarEntity implements Bar {

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

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createTime", updatable = false)
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modifyTime", updatable = false)
  private Date modifyTime;

  @ManyToMany(mappedBy = "bars")
  @SortNatural
  private Set<UserEntity> users = new TreeSet<>();

  @ManyToMany
  @JoinTable(name = "lkpBarsIngredients",
             joinColumns = @JoinColumn(name = "bar"),
             inverseJoinColumns = @JoinColumn(name = "ingredient"))
  @SortNatural
  private Set<IngredientEntity> ingredients = new TreeSet<>();

  @PreRemove
  public void onRemove() {
    for (UserEntity userEntity : users) {
      userEntity.removeBar(this);
    }
  }

  public BarEntity() {
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

  public void setDescription(String description) {
    this.description = description;
  }

  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }

  public void addUser(UserEntity userEntity) {
    users.add(userEntity);
  }

  @Override
  public Set<Ingredient> getIngredients() {
    return Collections.unmodifiableSet(ingredients);
  }

  public void setIngredients(Set<IngredientEntity> ingredients) {
    this.ingredients = ingredients;
  }

  public void addIngredient(IngredientEntity ingredient) {
    ingredients.add(ingredient);
  }
}
