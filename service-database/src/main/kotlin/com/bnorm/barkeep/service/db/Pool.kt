// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

// todo where do i put this?
interface Pool<T> {

  fun take(): T

  fun give(type: T)
}

inline fun <T> Pool<T>.borrow(action: T.() -> Unit) {
  val t = take()
  try {
    action(t)
  } finally {
    give(t)
  }
}
