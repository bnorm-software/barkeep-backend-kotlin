// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import javax.persistence.EntityManager

internal fun EntityManager.txn(action: () -> Unit) {
  transaction.begin()
  try {
    action.invoke()
    transaction.commit()
  } catch (t: Throwable) {
    transaction.rollback()
    throw t
  }
}
