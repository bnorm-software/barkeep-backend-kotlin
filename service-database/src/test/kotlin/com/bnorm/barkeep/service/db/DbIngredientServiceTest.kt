// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.value.IngredientSpecValue
import com.bnorm.barkeep.model.value.UserSpecValue
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

class DbIngredientServiceTest : AbstractDbServiceTest() {

  @After
  @Throws(IOException::class)
  fun cleanup() {
    val ingredients = service.getIngredients()
    for (ingredient in ingredients) {
      service.deleteIngredient(ingredient.id)
    }
  }

  // ======================= //
  // ***** POST ingredient ***** //
  // ======================= //

  @Test
  @Throws(Exception::class)
  fun createIngredient_successful() {
    // given
    val ingredient = IngredientSpecValue(title = "Ingredient1")

    // when
    val response = service.createIngredient(ingredient)

    // then
    assert {
      that(response.id).isAtLeast(0)
      that(response.title).isEqualTo("Ingredient1")
    }
  }


  // ====================== //
  // ***** GET ingredient ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun getIngredient_successful() {
    // given
    val ingredient = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))

    // when
    val response = service.getIngredient(ingredient.id)

    // then
    assert {
      that(response.id).isEqualTo(ingredient.id)
      that(response.title).isEqualTo(ingredient.title)
    }
  }

  @Test
  @Throws(Exception::class)
  fun getIngredient_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.getIngredient(-1)
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find ingredient with id=-1")
      }
    }
  }


  // ========================= //
  // ***** UPDATE ingredient ***** //
  // ========================= //

  @Test
  @Throws(Exception::class)
  fun setIngredient_successful_withValidBodyId() {
    // given
    val ingredient = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))

    // when
    val response = service.setIngredient(ingredient.id, IngredientSpecValue(title = "Ingredient2"))

    // then
    assert {
      that(response.id).isEqualTo(ingredient.id)
      that(response.title).isEqualTo("Ingredient2")
    }
  }

  @Test
  @Throws(Exception::class)
  fun setIngredient_successful_withInvalidBodyId() {
    // given
    val ingredient = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))

    // when
    val response = service.setIngredient(ingredient.id, IngredientSpecValue(title = "Ingredient2"))

    // then
    assert {
      that(response.id).isEqualTo(ingredient.id)
      that(response.title).isEqualTo("Ingredient2")
    }
  }

  @Test
  @Throws(Exception::class)
  fun setIngredient_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.setIngredient(-1, IngredientSpecValue(title = "Ingredient1"))
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find ingredient with id=-1")
      }
    }
  }


  // ========================= //
  // ***** DELETE ingredient ***** //
  // ========================= //

  @Test
  @Throws(Exception::class)
  fun deleteIngredient_successful() {
    // given
    val ingredient = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))

    // when
    service.deleteIngredient(ingredient.id)

    // then
  }

  @Test
  @Throws(Exception::class)
  fun deleteIngredient_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.deleteIngredient(-1)
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find ingredient with id=-1")
      }
    }
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun getIngredients_successful_empty() {
    // given

    // when
    val response = service.getIngredients()

    // then
    assert {
      that(response).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getIngredients_successful_emptyAfterDelete() {
    // given
    val ingredient1 = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))
    service.deleteIngredient(ingredient1.id)

    // when
    val response = service.getIngredients()

    // then
    assert {
      that(response).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getIngredients_successful_single() {
    // given
    val ingredient1 = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))

    // when
    val response = service.getIngredients()

    // then
    assert {
      all(that(response)) {
        hasSize(1)
        containsExactly(ingredient1)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getIngredients_successful_singleAfterDelete() {
    // given
    val ingredient1 = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))
    val ingredient2 = service.createIngredient(IngredientSpecValue(title = "Ingredient2"))
    val ingredient3 = service.createIngredient(IngredientSpecValue(title = "Ingredient3"))
    service.deleteIngredient(ingredient1.id)
    service.deleteIngredient(ingredient3.id)

    // when
    val response = service.getIngredients()

    // then
    assert {
      all(that(response)) {
        hasSize(1)
        containsExactly(ingredient2)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getIngredients_successful_multiple() {
    // given
    val ingredient1 = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))
    val ingredient3 = service.createIngredient(IngredientSpecValue(title = "Ingredient3"))
    val ingredient2 = service.createIngredient(IngredientSpecValue(title = "Ingredient2"))

    // when
    val response = service.getIngredients()

    // then
    assert {
      all(that(response)) {
        hasSize(3)
        containsExactly(ingredient1, ingredient3, ingredient2)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getIngredients_successful_multipleAfterDelete() {
    // given
    val ingredient5 = service.createIngredient(IngredientSpecValue(title = "Ingredient5"))
    val ingredient2 = service.createIngredient(IngredientSpecValue(title = "Ingredient2"))
    val ingredient3 = service.createIngredient(IngredientSpecValue(title = "Ingredient3"))
    val ingredient4 = service.createIngredient(IngredientSpecValue(title = "Ingredient4"))
    val ingredient1 = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))
    service.deleteIngredient(ingredient4.id)
    service.deleteIngredient(ingredient2.id)

    // when
    val response = service.getIngredients()

    // then
    assert {
      all(that(response)) {
        hasSize(3)
        containsExactly(ingredient5, ingredient3, ingredient1)
      }
    }
  }

  companion object {

    private lateinit var service: DbIngredientService
    private lateinit var joeTestmore: User

    @BeforeClass
    @JvmStatic
    fun setupIngredientService() {
      val userService = DbUserService(emPool)
      service = DbIngredientService(emPool)

      joeTestmore = userService.createUser(UserSpecValue(username = "joe",
                                                         password = "testmore",
                                                         email = "joe@test.more"))
    }
  }
}
