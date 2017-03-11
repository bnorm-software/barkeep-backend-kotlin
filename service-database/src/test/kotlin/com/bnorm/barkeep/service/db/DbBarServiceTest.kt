// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.value.BarSpecValue
import com.bnorm.barkeep.model.value.UserSpecValue
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

class DbBarServiceTest : AbstractDbServiceTest() {

  @After
  @Throws(IOException::class)
  fun cleanup() {
    val bars = service.getBars()
    for (bar in bars) {
      service.deleteBar(bar.id)
    }
  }

  // ==================== //
  // ***** POST bar ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun createBar_successful() {
    // given
    val bar = BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore)

    // when
    val response = service.createBar(bar)

    // then
    assert {
      that(response.id).isAtLeast(0)
      that(response.title).isEqualTo("Bar1")
      that(response.description).isEqualTo("Description1")
    }
  }


  // =================== //
  // ***** GET bar ***** //
  // =================== //

  @Test
  @Throws(Exception::class)
  fun getBar_successful() {
    // given
    val bar = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.getBar(bar.id)

    // then
    assert {
      that(response.id).isEqualTo(bar.id)
      that(response.title).isEqualTo(bar.title)
      that(response.description).isEqualTo(bar.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBar_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.getBar(-1)
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find bar with id=-1")
      }
    }
  }


  // ====================== //
  // ***** UPDATE bar ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun setBar_successful_withValidBodyId() {
    // given
    val bar = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.setBar(bar.id, BarSpecValue(title = "Bar2"))

    // then
    assert {
      that(response.id).isEqualTo(bar.id)
      that(response.title).isEqualTo("Bar2")
      that(response.description).isEqualTo(bar.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun setBar_successful_withInvalidBodyId() {
    // given
    val bar = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.setBar(bar.id, BarSpecValue(title = "Bar2", description = "Description2"))

    // then
    assert {
      that(response.id).isEqualTo(bar.id)
      that(response.title).isEqualTo("Bar2")
      that(response.description).isEqualTo("Description2")
    }
  }

  @Test
  @Throws(Exception::class)
  fun setBar_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.setBar(-1, BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find bar with id=-1")
      }
    }
  }


  // ====================== //
  // ***** DELETE bar ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun deleteBar_successful() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))

    // when
    service.deleteBar(bar1.id)

    // then
  }

  @Test
  @Throws(Exception::class)
  fun deleteBar_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.deleteBar(-1)
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find bar with id=-1")
      }
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
    assert {
      that(response).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_emptyAfterDelete() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))
    service.deleteBar(bar1.id)

    // when
    val response = service.getBars()

    // then
    assert {
      that(response).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_single() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))

    // when
    val response = service.getBars()

    // then
    assert {
      all(that(response)) {
        hasSize(1)
        containsExactly(bar1)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_singleAfterDelete() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))
    val bar2 = service.createBar(BarSpecValue(title = "Bar2", description = "Description2", owner = joeTestmore))
    val bar3 = service.createBar(BarSpecValue(title = "Bar3", description = "Description3", owner = joeTestmore))
    service.deleteBar(bar1.id)
    service.deleteBar(bar3.id)

    // when
    val response = service.getBars()

    // then
    assert {
      all(that(response)) {
        hasSize(1)
        containsExactly(bar2)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_multiple() {
    // given
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))
    val bar3 = service.createBar(BarSpecValue(title = "Bar3", description = "Description3", owner = joeTestmore))
    val bar2 = service.createBar(BarSpecValue(title = "Bar2", description = "Description2", owner = joeTestmore))

    // when
    val response = service.getBars()

    // then
    assert {
      all(that(response)) {
        hasSize(3)
        containsExactly(bar1, bar3, bar2)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getBars_successful_multipleAfterDelete() {
    // given
    val bar5 = service.createBar(BarSpecValue(title = "Bar5", description = "Description5", owner = joeTestmore))
    val bar2 = service.createBar(BarSpecValue(title = "Bar2", description = "Description2", owner = joeTestmore))
    val bar3 = service.createBar(BarSpecValue(title = "Bar3", description = "Description3", owner = joeTestmore))
    val bar4 = service.createBar(BarSpecValue(title = "Bar4", description = "Description4", owner = joeTestmore))
    val bar1 = service.createBar(BarSpecValue(title = "Bar1", description = "Description1", owner = joeTestmore))
    service.deleteBar(bar4.id)
    service.deleteBar(bar2.id)

    // when
    val response = service.getBars()

    // then
    assert {
      all(that(response)) {
        hasSize(3)
        containsExactly(bar5, bar3, bar1)
      }
    }
  }

  companion object {

    private lateinit var service: DbBarService
    private lateinit var joeTestmore: User

    @BeforeClass
    @JvmStatic
    fun setupBarService() {
      val userService = DbUserService(emPool)
      service = DbBarService(emPool, DbIngredientService(emPool), userService)

      joeTestmore = userService.createUser(UserSpecValue(username = "joe",
                                                         password = "testmore",
                                                         email = "joe@test.more"))
    }
  }
}
