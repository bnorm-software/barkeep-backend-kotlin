// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.service.BarService
import javax.persistence.EntityManager

class DbBarService(entityManager: EntityManager) : AbstractDbService(entityManager), BarService {

  override fun getBars(): List<Bar> {
    return super.getBars()
  }

  override fun getBar(id: Long): BarEntity? {
    return super.getBar(id)
  }

  override fun createBar(bar: Bar): BarEntity {
    if (bar.id != null) {
      throw IllegalArgumentException("Cannot create bar that already has an id=$bar.id")
    } else if (bar.owner == null) {
      throw IllegalArgumentException("Cannot create bar without an owner")
    }
    val userEntity = find(bar.owner!!) ?: throw IllegalArgumentException(String.format("Cannot create bar with an unknown owner id=%d",
                                                                                       bar.owner!!.id))

    val barEntity = BarEntity()
    barEntity.title = bar.title
    barEntity.description = bar.description
    barEntity.owner = userEntity
    barEntity.addUser(userEntity)
    if (bar.ingredients != null) {
      for (ingredient in bar.ingredients!!) {
        barEntity.addIngredient(find(ingredient)!!)
      }
    }

    txn {
      em.persist(barEntity)
      userEntity.addBar(barEntity)
    }
    return barEntity
  }

  override fun setBar(id: Long, bar: Bar): BarEntity {
    val barEntity = requireExists(findBar(id), id, "bar")
    requireMatch(bar, id, "bar")

    txn {
      if (bar.title != null) {
        barEntity.title = bar.title
      }
      if (bar.description != null) {
        barEntity.description = bar.description
      }
      if (bar.owner != null) {
        barEntity.owner = find(bar.owner!!)
      }
      if (bar.ingredients != null) {
        // todo is this correct?
        for (ingredient in bar.ingredients!!) {
          barEntity.addIngredient(find(ingredient)!!)
        }
      }
    }

    return barEntity
  }

  override fun deleteBar(id: Long) {
    txn {
      val barEntity = findBar(id)
      requireExists(barEntity, id, "bar")
      em.remove(barEntity)
    }
  }
}
