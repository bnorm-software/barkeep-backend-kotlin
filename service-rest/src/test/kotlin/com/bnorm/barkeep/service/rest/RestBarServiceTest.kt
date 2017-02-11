// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.value.BarSpecValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import java.io.IOException

class RestBarServiceTest : AbstractRestServiceTest() {

  @After
  @Throws(IOException::class)
  fun cleanup() {
    val bars = service.getBars().execute().body()
    if (bars != null) {
      for (bar in bars) {
        service.deleteBar(bar.id).execute()
      }
    }
  }

  // ==================== //
  // ***** POST bar ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun createBar_successful() {
    // given
    val bar = BarSpecValue(title = "Bar1", description = "Description1")

    // when
    val response = service.createBar(bar).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    response.body().id should beValid
    response.body().title shouldBe bar.title
    response.body().description shouldBe bar.description
  }


  // =================== //
  // ***** GET bar ***** //
  // =================== //

  @Test
  @Throws(Exception::class)
  fun getBar_successful() {
    // given
    val bar = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()

    // when
    val response = service.getBar(bar.id).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    response.body().id shouldBe bar.id
    response.body().title shouldBe bar.title
    response.body().description shouldBe bar.description
  }

  @Test
  @Throws(Exception::class)
  fun getBar_failure_badId() {
    // given

    // when
    val response = service.getBar(-1).execute()

    // then
    assertThat(response.isSuccessful()).isFalse()
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
  }


  // ====================== //
  // ***** UPDATE bar ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun updateBar_successful_withValidBodyId() {
    // given
    val bar = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()

    // when
    val response = service.updateBar(bar.id, BarSpecValue(title = "Bar2")).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    response.body().id shouldBe bar.id
    response.body().title shouldBe "Bar2"
    response.body().description shouldBe bar.description
  }

  @Test
  @Throws(Exception::class)
  fun updateBar_successful_withInvalidBodyId() {
    // given
    val bar = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()

    // when
    val response = service.updateBar(bar.id, BarSpecValue(title = "Bar2", description = "Description2")).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    response.body().id shouldBe bar.id
    response.body().title shouldBe "Bar2"
    response.body().description shouldBe "Description2"
  }

  @Test
  @Throws(Exception::class)
  fun updateBar_failure_badId() {
    // given

    // when
    val response = service.updateBar(-1, BarSpecValue(title = "Bar1", description = "Description1")).execute()

    // then
    assertThat(response.isSuccessful()).isFalse()
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
  }


  // ====================== //
  // ***** DELETE bar ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun deleteBar_successful() {
    // given
    val bar = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()

    // when
    val response = service.deleteBar(bar.id).execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
  }

  @Test
  @Throws(Exception::class)
  fun deleteBar_failure_badId() {
    // given

    // when
    val response = service.deleteBar(-1).execute()

    // then
    assertThat(response.isSuccessful()).isFalse()
    assertThat(response.raw().code()).isEqualTo(CODE_NOT_FOUND)
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun getBars_successful_empty() {
    // given

    // when
    val response = service.getBars().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Bar>(response.body()).isEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_emptyAfterDelete() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()
    service.deleteBar(bar1.id).execute()

    // when
    val response = service.getBars().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Bar>(response.body()).isEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_single() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()

    // when
    val response = service.getBars().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Bar>(response.body()).hasSize(1)
    assertThat<Bar>(response.body()).containsExactly(bar1)
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_singleAfterDelete() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()
    val bar2 = service.createBar(BarSpecValue(title = "Bar2", description = "Description2")).execute().body()
    val bar3 = service.createBar(BarSpecValue(title = "Bar3", description = "Description3")).execute().body()
    service.deleteBar(bar1.id).execute()
    service.deleteBar(bar3.id).execute()

    // when
    val response = service.getBars().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Bar>(response.body()).hasSize(1)
    assertThat<Bar>(response.body()).containsExactly(bar2)
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_multiple() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()
    val bar3 = service.createBar(BarSpecValue(title = "Bar3", description = "Description3")).execute().body()
    val bar2 = service.createBar(BarSpecValue(title = "Bar2", description = "Description2")).execute().body()

    // when
    val response = service.getBars().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Bar>(response.body()).hasSize(3)
    assertThat<Bar>(response.body()).containsExactly(bar1, bar3, bar2)
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_multipleAfterDelete() {
    // given
    val bar5 = service.createBar(BarSpecValue(title = "Bar5", description = "Description5")).execute().body()
    val bar3 = service.createBar(BarSpecValue(title = "Bar3", description = "Description3")).execute().body()
    val bar2 = service.createBar(BarSpecValue(title = "Bar2", description = "Description2")).execute().body()
    val bar4 = service.createBar(BarSpecValue(title = "Bar4", description = "Description4")).execute().body()
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1")).execute().body()
    service.deleteBar(bar4.id).execute()
    service.deleteBar(bar2.id).execute()

    // when
    val response = service.getBars().execute()

    // then
    assertThat(response.isSuccessful()).isTrue()
    assertThat(response.raw().code()).isEqualTo(CODE_SUCCESS)
    assertThat<Bar>(response.body()).hasSize(3)
    assertThat<Bar>(response.body()).containsExactly(bar5, bar3, bar1)
  }
}
