package com.bnorm.barkeep.db;

import com.bnorm.barkeep.model.Ingredient;

import javax.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "tblIngredients")
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
  public long getId() {
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

  public void setParent(Optional<IngredientEntity> parent) {
    this.parent = parent.orElse(null);
  }

  @Override
  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  @Override
  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }
}
