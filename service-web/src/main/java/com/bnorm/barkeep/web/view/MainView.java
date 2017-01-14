// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web.view;

import com.bnorm.barkeep.web.nav.DashboardMenu;
import com.bnorm.barkeep.web.nav.DashboardNavigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class MainView extends HorizontalLayout {

  public MainView() {
    setSizeFull();
    addStyleName("mainview");

    addComponent(new DashboardMenu());

    ComponentContainer content = new CssLayout();
    content.addStyleName("view-content");
    content.setSizeFull();
    addComponent(content);
    setExpandRatio(content, 1.0f);

    new DashboardNavigator(content);
  }
}
