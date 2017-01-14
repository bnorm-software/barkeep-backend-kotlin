// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Book
import org.hibernate.annotations.SortNatural
import java.time.Instant
import java.util.Date
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
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(name = "tblBooks")
@NamedQueries(NamedQuery(name = "BookEntity.findAll", query = "SELECT b FROM BookEntity b"))
class BookEntity : Book {

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

  @Column(name = "active", nullable = false)
  var isActive: Boolean = false

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createTime", updatable = false)
  private val createTime: Date? = null

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modifyTime", updatable = false)
  private val modifyTime: Date? = null

  @ManyToMany(mappedBy = "books")
  @SortNatural
  private val users: MutableSet<UserEntity> = TreeSet()

  @ManyToMany
  @JoinTable(name = "lkpBooksRecipes",
             joinColumns = arrayOf(JoinColumn(name = "book")),
             inverseJoinColumns = arrayOf(JoinColumn(name = "recipe")))
  @SortNatural
  override val recipes: MutableSet<RecipeEntity> = TreeSet()

  @PreRemove
  fun onRemove() {
    for (userEntity in users) {
      userEntity.removeBook(this)
    }
  }

  fun getCreateTime(): Instant {
    return createTime!!.toInstant()
  }

  fun getModifyTime(): Instant {
    return modifyTime!!.toInstant()
  }

  fun addRecipe(recipe: RecipeEntity) {
    recipes.add(recipe)
  }
}
