// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web.event;

import java.util.Collection;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.web.nav.DashboardViewType;

public interface DashboardEvent {

  final class UserLoginRequestedEvent {

    private final String username, authorization;

    public UserLoginRequestedEvent(String username, String authorization) {
      this.username = username;
      this.authorization = authorization;
    }

    public String getUsername() {
      return username;
    }

    public String getAuthorization() {
      return authorization;
    }
  }


  class BrowserResizeEvent {

  }


  class UserLoggedOutEvent {

  }


  class NotificationsCountUpdatedEvent {

    private final int count;

    public NotificationsCountUpdatedEvent(int count) {
      this.count = count;
    }

    public int getCount() {
      return count;
    }
  }


  final class ReportsCountUpdatedEvent {

    private final int count;

    public ReportsCountUpdatedEvent(int count) {
      this.count = count;
    }

    public int getCount() {
      return count;
    }

  }


  final class BookReportEvent {

    private final Collection<Book> books;

    public BookReportEvent(final Collection<Book> books) {
      this.books = books;
    }

    public Collection<Book> getBooks() {
      return books;
    }
  }


  final class PostViewChangeEvent {

    private final DashboardViewType view;

    public PostViewChangeEvent(DashboardViewType view) {
      this.view = view;
    }

    public DashboardViewType getView() {
      return view;
    }
  }


  class CloseOpenWindowsEvent {

  }


  class ProfileUpdatedEvent {

  }
}
