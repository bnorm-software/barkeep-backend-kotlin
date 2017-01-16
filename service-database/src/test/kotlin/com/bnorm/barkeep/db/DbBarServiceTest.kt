// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import com.bnorm.barkeep.model.BarValue
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.UserValue
import io.kotlintest.matchers.have
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException

class DbBarServiceTest : AbstractDbServiceTest() {

  @After
  @Throws(IOException::class)
  fun cleanup() {
    val bars = service.getBars()
    for (bar in bars) {
      service.deleteBar(bar.id!!)
    }
  }

  // ==================== //
  // ***** POST bar ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun createBar_successful() {
    // given
    val bar = BarValue.builder().setTitle("Bar1").setDescription("Description1").setOwner(joeTestmore).build()

    // when
    val response = service.createBar(bar)

    // then
    response.id should beValid
    response.title shouldBe "Bar1"
    response.description shouldBe "Description1"
  }

  @Test
  @Throws(Exception::class)
  fun createBar_failure_badId() {
    // given
    val bar = BarValue.builder()
            .setId(1L)
            .setTitle("Bar1")
            .setDescription("Description1")
            .setOwner(joeTestmore)
            .build()

    try {
      // when
      service.createBar(bar)
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "already has an id"
    }
  }


  // =================== //
  // ***** GET bar ***** //
  // =================== //

  @Test
  @Throws(Exception::class)
  fun getBar_successful() {
    // given
    val bar = service.createBar(BarValue.builder()
                                        .setTitle("Bar1")
                                        .setDescription("Description1")
                                        .setOwner(joeTestmore)
                                        .build())

    // when
    val response = service.getBar(bar.id)

    // then
    response!!
    response.id shouldBe bar.id
    response.title shouldBe bar.title
    response.description shouldBe bar.description
  }

  @Test
  @Throws(Exception::class)
  fun getBar_failure_badId() {
    // given

    // when
    val response = service.getBar(-1)

    // then
    response shouldBe null
  }


  // ====================== //
  // ***** UPDATE bar ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun setBar_successful_withValidBodyId() {
    // given
    val bar = service.createBar(BarValue.builder()
                                        .setTitle("Bar1")
                                        .setDescription("Description1")
                                        .setOwner(joeTestmore)
                                        .build())

    // when
    val response = service.setBar(bar.id, BarValue.builder().setTitle("Bar2").build())

    // then
    response.id shouldBe bar.id
    response.title shouldBe "Bar2"
    response.description shouldBe bar.description
  }

  @Test
  @Throws(Exception::class)
  fun setBar_successful_withInvalidBodyId() {
    // given
    val bar = service.createBar(BarValue.builder()
                                        .setTitle("Bar1")
                                        .setDescription("Description1")
                                        .setOwner(joeTestmore)
                                        .build())

    // when
    val response = service.setBar(bar.id,
                                  BarValue.builder()
                                          .setTitle("Bar2")
                                          .setDescription("Description2")
                                          .build())

    // then
    response.id shouldBe bar.id
    response.title shouldBe "Bar2"
    response.description shouldBe "Description2"
  }

  @Test
  @Throws(Exception::class)
  fun setBar_failure_badId() {
    // given

    try {
      // when
      service.setBar(-1,
                     BarValue.builder()
                             .setTitle("Bar1")
                             .setDescription("Description1")
                             .setOwner(joeTestmore)
                             .build())
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "Cannot find bar"
    }
  }


  // ====================== //
  // ***** DELETE bar ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun deleteBar_successful() {
    // given
    val bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build())

    // when
    service.deleteBar(bar1.id)

    // then
  }

  @Test
  @Throws(Exception::class)
  fun deleteBar_failure_badId() {
    // given

    try {
      // when
      service.deleteBar(-1)
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "Cannot find bar"
    }
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun getBars_successful_empty() {
    // given

    // when
    val response = service.getBars()

    // then
    response should beEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_emptyAfterDelete() {
    // given
    val bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build())
    service.deleteBar(bar1.id)

    // when
    val response = service.getBars()

    // then
    response should beEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_single() {
    // given
    val bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build())

    // when
    val response = service.getBars()

    // then
    response should haveSize(1)
    response shouldBe listOf(bar1)
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_singleAfterDelete() {
    // given
    val bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build())
    val bar2 = service.createBar(BarValue.builder()
                                         .setTitle("Bar2")
                                         .setDescription("Description2")
                                         .setOwner(joeTestmore)
                                         .build())
    val bar3 = service.createBar(BarValue.builder()
                                         .setTitle("Bar3")
                                         .setDescription("Description3")
                                         .setOwner(joeTestmore)
                                         .build())
    service.deleteBar(bar1.id)
    service.deleteBar(bar3.id)

    // when
    val response = service.getBars()

    // then
    response should haveSize(1)
    response shouldBe listOf(bar2)
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_multiple() {
    // given
    val bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build())
    val bar3 = service.createBar(BarValue.builder()
                                         .setTitle("Bar3")
                                         .setDescription("Description3")
                                         .setOwner(joeTestmore)
                                         .build())
    val bar2 = service.createBar(BarValue.builder()
                                         .setTitle("Bar2")
                                         .setDescription("Description2")
                                         .setOwner(joeTestmore)
                                         .build())

    // when
    val response = service.getBars()

    // then
    response should haveSize(3)
    response shouldBe listOf(bar1, bar3, bar2)
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_multipleAfterDelete() {
    // given
    val bar5 = service.createBar(BarValue.builder()
                                         .setTitle("Bar5")
                                         .setDescription("Description5")
                                         .setOwner(joeTestmore)
                                         .build())
    val bar2 = service.createBar(BarValue.builder()
                                         .setTitle("Bar2")
                                         .setDescription("Description2")
                                         .setOwner(joeTestmore)
                                         .build())
    val bar3 = service.createBar(BarValue.builder()
                                         .setTitle("Bar3")
                                         .setDescription("Description3")
                                         .setOwner(joeTestmore)
                                         .build())
    val bar4 = service.createBar(BarValue.builder()
                                         .setTitle("Bar4")
                                         .setDescription("Description4")
                                         .setOwner(joeTestmore)
                                         .build())
    val bar1 = service.createBar(BarValue.builder()
                                         .setTitle("Bar1")
                                         .setDescription("Description1")
                                         .setOwner(joeTestmore)
                                         .build())
    service.deleteBar(bar4.id)
    service.deleteBar(bar2.id)

    // when
    val response = service.getBars()

    // then
    response should haveSize(3)
    response shouldBe listOf(bar5, bar3, bar1)
  }

  companion object {

    private lateinit var service: DbBarService
    private lateinit var joeTestmore: User

    @BeforeClass
    @JvmStatic
    fun setupBarService() {
      service = DbBarService(em)

      val userService = DbUserService(em)
      joeTestmore = userService.createUser(UserValue.builder()
                                                   .setUsername("joe")
                                                   .setPassword("testmore")
                                                   .setEmail("joe@test.more")
                                                   .build())
    }
  }
}
