// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.db

import com.bnorm.barkeep.model.Ingredient
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table

@Entity
@Table(name = "tblIngredients")
@NamedQueries(NamedQuery(name = "IngredientEntity.findAll", query = "SELECT i FROM IngredientEntity i"))
class IngredientEntity : Ingredient {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  override var id: Long = 0

  @Column(name = "title", nullable = false)
  override var title: String? = null

  @ManyToOne
  @JoinColumn(name = "parent", referencedColumnName = "id")
  override var parent: IngredientEntity? = null

  @Column(name = "createTime", updatable = false)
  private val createTime: LocalDateTime? = null

  @Column(name = "modifyTime", updatable = false)
  private val modifyTime: LocalDateTime? = null
}
