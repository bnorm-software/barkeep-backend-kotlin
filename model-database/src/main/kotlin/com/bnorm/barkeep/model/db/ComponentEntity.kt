// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.db

import com.bnorm.barkeep.model.Component

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Embeddable
class ComponentEntity : Component {

  @ManyToOne
  @JoinColumn(name = "ingredient", referencedColumnName = "id", nullable = false)
  override lateinit var ingredient: IngredientEntity

  @Column(name = "min", nullable = false)
  override var min: Double = 0.toDouble()

  @Column(name = "max")
  override var max: Double? = null

  @Column(name = "componentNum", nullable = false)
  override var componentNum: Long = 0

  @Column(name = "`order`", nullable = false)
  override var order: Long = 0
}
