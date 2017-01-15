// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.User
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
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.OneToMany
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
  @SortNatural
  private val ownedBooks: MutableSet<BookEntity> = TreeSet()

  @ManyToMany
  @JoinTable(name = "lkpUsersBooks",
             joinColumns = arrayOf(JoinColumn(name = "user")),
             inverseJoinColumns = arrayOf(JoinColumn(name = "book")))
  @SortNatural
  override val books: MutableSet<BookEntity> = TreeSet()

  @OneToMany(mappedBy = "owner", orphanRemoval = true)
  @SortNatural
  private val ownedBars: MutableSet<BarEntity> = TreeSet()

  @ManyToMany
  @JoinTable(name = "lkpUsersBars",
             joinColumns = arrayOf(JoinColumn(name = "user")),
             inverseJoinColumns = arrayOf(JoinColumn(name = "bar")))
  @SortNatural
  override val bars: MutableSet<BarEntity> = TreeSet()

  @OneToMany(mappedBy = "owner")
  @SortNatural
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

  fun removeBook(barEntity: BookEntity) {
    books.remove(barEntity)
    ownedBooks.remove(barEntity)
  }

  fun addRecipe(recipeEntity: RecipeEntity) {
    recipes.add(recipeEntity)
  }

  fun removeRecipe(recipeEntity: RecipeEntity) {
    recipes.remove(recipeEntity)
  }
}
