// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web;

import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.BarSpec;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.BookSpec;
import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.IngredientSpec;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.RecipeSpec;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.model.UserSpec;
import com.bnorm.barkeep.model.bean.BarBean;
import com.bnorm.barkeep.model.bean.BookBean;
import com.bnorm.barkeep.model.bean.ComponentBean;
import com.bnorm.barkeep.model.bean.IngredientBean;
import com.bnorm.barkeep.model.bean.RecipeBean;
import com.bnorm.barkeep.model.bean.UserBean;
import com.bnorm.barkeep.web.event.DashboardEvent;
import com.bnorm.barkeep.web.event.DashboardEventBus;
import com.bnorm.barkeep.web.view.LoginView;
import com.bnorm.barkeep.web.view.MainView;
import com.google.common.eventbus.Subscribe;
import com.squareup.moshi.Moshi;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Theme("dashboard")
//@Widgetset("com.vaadin.demo.dashboard.DashboardWidgetSet")
@Title("QuickTickets Dashboard")
@SpringUI
public class RootUi extends UI {

  public static final Logger log = LoggerFactory.getLogger(RootUi.class);

  @Override
  protected void init(VaadinRequest request) {
    setLocale(Locale.US);

    DashboardEventBus.register(this);
    Responsive.makeResponsive(this);
    addStyleName(ValoTheme.UI_WITH_MENU);

    updateContent();

    // Some views need to be aware of browser resize events so a
    // BrowserResizeEvent gets fired to the event bus on every occasion.
    Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {
      @Override
      public void browserWindowResized(final Page.BrowserWindowResizeEvent event) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
      }
    });
  }

  private void updateContent() {
    User user = VaadinSession.getCurrent().getAttribute(User.class);
    if (user != null) {
      // Authenticated user
      setContent(new MainView());
      removeStyleName("loginview");
      getNavigator().navigateTo(getNavigator().getState());
    } else {
      setContent(new LoginView());
      addStyleName("loginview");
    }
  }

  @Subscribe
  public void userLoginRequested(DashboardEvent.UserLoginRequestedEvent event) {
    BarkeepService service = service(event.getAuthorization());
    try {
      // todo replace with login request
      Response<User> response = service.getCurrentUser().execute();
      if (response.isSuccessful()) {
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute(User.class, response.body());
        session.setAttribute(BarkeepService.class, service);
      }
    } catch (IOException e) {
      log.warn("Unable to connect to Barkeep service", e);
    }
    updateContent();
  }

  @Subscribe
  public void userLoggedOut(DashboardEvent.UserLoggedOutEvent event) {
    // When the user logs out, current VaadinSession gets closed and the
    // page gets reloaded on the login screen. Do notice the this doesn't
    // invalidate the current HttpSession.
    VaadinSession session = VaadinSession.getCurrent();
    session.setAttribute(User.class, null);
    session.setAttribute(BarkeepService.class, null);
    session.close();
    Page.getCurrent().reload();

  }

  @Subscribe
  public void closeOpenWindows(DashboardEvent.CloseOpenWindowsEvent event) {
    for (Window window : getWindows()) {
      window.close();
    }
  }

  // todo move somewhere else
  private BarkeepService service(String authorization) {
    HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
    urlBuilder.scheme("http");
    urlBuilder.host("localhost");
    urlBuilder.port(8088);
    urlBuilder.addPathSegment("rest");
    urlBuilder.addPathSegment("api");
    urlBuilder.addPathSegment("v1");
    // This last, empty segment adds a trailing '/' which is required for relative paths in the annotations
    urlBuilder.addPathSegment("");
    HttpUrl url = urlBuilder.build();


    // todo this generates a lot of follow up requests if incorrect...
    Authenticator authenticator = (route, response) -> {
      return response.request().newBuilder().header("Authorization", authorization).build();
    };
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new CookieInterceptor(url))
                                                    // .addInterceptor(new WireTraceInterceptor())
                                                    .authenticator(authenticator).build();
    Moshi moshi = new Moshi.Builder().add((type, annotations, m) -> {
      if (type == Recipe.class || type == RecipeSpec.class) {
        return m.adapter(RecipeBean.class);
      } else if (type == Bar.class || type == BarSpec.class) {
        return m.adapter(BarBean.class);
      } else if (type == Book.class || type == BookSpec.class) {
        return m.adapter(BookBean.class);
      } else if (type == Ingredient.class || type == IngredientSpec.class) {
        return m.adapter(IngredientBean.class);
      } else if (type == Component.class) {
        return m.adapter(ComponentBean.class);
      } else if (type == User.class || type == UserSpec.class) {
        return m.adapter(UserBean.class);
      } else {
        return null;
      }
    }).add(ImmutableSetAdapter.FACTORY).build();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                              .client(client)
                                              .addConverterFactory(MoshiConverterFactory.create(moshi))
                                              .build();

    return retrofit.create(BarkeepService.class);
  }
}
