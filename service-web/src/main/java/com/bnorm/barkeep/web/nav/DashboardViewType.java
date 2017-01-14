// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web.nav;

import com.bnorm.barkeep.web.view.BarsView;
import com.bnorm.barkeep.web.view.BooksView;
import com.bnorm.barkeep.web.view.DashboardView;
import com.bnorm.barkeep.web.view.RecipesView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum DashboardViewType {
  DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, true),
  BOOKS("books", BooksView.class, FontAwesome.BOOK, false),
  BARS("bars", BarsView.class, FontAwesome.FLASK, false),
  RECIPES("recipes", RecipesView.class, FontAwesome.GLASS, false),
  //  SALES("sales", SalesView.class, FontAwesome.BAR_CHART_O, false),
  //  TRANSACTIONS("transactions", TransactionsView.class, FontAwesome.TABLE, false),
  //  REPORTS("reports", ReportsView.class, FontAwesome.FILE_TEXT_O, true),
  //  SCHEDULE("schedule", ScheduleView.class, FontAwesome.CALENDAR_O, false),

  ;

  private final String viewName;
  private final Class<? extends View> viewClass;
  private final Resource icon;
  private final boolean stateful;

  private DashboardViewType(String viewName,
                            Class<? extends View> viewClass,
                            Resource icon,
                            boolean stateful) {
    this.viewName = viewName;
    this.viewClass = viewClass;
    this.icon = icon;
    this.stateful = stateful;
  }

  public boolean isStateful() {
    return stateful;
  }

  public String getViewName() {
    return viewName;
  }

  public Class<? extends View> getViewClass() {
    return viewClass;
  }

  public Resource getIcon() {
    return icon;
  }

  public static DashboardViewType getByViewName(String viewName) {
    DashboardViewType result = null;
    for (DashboardViewType viewType : values()) {
      if (viewType.getViewName().equals(viewName)) {
        result = viewType;
        break;
      }
    }
    return result;
  }

}