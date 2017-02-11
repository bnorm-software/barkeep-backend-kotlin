// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model.db

import com.bnorm.barkeep.model.Recipe
import org.hibernate.annotations.SortNatural
import java.time.LocalDateTime
import java.util.TreeSet
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.PreRemove
import javax.persistence.Table

@Entity
@Table(name = "tblRecipes")
@NamedQueries(NamedQuery(name = "RecipeEntity.findAll", query = "SELECT r FROM RecipeEntity r"))
class RecipeEntity : Recipe {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  override var id: Long = 0

  @Column(name = "title", nullable = false)
  override var title: String? = null

  @Column(name = "description")
  override var description: String? = null

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
  override var owner: UserEntity? = null

  @Column(name = "imageUrl")
  override var imageUrl: String? = null

  @Column(name = "instructions")
  override var instructions: String? = null

  @Column(name = "source")
  override var source: String? = null

  @Column(name = "createTime", updatable = false)
  private val createTime: LocalDateTime? = null

  @Column(name = "modifyTime", updatable = false)
  private val modifyTime: LocalDateTime? = null

  @ElementCollection
  @CollectionTable(name = "tblRecipeComponents", joinColumns = arrayOf(JoinColumn(name = "recipe")))
  @SortNatural
  override val components: MutableSet<ComponentEntity> = TreeSet()

  @ManyToMany(mappedBy = "recipes")
  @SortNatural
  private val users: MutableSet<UserEntity> = TreeSet()

  @ManyToMany(mappedBy = "recipes")
  @SortNatural
  private val books: MutableSet<BookEntity> = TreeSet()

  @PreRemove
  fun onRemove() {
    users.forEach { it.removeRecipe(this) }
    users.clear()
    books.forEach { it.removeRecipe(this) }
    books.clear()
  }

  fun addUser(userEntity: UserEntity) {
    users.add(userEntity)
  }

  fun addComponent(componentEntity: ComponentEntity) {
    components.add(componentEntity)
  }

  fun removeBook(bookEntity: BookEntity) {
    books.remove(bookEntity)
  }
}
