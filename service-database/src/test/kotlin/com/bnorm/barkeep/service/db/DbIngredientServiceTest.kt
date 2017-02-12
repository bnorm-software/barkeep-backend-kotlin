// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.value.IngredientSpecValue
import com.bnorm.barkeep.model.value.UserSpecValue
import io.kotlintest.matchers.have
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException

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
    response.id should beValid
    response.title shouldBe "Ingredient1"
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
    response.id shouldBe ingredient.id
    response.title shouldBe ingredient.title
  }

  @Test
  @Throws(Exception::class)
  fun getIngredient_failure_badId() {
    // given

    try {
      // when
      service.getIngredient(-1)
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "Cannot find ingredient"
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
    response.id shouldBe ingredient.id
    response.title shouldBe "Ingredient2"
  }

  @Test
  @Throws(Exception::class)
  fun setIngredient_successful_withInvalidBodyId() {
    // given
    val ingredient = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))

    // when
    val response = service.setIngredient(ingredient.id, IngredientSpecValue(title = "Ingredient2"))

    // then
    response.id shouldBe ingredient.id
    response.title shouldBe "Ingredient2"
  }

  @Test
  @Throws(Exception::class)
  fun setIngredient_failure_badId() {
    // given

    try {
      // when
      service.setIngredient(-1, IngredientSpecValue(title = "Ingredient1"))
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "Cannot find ingredient"
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

    try {
      // when
      service.deleteIngredient(-1)
      fail("Did not fail as expected")
    } catch (e: IllegalArgumentException) {
      // then
      e.message!! should have substring "Cannot find ingredient"
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
    response should haveSize(0)
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
    response should haveSize(0)
  }

  @Test
  @Throws(Exception::class)
  fun getIngredients_successful_single() {
    // given
    val ingredient1 = service.createIngredient(IngredientSpecValue(title = "Ingredient1"))

    // when
    val response = service.getIngredients()

    // then
    response should haveSize(1)
    response shouldBe listOf(ingredient1)
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
    response should haveSize(1)
    response shouldBe listOf(ingredient2)
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
    response should haveSize(3)
    response shouldBe listOf(ingredient1, ingredient3, ingredient2)
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
    response should haveSize(3)
    response shouldBe listOf(ingredient5, ingredient3, ingredient1)
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
