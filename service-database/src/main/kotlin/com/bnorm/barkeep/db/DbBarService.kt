// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.BarSpec
import com.bnorm.barkeep.service.BarService
import javax.persistence.EntityManager

class DbBarService(entityManager: EntityManager) : AbstractDbService(entityManager), BarService {

  override fun getBars(): List<Bar> {
    return super.getBars()
  }

  override fun getBar(id: Long): BarEntity? {
    return super.getBar(id)
  }

  override fun createBar(bar: BarSpec): BarEntity {
    if (bar.owner == null) {
      throw IllegalArgumentException("Cannot create bar without an owner")
    }
    val userEntity = find(bar.owner!!) ?: throw IllegalArgumentException(String.format("Cannot create bar with an unknown owner id=%d",
                                                                                       bar.owner!!.id))

    val barEntity = BarEntity()
    barEntity.title = bar.title
    barEntity.description = bar.description
    barEntity.owner = userEntity
    barEntity.addUser(userEntity)

    txn {
      em.persist(barEntity)
      userEntity.addBar(barEntity)
    }
    return barEntity
  }

  override fun setBar(id: Long, bar: BarSpec): BarEntity {
    val barEntity = requireExists(findBar(id), id, "bar")

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
