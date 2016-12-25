// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.junit.After;
import org.junit.Test;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.BarValue;

import retrofit2.Response;

import static com.bnorm.barkeep.model.BarAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class RestBarServiceTest extends AbstractRestServiceTest {

  @After
  public void cleanup() throws IOException {
    List<Bar> bars = service.getBars().execute().body();
    if (bars != null) {
      for (Bar bar : bars) {
        service.deleteBar(bar.getId()).execute();
      }
    }
  }

  // ==================== //
  // ***** POST bar ***** //
  // ==================== //

  @Test
  public void createBar_successful() throws Exception {
    // given
    Bar bar = BarValue.builder().setTitle("Bar1").setDescription("Description1").build();

    // when
    Response<Bar> response = service.createBar(bar).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).has(VALID_ID);
    assertThat(response.body()).hasTitle("Bar1");
    assertThat(response.body()).hasDescription("Description1");
  }

  @Test
  public void createBar_failure_badId() throws Exception {
    // given
    Bar bar = BarValue.builder().setId(1L).setTitle("Bar1").setDescription("Description1").build();

    // when
    Response<Bar> response = service.createBar(bar).execute();

    // then
    assertThat(response.isSuccessful()).isFalse();
    assertThat(response.raw().code()).isEqualTo(BAD_REQUEST);
  }


  // =================== //
  // ***** GET bar ***** //
  // =================== //

  @Test
  public void getBar_successful() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();

    // when
    Response<Bar> response = service.getBar(Objects.requireNonNull(bar1.getId())).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasId(bar1.getId());
    assertThat(response.body()).hasTitle("Bar1");
    assertThat(response.body()).hasDescription("Description1");
  }

  @Test
  public void getBar_failure_badId() throws Exception {
    // given

    // when
    Response<Bar> response = service.getBar(-1).execute();

    // then
    assertThat(response.isSuccessful()).isFalse();
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND);
  }


  // ====================== //
  // ***** UPDATE bar ***** //
  // ====================== //

  @Test
  public void updateBar_successful_withValidBodyId() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();

    // when
    Response<Bar> response = service.updateBar(Objects.requireNonNull(bar1.getId()),
                                               BarValue.builder().setTitle("Bar2").build()).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasId(bar1.getId());
    assertThat(response.body()).hasTitle("Bar2");
    assertThat(response.body()).hasDescription("Description1");
  }

  @Test
  public void updateBar_successful_withInvalidBodyId() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();

    // when
    Response<Bar> response = service.updateBar(Objects.requireNonNull(bar1.getId()),
                                               BarValue.builder()
                                                       .setTitle("Bar2")
                                                       .setDescription("Description2")
                                                       .build()).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasId(bar1.getId());
    assertThat(response.body()).hasTitle("Bar2");
    assertThat(response.body()).hasDescription("Description2");
  }

  @Test
  public void updateBar_failure_badId() throws Exception {
    // given

    // when
    Response<Bar> response = service.updateBar(-1,
                                               BarValue.builder()
                                                       .setTitle("Bar1")
                                                       .setDescription("Description1")
                                                       .build()).execute();

    // then
    assertThat(response.isSuccessful()).isFalse();
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND);
  }


  // ====================== //
  // ***** DELETE bar ***** //
  // ====================== //

  @Test
  public void deleteBar_successful() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();

    // when
    Response<Void> response = service.deleteBar(Objects.requireNonNull(bar1.getId())).execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
  }

  @Test
  public void deleteBar_failure_badId() throws Exception {
    // given

    // when
    Response<Void> response = service.deleteBar(-1).execute();

    // then
    assertThat(response.isSuccessful()).isFalse();
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND);
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  public void getBars_successful_empty() throws Exception {
    // given

    // when
    Response<List<Bar>> response = service.getBars().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).isEmpty();
  }

  @Test
  public void getBars_successful_emptyAfterDelete() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();
    service.deleteBar(Objects.requireNonNull(bar1.getId())).execute();

    // when
    Response<List<Bar>> response = service.getBars().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).isEmpty();
  }

  @Test
  public void getBars_successful_single() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();

    // when
    Response<List<Bar>> response = service.getBars().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasSize(1);
    assertThat(response.body()).containsExactly(bar1);
  }

  @Test
  public void getBars_successful_singleAfterDelete() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();
    Bar bar2 = service.createBar(BarValue.builder().setTitle("Bar2").setDescription("Description2").build())
                      .execute()
                      .body();
    Bar bar3 = service.createBar(BarValue.builder().setTitle("Bar3").setDescription("Description3").build())
                      .execute()
                      .body();
    service.deleteBar(Objects.requireNonNull(bar1.getId())).execute();
    service.deleteBar(Objects.requireNonNull(bar3.getId())).execute();

    // when
    Response<List<Bar>> response = service.getBars().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasSize(1);
    assertThat(response.body()).containsExactly(bar2);
  }

  @Test
  public void getBars_successful_multiple() throws Exception {
    // given
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();
    Bar bar3 = service.createBar(BarValue.builder().setTitle("Bar3").setDescription("Description3").build())
                      .execute()
                      .body();
    Bar bar2 = service.createBar(BarValue.builder().setTitle("Bar2").setDescription("Description2").build())
                      .execute()
                      .body();

    // when
    Response<List<Bar>> response = service.getBars().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasSize(3);
    assertThat(response.body()).containsExactly(bar1, bar3, bar2);
  }

  @Test
  public void getBars_successful_multipleAfterDelete() throws Exception {
    // given
    Bar bar5 = service.createBar(BarValue.builder().setTitle("Bar5").setDescription("Description5").build())
                      .execute()
                      .body();
    Bar bar2 = service.createBar(BarValue.builder().setTitle("Bar2").setDescription("Description2").build())
                      .execute()
                      .body();
    Bar bar3 = service.createBar(BarValue.builder().setTitle("Bar3").setDescription("Description3").build())
                      .execute()
                      .body();
    Bar bar4 = service.createBar(BarValue.builder().setTitle("Bar4").setDescription("Description4").build())
                      .execute()
                      .body();
    Bar bar1 = service.createBar(BarValue.builder().setTitle("Bar1").setDescription("Description1").build())
                      .execute()
                      .body();
    service.deleteBar(Objects.requireNonNull(bar4.getId())).execute();
    service.deleteBar(Objects.requireNonNull(bar2.getId())).execute();

    // when
    Response<List<Bar>> response = service.getBars().execute();

    // then
    assertThat(response.isSuccessful()).isTrue();
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS);
    assertThat(response.body()).hasSize(3);
    assertThat(response.body()).containsExactly(bar5, bar3, bar1);
  }
}
