// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.BarValue;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.model.UserValue;

import static com.bnorm.barkeep.model.BarAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class DbBarServiceTest extends AbstractDbServiceTest {

  private static DbBarService service;
  private static User joeTestmore;

  @BeforeClass
  public static void setupBarService() {
    service = new DbBarService(em);

    DbUserService userService = new DbUserService(em);
    joeTestmore = userService.createUser(UserValue.builder()
                                                  .setUsername("joe")
                                                  .setPassword("testmore")
                                                  .setEmail("joe@test.more")
                                                  .build());
  }

  @After
  public void cleanup() throws IOException {
    Collection<Bar> bars = service.getBars();
    if (bars != null) {
      for (Bar bar : bars) {
        service.deleteBar(bar.getId());
      }
    }
  }

  // ==================== //
  // ***** POST bar ***** //
  // ==================== //

  @Test
  public void createBar_successful() throws Exception {
    // given
    Bar bar = BarValue.builder().setTitle("Bar1").setDescription("Description1").setOwner(joeTestmore).build();

    // when
    BarEntity response = service.createBar(bar);

    // then
    assertThat(response).has(VALID_ID);
    assertThat(response).hasTitle("Bar1");
    assertThat(response).hasDescription("Description1");
  }

  @Test
  public void createBar_failure_badId() throws Exception {
    // given
    Bar bar = BarValue.builder()
                      .setId(1L)
                      .setTitle("Bar1")
                      .setDescription("Description1")
                      .setOwner(joeTestmore)
                      .build();

    try {
      // when
      service.createBar(bar);
      fail("Did not fail as expected");
    } catch (IllegalArgumentException e) {
      // then
      assertThat(e).hasMessageContaining("already has an id");
    }
  }


  // =================== //
  // ***** GET bar ***** //
  // =================== //

  @Test
  public void getBar_successful() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());

    // when
    BarEntity response = service.getBar(Objects.requireNonNull(bar1.getId()));

    // then
    assertThat(response).hasId(bar1.getId());
    assertThat(response).hasTitle("Bar1");
    assertThat(response).hasDescription("Description1");
  }

  @Test
  public void getBar_failure_badId() throws Exception {
    // given

    // when
    BarEntity response = service.getBar(-1);

    // then
    assertThat(response).isNull();
  }


  // ====================== //
  // ***** UPDATE bar ***** //
  // ====================== //

  @Test
  public void setBar_successful_withValidBodyId() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());

    // when
    BarEntity response = service.setBar(Objects.requireNonNull(bar1.getId()),
                                        BarValue.builder().setTitle("Bar2").build());

    // then
    assertThat(response).hasId(bar1.getId());
    assertThat(response).hasTitle("Bar2");
    assertThat(response).hasDescription("Description1");
  }

  @Test
  public void setBar_successful_withInvalidBodyId() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());

    // when
    BarEntity response = service.setBar(Objects.requireNonNull(bar1.getId()),
                                        BarValue.builder().setTitle("Bar2").setDescription("Description2").build());

    // then
    assertThat(response).hasId(bar1.getId());
    assertThat(response).hasTitle("Bar2");
    assertThat(response).hasDescription("Description2");
  }

  @Test
  public void setBar_failure_badId() throws Exception {
    // given

    try {
      // when
      service.setBar(-1,
                     BarValue.builder().setTitle("Bar1").setDescription("Description1").setOwner(joeTestmore).build());
      fail("Did not fail as expected");
    } catch (IllegalArgumentException e) {
      // then
      assertThat(e).hasMessageContaining("Cannot find bar");
    }
  }


  // ====================== //
  // ***** DELETE bar ***** //
  // ====================== //

  @Test
  public void deleteBar_successful() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());

    // when
    service.deleteBar(Objects.requireNonNull(bar1.getId()));

    // then
  }

  @Test
  public void deleteBar_failure_badId() throws Exception {
    // given

    try {
      // when
      service.deleteBar(-1);
      fail("Did not fail as expected");
    } catch (IllegalArgumentException e) {
      // then
      assertThat(e).hasMessageContaining("Cannot find bar");
    }
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  public void getBars_successful_empty() throws Exception {
    // given

    // when
    Collection<Bar> response = service.getBars();

    // then
    assertThat(response).isEmpty();
  }

  @Test
  public void getBars_successful_emptyAfterDelete() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());
    service.deleteBar(Objects.requireNonNull(bar1.getId()));

    // when
    Collection<Bar> response = service.getBars();

    // then
    assertThat(response).isEmpty();
  }

  @Test
  public void getBars_successful_single() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());

    // when
    Collection<Bar> response = service.getBars();

    // then
    assertThat(response).hasSize(1);
    assertThat(response).containsExactly(bar1);
  }

  @Test
  public void getBars_successful_singleAfterDelete() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());
    Bar bar2 = service.createBar(BarValue.builder()
                                         .setTitle("Bar2")
                                         .setDescription("Description2")
                                         .setOwner(joeTestmore)
                                         .build());
    Bar bar3 = service.createBar(BarValue.builder()
                                         .setTitle("Bar3")
                                         .setDescription("Description3")
                                         .setOwner(joeTestmore)
                                         .build());
    service.deleteBar(Objects.requireNonNull(bar1.getId()));
    service.deleteBar(Objects.requireNonNull(bar3.getId()));

    // when
    Collection<Bar> response = service.getBars();

    // then
    assertThat(response).hasSize(1);
    assertThat(response).containsExactly(bar2);
  }

  @Test
  public void getBars_successful_multiple() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());
    Bar bar3 = service.createBar(BarValue.builder()
                                         .setTitle("Bar3")
                                         .setDescription("Description3")
                                         .setOwner(joeTestmore)
                                         .build());
    Bar bar2 = service.createBar(BarValue.builder()
                                         .setTitle("Bar2")
                                         .setDescription("Description2")
                                         .setOwner(joeTestmore)
                                         .build());

    // when
    Collection<Bar> response = service.getBars();

    // then
    assertThat(response).hasSize(3);
    assertThat(response).containsExactly(bar1, bar3, bar2);
  }

  @Test
  public void getBars_successful_multipleAfterDelete() throws Exception {
    // given
    Bar bar5 = service.createBar(BarValue.builder()
                                         .setTitle("Bar5")
                                         .setDescription("Description5")
                                         .setOwner(joeTestmore)
                                         .build());
    Bar bar2 = service.createBar(BarValue.builder()
                                         .setTitle("Bar2")
                                         .setDescription("Description2")
                                         .setOwner(joeTestmore)
                                         .build());
    Bar bar3 = service.createBar(BarValue.builder()
                                         .setTitle("Bar3")
                                         .setDescription("Description3")
                                         .setOwner(joeTestmore)
                                         .build());
    Bar bar4 = service.createBar(BarValue.builder()
                                         .setTitle("Bar4")
                                         .setDescription("Description4")
                                         .setOwner(joeTestmore)
                                         .build());
    Bar bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build());
    service.deleteBar(Objects.requireNonNull(bar4.getId()));
    service.deleteBar(Objects.requireNonNull(bar2.getId()));

    // when
    Collection<Bar> response = service.getBars();

    // then
    assertThat(response).hasSize(3);
    assertThat(response).containsExactly(bar5, bar3, bar1);
  }
}
