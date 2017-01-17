// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class IngredientSpecValue(override val title: String?,
                               override val parent: Ingredient? = null) : IngredientSpec

data class IngredientValue(override val id: Long = -1,
                           override val title: String?,
                           override val parent: Ingredient? = null) : Ingredient

object IngredientValueAdapter {
  @ToJson fun toJson(ingredient: IngredientSpec): IngredientSpecValue {
    return IngredientSpecValue(ingredient.title, ingredient.parent)
  }

  @FromJson fun fromJson(json: IngredientSpecValue): IngredientSpec {
    return json
  }

  @ToJson fun toJson(ingredient: Ingredient): IngredientValue {
    return IngredientValue(ingredient.id, ingredient.title, ingredient.parent)
  }

  @FromJson fun fromJson(json: IngredientValue): Ingredient {
    return json
  }
}
