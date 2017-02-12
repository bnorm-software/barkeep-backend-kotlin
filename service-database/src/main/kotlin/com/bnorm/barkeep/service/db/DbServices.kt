// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import javax.persistence.EntityManager

internal class Rollback : RuntimeException()

internal inline fun Pool<EntityManager>.txn(action: EntityManager.() -> Unit) {
  borrow {
    txn(action)
  }
}

internal inline fun EntityManager.txn(action: EntityManager.() -> Unit) {
  transaction.begin()
  try {
    action.invoke(this)
    transaction.commit()
  } catch (t: Rollback) {
    transaction.rollback()
  } catch (t: Throwable) {
    if (transaction.isActive) {
      transaction.rollback()
    }
    throw t
  }
}
