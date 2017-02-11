// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.BarSpec
import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.BookSpec
import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.IngredientSpec
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.RecipeSpec

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BarkeepService {

  // ================= //
  // ***** Books ***** //
  // ================= //

  @GET("books")
  fun getBooks(): Call<List<Book>>

  @POST("books")
  fun createBook(@Body book: BookSpec): Call<Book>

  @GET("books/{id}")
  fun getBook(@Path("id") id: Long): Call<Book>

  @GET("books/{id}/recipes")
  fun getBookRecipes(@Path("id") id: Long): Call<List<Recipe>>

  @PUT("books/{id}")
  fun updateBook(@Path("id") id: Long, @Body book: BookSpec): Call<Book>

  @DELETE("books/{id}")
  fun deleteBook(@Path("id") id: Long): Call<Void>


  // ================= //
  // ***** Bars ***** //
  // ================= //

  @GET("bars")
  fun getBars(): Call<List<Bar>>

  @POST("bars")
  fun createBar(@Body bar: BarSpec): Call<Bar>

  @GET("bars/{id}")
  fun getBar(@Path("id") id: Long): Call<Bar>

  @PUT("bars/{id}")
  fun updateBar(@Path("id") id: Long, @Body bar: BarSpec): Call<Bar>

  @DELETE("bars/{id}")
  fun deleteBar(@Path("id") id: Long): Call<Void>


  // =================== //
  // ***** Recipes ***** //
  // =================== //

  @GET("recipes")
  fun getRecipes(): Call<List<Recipe>>

  @POST("recipes")
  fun createRecipe(@Body recipe: RecipeSpec): Call<Recipe>

  @GET("recipes/{id}")
  fun getRecipe(@Path("id") id: Long): Call<Recipe>

  @PUT("recipes/{id}")
  fun updateRecipe(@Path("id") id: Long, @Body recipe: RecipeSpec): Call<Recipe>

  @DELETE("recipes/{id}")
  fun deleteRecipe(@Path("id") id: Long): Call<Void>


  // ======================= //
  // ***** Ingredients ***** //
  // ======================= //

  @GET("ingredients")
  fun getIngredients(@Query("search") search: String): Call<List<Ingredient>>

  @GET("ingredients")
  fun getIngredients(): Call<List<Ingredient>>

  @POST("ingredients")
  fun createIngredient(@Body ingredient: IngredientSpec): Call<Ingredient>

  @GET("ingredients/{id}")
  fun getIngredient(@Path("id") id: Long): Call<Ingredient>

  @PUT("ingredients/{id}")
  fun updateIngredient(@Path("id") id: Long, @Body ingredient: IngredientSpec): Call<Ingredient>

  @DELETE("ingredients/{id}")
  fun deleteIngredient(@Path("id") id: Long): Call<Void>
}
