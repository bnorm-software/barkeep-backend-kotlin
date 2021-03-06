// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.BarSpec
import com.bnorm.barkeep.model.db.BarEntity
import com.bnorm.barkeep.service.BarService
import javax.persistence.EntityManager

class DbBarService(private val emPool: Pool<EntityManager>,
                   private val ingredientService: DbIngredientService,
                   private val userService: DbUserService) : BarService {

  override fun getBars(): List<Bar> {
    emPool.borrow {
      return createNamedQuery("BarEntity.findAll", BarEntity::class.java).resultList
    }
    return emptyList() // never used
  }

  fun findBar(id: Long): BarEntity? {
    emPool.borrow {
      return find(BarEntity::class.java, id)
    }
    return null // never used
  }

  override fun getBar(id: Long): BarEntity {
    return findBar(id) ?: throw IllegalArgumentException("Cannot find bar with id=$id")
  }

  override fun createBar(bar: BarSpec): BarEntity {
    val owner = bar.owner ?: throw IllegalArgumentException("Cannot create bar without an owner")
    val userEntity = userService.findUser(owner.id) ?: throw IllegalArgumentException("Cannot create bar with an unknown owner id=${owner.id}")

    val barEntity = BarEntity()
    barEntity.title = bar.title
    barEntity.description = bar.description
    barEntity.owner = userEntity
    barEntity.addUser(userEntity)

    emPool.txn {
      persist(barEntity)
      userEntity.addBar(barEntity)
    }
    return barEntity

  }

  override fun addBarIngredient(barId: Long, ingredientId: Long) {
    val barEntity = getBar(barId)
    val ingredientEntity = ingredientService.getIngredient(ingredientId)

    emPool.txn {
      barEntity.addIngredient(ingredientEntity)
    }
  }

  override fun removeBarIngredient(barId: Long, ingredientId: Long) {
    val barEntity = getBar(barId)
    val ingredientEntity = ingredientService.getIngredient(ingredientId)

    emPool.txn {
      barEntity.removeIngredient(ingredientEntity)
    }
  }

  override fun setBar(id: Long, bar: BarSpec): BarEntity {
    val barEntity = getBar(id)

    emPool.txn {
      // todo what if we want to clear a value?
      bar.title?.apply { barEntity.title = this }
      bar.description?.apply { barEntity.description = this }
      bar.owner?.apply { barEntity.owner = userService.getUser(this.id) }
    }
    return barEntity
  }

  override fun deleteBar(id: Long) {
    emPool.txn {
      remove(getBar(id))
    }
  }
}
