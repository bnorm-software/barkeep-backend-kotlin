// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web.view;

import com.bnorm.barkeep.web.event.DashboardEvent;
import com.bnorm.barkeep.web.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import okhttp3.Credentials;

public class LoginView extends VerticalLayout {

  public LoginView() {
    setSizeFull();

    Component loginForm = buildLoginForm();
    addComponent(loginForm);
    setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
  }

  private Component buildLoginForm() {
    final VerticalLayout loginPanel = new VerticalLayout();
    loginPanel.setSizeUndefined();
    loginPanel.setSpacing(true);
    Responsive.makeResponsive(loginPanel);
    loginPanel.addStyleName("login-panel");

    loginPanel.addComponent(buildLabels());
    loginPanel.addComponent(buildFields());
    loginPanel.addComponent(new CheckBox("Remember me", true));
    return loginPanel;
  }

  private Component buildFields() {
    HorizontalLayout fields = new HorizontalLayout();
    fields.setSpacing(true);
    fields.addStyleName("fields");

    TextField username = new TextField("Username");
    username.setIcon(FontAwesome.USER);
    username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    username.focus();

    PasswordField password = new PasswordField("Password");
    password.setIcon(FontAwesome.LOCK);
    password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

    Button signin = new Button("Sign In");
    signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
    signin.setClickShortcut(KeyCode.ENTER);

    fields.addComponents(username, password, signin);
    fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

    signin.addClickListener(event -> {
      String authorization = Credentials.basic(username.getValue(), password.getValue());
      DashboardEventBus.post(new DashboardEvent.UserLoginRequestedEvent(username.getValue(), authorization));
    });
    return fields;
  }

  private Component buildLabels() {
    CssLayout labels = new CssLayout();
    labels.addStyleName("labels");

    Label welcome = new Label("Welcome");
    welcome.setSizeUndefined();
    welcome.addStyleName(ValoTheme.LABEL_H4);
    welcome.addStyleName(ValoTheme.LABEL_COLORED);
    labels.addComponent(welcome);

    Label title = new Label("QuickTickets Dashboard");
    title.setSizeUndefined();
    title.addStyleName(ValoTheme.LABEL_H3);
    title.addStyleName(ValoTheme.LABEL_LIGHT);
    labels.addComponent(title);
    return labels;
  }

}
