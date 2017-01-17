// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class RecipeSpecValue(override val title: String?,
                           override val description: String? = null,
                           override val owner: User? = null,
                           override val imageUrl: String? = null,
                           override val instructions: String? = null,
                           override val source: String? = null,
                           override val components: Set<Component>? = emptySet()) : RecipeSpec

data class RecipeValue(override val id: Long = -1,
                       override val title: String?,
                       override val description: String? = null,
                       override val owner: User? = null,
                       override val imageUrl: String? = null,
                       override val instructions: String? = null,
                       override val source: String? = null,
                       override val components: Set<Component>? = emptySet()) : Recipe

object RecipeValueAdapter {
  @ToJson fun toJson(recipe: RecipeSpec): RecipeSpecValue {
    return RecipeSpecValue(recipe.title,
                           recipe.description,
                           recipe.owner,
                           recipe.imageUrl,
                           recipe.instructions,
                           recipe.source,
                           recipe.components)
  }

  @FromJson fun fromJson(json: RecipeSpecValue): RecipeSpec {
    return json
  }

  @ToJson fun toJson(recipe: Recipe): RecipeValue {
    return RecipeValue(recipe.id,
                       recipe.title,
                       recipe.description,
                       recipe.owner,
                       recipe.imageUrl,
                       recipe.instructions,
                       recipe.source,
                       recipe.components)
  }

  @FromJson fun fromJson(json: RecipeValue): Recipe {
    return json
  }
}
