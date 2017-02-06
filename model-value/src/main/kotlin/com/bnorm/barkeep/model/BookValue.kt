// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class BookSpecValue(override val title: String?,
                         override val description: String? = null,
                         override val owner: User? = null) : BookSpec

data class BookValue(override val id: Long = -1,
                     override val title: String?,
                     override val description: String? = null,
                     override val owner: User? = null,
                     override val recipes: Set<Recipe> = emptySet()) : Book

object BookValueAdapter {
  @ToJson fun toJson(book: BookSpec): BookSpecValue {
    return BookSpecValue(book.title, book.description, book.owner)
  }

  @FromJson fun fromJson(json: BookSpecValue): BookSpec {
    return json
  }

  @ToJson fun toJson(book: Book): BookValue {
    return BookValue(book.id, book.title, book.description, book.owner, book.recipes)
  }

  @FromJson fun fromJson(json: BookValue): Book {
    return json
  }
}
