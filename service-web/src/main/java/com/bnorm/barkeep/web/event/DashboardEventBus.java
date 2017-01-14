// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web.event;

import com.google.common.eventbus.EventBus;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
public class DashboardEventBus {

  private static final EventBus eventBus = new EventBus((exception, context) -> {
    exception.printStackTrace();
  });

  public static void post(final Object event) {
    eventBus.post(event);
  }

  public static void register(final Object object) {
    eventBus.register(object);
  }

  public static void unregister(final Object object) {
    eventBus.unregister(object);
  }
}
