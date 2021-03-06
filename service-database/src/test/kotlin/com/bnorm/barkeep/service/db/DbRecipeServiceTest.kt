// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.value.RecipeSpecValue
import com.bnorm.barkeep.model.value.UserSpecValue
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

class DbRecipeServiceTest : AbstractDbServiceTest() {

  @After
  @Throws(IOException::class)
  fun cleanup() {
    val recipes = service.getRecipes()
    for (recipe in recipes) {
      service.deleteRecipe(recipe.id)
    }
  }

  // ======================= //
  // ***** POST recipe ***** //
  // ======================= //

  @Test
  @Throws(Exception::class)
  fun createRecipe_successful() {
    // given
    val recipe = RecipeSpecValue(title = "Recipe1", description = "Description1", owner = joeTestmore)

    // when
    val response = service.createRecipe(recipe)

    // then
    assert {
      that(response.id).isAtLeast(0)
      that(response.title).isEqualTo("Recipe1")
      that(response.description).isEqualTo("Description1")
    }
  }


  // ====================== //
  // ***** GET recipe ***** //
  // ====================== //

  @Test
  @Throws(Exception::class)
  fun getRecipe_successful() {
    // given
    val recipe = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                      description = "Description1",
                                                      owner = joeTestmore))

    // when
    val response = service.getRecipe(recipe.id)

    // then
    assert {
      that(response.id).isEqualTo(recipe.id)
      that(response.title).isEqualTo(recipe.title)
      that(response.description).isEqualTo(recipe.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun getRecipe_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.getRecipe(-1)
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find recipe with id=-1")
      }
    }
  }


  // ========================= //
  // ***** UPDATE recipe ***** //
  // ========================= //

  @Test
  @Throws(Exception::class)
  fun setRecipe_successful_withValidBodyId() {
    // given
    val recipe = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                      description = "Description1",
                                                      owner = joeTestmore))

    // when
    val response = service.setRecipe(recipe.id, RecipeSpecValue(title = "Recipe2"))

    // then
    assert {
      that(response.id).isEqualTo(recipe.id)
      that(response.title).isEqualTo("Recipe2")
      that(response.description).isEqualTo(recipe.description)
    }
  }

  @Test
  @Throws(Exception::class)
  fun setRecipe_successful_withInvalidBodyId() {
    // given
    val recipe = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                      description = "Description1",
                                                      owner = joeTestmore))

    // when
    val response = service.setRecipe(recipe.id, RecipeSpecValue(title = "Recipe2", description = "Description2"))

    // then
    assert {
      that(response.id).isEqualTo(recipe.id)
      that(response.title).isEqualTo("Recipe2")
      that(response.description).isEqualTo("Description2")
    }
  }

  @Test
  @Throws(Exception::class)
  fun setRecipe_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.setRecipe(-1, RecipeSpecValue(title = "Recipe1", description = "Description1", owner = joeTestmore))
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find recipe with id=-1")
      }
    }
  }


  // ========================= //
  // ***** DELETE recipe ***** //
  // ========================= //

  @Test
  @Throws(Exception::class)
  fun deleteRecipe_successful() {
    // given
    val recipe = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                      description = "Description1",
                                                      owner = joeTestmore))

    // when
    service.deleteRecipe(recipe.id)

    // then
  }

  @Test
  @Throws(Exception::class)
  fun deleteRecipe_failure_badId() {
    // given

    // when
    val e: IllegalArgumentException = assertFailsWith {
      service.deleteRecipe(-1)
    }

    // then
    assert {
      all(that(e)) {
        isInstanceOf(IllegalArgumentException::class.java)
        hasMessage("Cannot find recipe with id=-1")
      }
    }
  }


  // ==================== //
  // ***** GET list ***** //
  // ==================== //

  @Test
  @Throws(Exception::class)
  fun getRecipes_successful_empty() {
    // given

    // when
    val response = service.getRecipes()

    // then
    assert {
      that(response).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getRecipes_successful_emptyAfterDelete() {
    // given
    val recipe1 = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                       description = "Description1",
                                                       owner = joeTestmore))
    service.deleteRecipe(recipe1.id)

    // when
    val response = service.getRecipes()

    // then
    assert {
      that(response).isEmpty()
    }
  }

  @Test
  @Throws(Exception::class)
  fun getRecipes_successful_single() {
    // given
    val recipe1 = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                       description = "Description1",
                                                       owner = joeTestmore))

    // when
    val response = service.getRecipes()

    // then
    assert {
      all(that(response)) {
        hasSize(1)
        containsExactly(recipe1)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getRecipes_successful_singleAfterDelete() {
    // given
    val recipe1 = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                       description = "Description1",
                                                       owner = joeTestmore))
    val recipe2 = service.createRecipe(RecipeSpecValue(title = "Recipe2",
                                                       description = "Description2",
                                                       owner = joeTestmore))
    val recipe3 = service.createRecipe(RecipeSpecValue(title = "Recipe3",
                                                       description = "Description3",
                                                       owner = joeTestmore))
    service.deleteRecipe(recipe1.id)
    service.deleteRecipe(recipe3.id)

    // when
    val response = service.getRecipes()

    // then
    assert {
      all(that(response)) {
        hasSize(1)
        containsExactly(recipe2)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getRecipes_successful_multiple() {
    // given
    val recipe1 = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                       description = "Description1",
                                                       owner = joeTestmore))
    val recipe3 = service.createRecipe(RecipeSpecValue(title = "Recipe3",
                                                       description = "Description3",
                                                       owner = joeTestmore))
    val recipe2 = service.createRecipe(RecipeSpecValue(title = "Recipe2",
                                                       description = "Description2",
                                                       owner = joeTestmore))

    // when
    val response = service.getRecipes()

    // then
    assert {
      all(that(response)) {
        hasSize(3)
        containsExactly(recipe1, recipe3, recipe2)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun getRecipes_successful_multipleAfterDelete() {
    // given
    val recipe5 = service.createRecipe(RecipeSpecValue(title = "Recipe5",
                                                       description = "Description5",
                                                       owner = joeTestmore))
    val recipe2 = service.createRecipe(RecipeSpecValue(title = "Recipe2",
                                                       description = "Description2",
                                                       owner = joeTestmore))
    val recipe3 = service.createRecipe(RecipeSpecValue(title = "Recipe3",
                                                       description = "Description3",
                                                       owner = joeTestmore))
    val recipe4 = service.createRecipe(RecipeSpecValue(title = "Recipe4",
                                                       description = "Description4",
                                                       owner = joeTestmore))
    val recipe1 = service.createRecipe(RecipeSpecValue(title = "Recipe1",
                                                       description = "Description1",
                                                       owner = joeTestmore))
    service.deleteRecipe(recipe4.id)
    service.deleteRecipe(recipe2.id)

    // when
    val response = service.getRecipes()

    // then
    assert {
      all(that(response)) {
        hasSize(3)
        containsExactly(recipe5, recipe3, recipe1)
      }
    }
  }

  companion object {

    private lateinit var service: DbRecipeService
    private lateinit var joeTestmore: User

    @BeforeClass
    @JvmStatic
    fun setupRecipeService() {
      val userService = DbUserService(emPool)
      service = DbRecipeService(emPool, DbIngredientService(emPool), userService)

      joeTestmore = userService.createUser(UserSpecValue(username = "joe",
                                                         password = "testmore",
                                                         email = "joe@test.more"))
    }
  }
}
