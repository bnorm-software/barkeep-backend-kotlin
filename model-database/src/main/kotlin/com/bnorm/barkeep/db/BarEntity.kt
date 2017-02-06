// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Bar
import org.hibernate.annotations.SortNatural
import java.time.LocalDateTime
import java.util.TreeSet
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.PreRemove
import javax.persistence.Table

@Entity
@Table(name = "tblBars")
@NamedQueries(NamedQuery(name = "BarEntity.findAll", query = "SELECT b FROM BarEntity b"))
class BarEntity : Bar {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  override var id: Long = 0

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
  override var owner: UserEntity? = null

  @Column(name = "title", nullable = false)
  override var title: String? = null

  @Column(name = "description")
  override var description: String? = null

  @Column(name = "createTime", updatable = false)
  private val createTime: LocalDateTime? = null

  @Column(name = "modifyTime", updatable = false)
  private val modifyTime: LocalDateTime? = null

  @ManyToMany(mappedBy = "bars")
  @SortNatural
  private val users: MutableSet<UserEntity> = TreeSet()

  @ManyToMany
  @JoinTable(name = "lkpBarsIngredients",
             joinColumns = arrayOf(JoinColumn(name = "bar")),
             inverseJoinColumns = arrayOf(JoinColumn(name = "ingredient")))
  @SortNatural
  override var ingredients: MutableSet<IngredientEntity> = TreeSet()

  @PreRemove
  fun onRemove() {
    for (userEntity in users) {
      userEntity.removeBar(this)
    }
  }

  fun addUser(userEntity: UserEntity) {
    users.add(userEntity)
  }

  fun addIngredient(ingredient: IngredientEntity) {
    ingredients.add(ingredient)
  }

  fun removeIngredient(ingredient: IngredientEntity) {
    ingredients.remove(ingredient)
  }
}
