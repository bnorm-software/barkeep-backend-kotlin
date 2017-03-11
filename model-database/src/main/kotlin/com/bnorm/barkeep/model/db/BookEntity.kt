// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.db

import com.bnorm.barkeep.model.Book
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
import javax.persistence.OrderBy
import javax.persistence.PreRemove
import javax.persistence.Table

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

  @Column(name = "createTime", updatable = false)
  private val createTime: LocalDateTime? = null

  @Column(name = "modifyTime", updatable = false)
  private val modifyTime: LocalDateTime? = null

  @ManyToMany(mappedBy = "books")
  @OrderBy
  private val users: MutableSet<UserEntity> = TreeSet()

  @ManyToMany
  @JoinTable(name = "lkpBooksRecipes",
             joinColumns = arrayOf(JoinColumn(name = "book")),
             inverseJoinColumns = arrayOf(JoinColumn(name = "recipe")))
  @OrderBy
  override val recipes: MutableSet<RecipeEntity> = TreeSet()

  @PreRemove
  fun onRemove() {
    users.forEach { it.removeBook(this) }
    users.clear()

    recipes.forEach { it.removeBook(this) }
    recipes.clear()
  }

  fun addUser(userEntity: UserEntity) {
    users.add(userEntity)
  }

  fun addRecipe(recipe: RecipeEntity) {
    recipes.add(recipe)
  }

  fun removeRecipe(recipe: RecipeEntity) {
    recipes.remove(recipe)
  }
}
