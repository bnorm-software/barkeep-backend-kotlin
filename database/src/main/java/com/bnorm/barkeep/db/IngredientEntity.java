package com.bnorm.barkeep.db;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bnorm.barkeep.model.Ingredient;

@Entity
@Table(name = "tblIngredients")
@NamedQueries({@NamedQuery(name = "IngredientEntity.findAll", query = "SELECT i FROM IngredientEntity i")})
public class IngredientEntity implements Ingredient {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @Column(name = "title", nullable = false)
  private String title;

  @ManyToOne
  @JoinColumn(name = "parent", referencedColumnName = "id")
  private IngredientEntity parent;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createTime", updatable = false)
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modifyTime", updatable = false)
  private Date modifyTime;

  public IngredientEntity() {
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public Ingredient getParent() {
    return parent;
  }

  public void setParent(IngredientEntity parent) {
    this.parent = parent;
  }

  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }
}
