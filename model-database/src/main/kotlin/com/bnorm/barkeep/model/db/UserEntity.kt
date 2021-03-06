// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.db

import com.bnorm.barkeep.model.User
import java.time.LocalDateTime
import java.util.TreeSet
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.Table

@Entity
@Table(name = "tblUsers")
@NamedQueries(NamedQuery(name = "UserEntity.findByUsername",
                         query = "SELECT u FROM UserEntity u where u.username = :username"),
              NamedQuery(name = "UserEntity.findByEmail", query = "SELECT u FROM UserEntity u where u.email = :email"))
class UserEntity : User {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  override var id: Long = 0

  @Column(name = "username", nullable = false)
  override var username: String? = null

  @Column(name = "password", nullable = false)
  override var password: String? = null

  @Column(name = "displayName")
  override var displayName: String? = null

  @Column(name = "email", unique = true, nullable = false)
  override var email: String? = null

  @Column(name = "createTime", updatable = false)
  private val createTime: LocalDateTime? = null

  @Column(name = "modifyTime", updatable = false)
  private val modifyTime: LocalDateTime? = null

  @OneToMany(mappedBy = "owner", orphanRemoval = true)
  @OrderBy
  private val ownedBooks: MutableSet<BookEntity> = TreeSet()

  @ManyToMany
  @JoinTable(name = "lkpUsersBooks",
             joinColumns = arrayOf(JoinColumn(name = "user")),
             inverseJoinColumns = arrayOf(JoinColumn(name = "book")))
  @OrderBy
  override val books: MutableSet<BookEntity> = TreeSet()

  @OneToMany(mappedBy = "owner", orphanRemoval = true)
  @OrderBy
  private val ownedBars: MutableSet<BarEntity> = TreeSet()

  @ManyToMany
  @JoinTable(name = "lkpUsersBars",
             joinColumns = arrayOf(JoinColumn(name = "user")),
             inverseJoinColumns = arrayOf(JoinColumn(name = "bar")))
  @OrderBy
  override val bars: MutableSet<BarEntity> = TreeSet()

  @OneToMany(mappedBy = "owner", orphanRemoval = true)
  @OrderBy
  private val ownedRecipes: MutableSet<RecipeEntity> = TreeSet()

  @OneToMany
  @JoinTable(name = "lkpUsersRecipes",
             joinColumns = arrayOf(JoinColumn(name = "user")),
             inverseJoinColumns = arrayOf(JoinColumn(name = "recipe")))
  @OrderBy
  override val recipes: MutableSet<RecipeEntity> = TreeSet()

  fun addBar(barEntity: BarEntity) {
    bars.add(barEntity)
  }

  fun removeBar(barEntity: BarEntity) {
    bars.remove(barEntity)
    ownedBars.remove(barEntity)
  }

  fun addBook(bookEntity: BookEntity) {
    books.add(bookEntity)
  }

  fun removeBook(bookEntity: BookEntity) {
    books.remove(bookEntity)
    ownedBooks.remove(bookEntity)
  }

  fun addRecipe(recipeEntity: RecipeEntity) {
    recipes.add(recipeEntity)
  }

  fun removeRecipe(recipeEntity: RecipeEntity) {
    recipes.remove(recipeEntity)
    ownedRecipes.remove(recipeEntity)
  }
}
