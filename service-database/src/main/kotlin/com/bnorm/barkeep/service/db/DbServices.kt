// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import javax.persistence.EntityManager

internal class Rollback : RuntimeException()

internal inline fun EntityManager.txn(action: () -> Unit) {
  transaction.begin()
  try {
    action.invoke()
    transaction.commit()
  } catch (t: Rollback) {
    transaction.rollback()
  } catch (t: Throwable) {
    transaction.rollback()
    throw t
  }
}
