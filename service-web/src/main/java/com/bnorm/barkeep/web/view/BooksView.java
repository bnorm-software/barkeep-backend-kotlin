// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web.view;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.web.BarkeepService;
import com.bnorm.barkeep.web.event.DashboardEvent;
import com.bnorm.barkeep.web.event.DashboardEventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import retrofit2.Response;

public final class BooksView extends VerticalLayout implements View {

  //  private final Table table;
  private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
  private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
  private static final String[] DEFAULT_COLLAPSIBLE = {};

  public BooksView() {
    setSizeFull();
    addStyleName("transactions");
    DashboardEventBus.register(this);

    addComponent(buildToolbar());

    //    table = buildTable();
    VerticalLayout list = buildList();

    addComponent(list);
    setExpandRatio(list, 1);
  }

  @Override
  public void detach() {
    super.detach();
    // A new instance of BooksView is created every time it's
    // navigated to so we'll need to clean up references to it on detach.
    DashboardEventBus.unregister(this);
  }

  private Component buildToolbar() {
    HorizontalLayout header = new HorizontalLayout();
    header.addStyleName("viewheader");
    header.setSpacing(true);
    Responsive.makeResponsive(header);

    Label title = new Label("Books");
    title.setSizeUndefined();
    title.addStyleName(ValoTheme.LABEL_H1);
    title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
    header.addComponent(title);

    HorizontalLayout tools = new HorizontalLayout(buildFilter());
    tools.setSpacing(true);
    tools.addStyleName("toolbar");
    header.addComponent(tools);

    return header;
  }

  private Component buildFilter() {
    TextField filter = new TextField();
    filter.addTextChangeListener(new TextChangeListener() {
      @Override
      public void textChange(final TextChangeEvent event) {
        //        Filterable data = (Filterable) table.getContainerDataSource();
        //        data.removeAllContainerFilters();
        //        data.addContainerFilter(new Filter() {
        //          @Override
        //          public boolean passesFilter(final Object itemId, final Item item) {
        //
        //            String text = event.getText();
        //            if (text == null || text.equals("")) {
        //              return true;
        //            }
        //
        //            return filterByProperty("title", item, text) //
        //                    || filterByProperty("owner", item, text);
        //
        //          }
        //
        //          @Override
        //          public boolean appliesToProperty(final Object propertyId) {
        //            return propertyId.equals("title") //
        //                    || propertyId.equals("owner");
        //          }
        //        });
      }
    });

    filter.setInputPrompt("Filter");
    filter.setIcon(FontAwesome.SEARCH);
    filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
      @Override
      public void handleAction(final Object sender, final Object target) {
        filter.setValue("");
        //        ((Filterable) table.getContainerDataSource()).removeAllContainerFilters();
      }
    });
    return filter;
  }

  private VerticalLayout buildList() {
    VerticalLayout bookList = new VerticalLayout();

    try {
      BarkeepService service = VaadinSession.getCurrent().getAttribute(BarkeepService.class);
      Response<List<Book>> response = service.getBooks().execute();
      if (response.isSuccessful()) {
        for (Book book : response.body()) {
          bookList.addComponent(buildBookLayout(book));
          bookList.addComponent(new Label("<hr/>", ContentMode.HTML));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return bookList;
  }

  private GridLayout buildBookLayout(Book book) {
    Label title = new Label(book.getTitle());
    title.addStyleName(ValoTheme.LABEL_H3);
    title.addStyleName(ValoTheme.LABEL_LIGHT);

    Label owner = new Label();
    owner.addStyleName(ValoTheme.LABEL_H4);
    owner.addStyleName(ValoTheme.LABEL_COLORED);

    if (book.getOwner() != null) {
      owner.setValue(book.getOwner().getDisplayName());
    } else {
      owner.setValue("N/A");
    }

    GridLayout grid = new GridLayout(2, 2);
    grid.setMargin(new MarginInfo(false, true));
    grid.addComponent(title, 0, 0, 1, 0);
    grid.addComponent(owner, 1, 1);

    return grid;
  }

  @Subscribe
  public void browserResized(final DashboardEvent.BrowserResizeEvent event) {
  }

  private boolean filterByProperty(final String prop, final Item item, final String text) {
    if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
      return false;
    }
    String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
    if (val.contains(text.toLowerCase().trim())) {
      return true;
    }
    return false;
  }

  void createNewReportFromSelection() {
    //    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REPORTS.getViewName());
    //    DashboardEventBus.post(new DashboardEvent.BookReportEvent((Collection<Book>) table.getValue()));
  }

  @Override
  public void enter(final ViewChangeEvent event) {
  }


  private static final Action report = new Action("Create Report");
  private static final Action discard = new Action("Discard");
  private static final Action details = new Action("Movie details");


  private class BooksActionHandler implements Handler {

    @Override
    public void handleAction(final Action action, final Object sender, final Object target) {
      if (action == report) {
        createNewReportFromSelection();
      } else if (action == discard) {
        Notification.show("Not implemented in this demo");
      } else if (action == details) {
        Item item = ((Table) sender).getItem(target);
        if (item != null) {
          Long movieId = (Long) item.getItemProperty("id").getValue();
          //                    MovieDetailsWindow.open(DashboardUI.getDataProvider()
          //                            .getMovie(movieId), null, null);
        }
      }
    }

    @Override
    public Action[] getActions(final Object target, final Object sender) {
      return new Action[] {details, report, discard};
    }
  }


  public static class BookBean {

    private Long id;
    private String title;
    private String owner;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getOwner() {
      return owner;
    }

    public void setOwner(String owner) {
      this.owner = owner;
    }
  }
}
