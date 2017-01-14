// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web.view;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.web.BarkeepService;
import com.bnorm.barkeep.web.event.DashboardEvent;
import com.bnorm.barkeep.web.event.DashboardEventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public final class BarsView extends VerticalLayout implements View {

  private final Table table;
  private Button createReport;
  private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
  private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
  private static final String[] DEFAULT_COLLAPSIBLE = {};

  public BarsView() {
    setSizeFull();
    addStyleName("transactions");
    DashboardEventBus.register(this);

    addComponent(buildToolbar());

    table = buildTable();
    addComponent(table);
    setExpandRatio(table, 1);
  }

  @Override
  public void detach() {
    super.detach();
    // A new instance of BarsView is created every time it's
    // navigated to so we'll need to clean up references to it on detach.
    DashboardEventBus.unregister(this);
  }

  private Component buildToolbar() {
    HorizontalLayout header = new HorizontalLayout();
    header.addStyleName("viewheader");
    header.setSpacing(true);
    Responsive.makeResponsive(header);

    Label title = new Label("Bars");
    title.setSizeUndefined();
    title.addStyleName(ValoTheme.LABEL_H1);
    title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
    header.addComponent(title);

    createReport = buildCreateReport();
    HorizontalLayout tools = new HorizontalLayout(buildFilter(), createReport);
    tools.setSpacing(true);
    tools.addStyleName("toolbar");
    header.addComponent(tools);

    return header;
  }

  private Button buildCreateReport() {
    final Button createReport = new Button("Create Report");
    createReport.setDescription("Create a new report from the selected transactions");
    createReport.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        createNewReportFromSelection();
      }
    });
    createReport.setEnabled(false);
    return createReport;
  }

  private Component buildFilter() {
    final TextField filter = new TextField();
    filter.addTextChangeListener(new TextChangeListener() {
      @Override
      public void textChange(final TextChangeEvent event) {
        Filterable data = (Filterable) table.getContainerDataSource();
        data.removeAllContainerFilters();
        data.addContainerFilter(new Filter() {
          @Override
          public boolean passesFilter(final Object itemId,
                                      final Item item) {

            String text = event.getText();
            if (text == null
                    || text.equals("")) {
              return true;
            }

            return filterByProperty("title", item, text)
                    || filterByProperty("owner", item, text);

          }

          @Override
          public boolean appliesToProperty(final Object propertyId) {
            return propertyId.equals("title")
                    || propertyId.equals("owner");
          }
        });
      }
    });

    filter.setInputPrompt("Filter");
    filter.setIcon(FontAwesome.SEARCH);
    filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
      @Override
      public void handleAction(final Object sender, final Object target) {
        filter.setValue("");
        ((Filterable) table.getContainerDataSource()).removeAllContainerFilters();
      }
    });
    return filter;
  }

  private Table buildTable() {
    final Table table = new Table() {
      @Override
      protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
        String result = super.formatPropertyValue(rowId, colId, property);
        if (colId.equals("time")) {
          result = DATEFORMAT.format(((Date) property.getValue()));
        } else if (colId.equals("price")) {
          if (property != null && property.getValue() != null) {
            return "$" + DECIMALFORMAT.format(property.getValue());
          } else {
            return "";
          }
        }
        return result;
      }
    };
    table.setSizeFull();
    table.addStyleName(ValoTheme.TABLE_BORDERLESS);
    table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    table.addStyleName(ValoTheme.TABLE_COMPACT);
    table.setSelectable(true);

    table.setColumnCollapsingAllowed(true);
    table.setColumnCollapsible("title", false);
    //    table.setColumnCollapsible("price", false);

    table.setColumnReorderingAllowed(true);
    BeanContainer<Long, BarBean> newDataSource = new BeanContainer<>(BarBean.class);
    newDataSource.setBeanIdProperty("id");
    try {
      BarkeepService service = VaadinSession.getCurrent().getAttribute(BarkeepService.class);
      List<Bar> bars = service.getBars().execute().body();
      for (Bar bar : bars) {
        BarBean bean = new BarBean();
        bean.setId(bar.getId());
        bean.setTitle(bar.getTitle());
        User owner = bar.getOwner();
        if (owner != null) {
          bean.setOwner(owner.getDisplayName());
        } else {
          bean.setOwner("N/A");
        }
        newDataSource.addBean(bean);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    table.setContainerDataSource(newDataSource);
    table.setSortContainerPropertyId("title");
    table.setSortAscending(false);

    //    table.setColumnAlignment("seats", Align.RIGHT);
    //    table.setColumnAlignment("price", Align.RIGHT);

    table.setVisibleColumns("title", "owner");
    table.setColumnHeaders("Title", "Owner");

    //    table.setFooterVisible(true);
    //    table.setColumnFooter("time", "Total");
    //
    //    table.setColumnFooter("price", "$" + DECIMALFORMAT.format(0));

    // Allow dragging items to the reports menu
    table.setDragMode(TableDragMode.MULTIROW);
    table.setMultiSelect(true);

    table.addActionHandler(new BarsActionHandler());

    table.addValueChangeListener(new ValueChangeListener() {
      @Override
      public void valueChange(final ValueChangeEvent event) {
        if (table.getValue() instanceof Set) {
          Set<?> val = (Set<?>) table.getValue();
          createReport.setEnabled(val.size() > 0);
        }
      }
    });
    table.setImmediate(true);

    return table;
  }

  private boolean defaultColumnsVisible() {
    boolean result = true;
    for (String propertyId : DEFAULT_COLLAPSIBLE) {
      if (table.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
        result = false;
      }
    }
    return result;
  }

  @Subscribe
  public void browserResized(final DashboardEvent.BrowserResizeEvent event) {
    // Some columns are collapsed when browser window width gets small
    // enough to make the table fit better.
    if (defaultColumnsVisible()) {
      for (String propertyId : DEFAULT_COLLAPSIBLE) {
        table.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
      }
    }
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
    //    DashboardEventBus.post(new DashboardEvent.BarReportEvent((Collection<Bar>) table.getValue()));
  }

  @Override
  public void enter(final ViewChangeEvent event) {
  }


  private static final Action report = new Action("Create Report");
  private static final Action discard = new Action("Discard");
  private static final Action details = new Action("Movie details");


  private class BarsActionHandler implements Handler {

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


  public static class BarBean {

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
