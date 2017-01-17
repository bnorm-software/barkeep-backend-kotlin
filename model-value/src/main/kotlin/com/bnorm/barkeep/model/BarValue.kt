// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import java.lang.reflect.Type

data class BarSpecValue(override val title: String?,
                        override val description: String? = null,
                        override val owner: User? = null) : BarSpec

data class BarValue(override val id: Long = -1,
                    override val title: String?,
                    override val description: String? = null,
                    override val owner: User? = null,
                    override val ingredients: Set<Ingredient>? = emptySet()) : Bar

object BarValueAdapter {
  @ToJson fun toJson(bar: BarSpec): BarSpecValue {
    return BarSpecValue(bar.title, bar.description, bar.owner)
  }

  @FromJson fun fromJson(json: BarSpecValue): BarSpec {
    return json
  }

  @ToJson fun toJson(bar: Bar): BarValue {
    return BarValue(bar.id, bar.title, bar.description, bar.owner, bar.ingredients)
  }

  @FromJson fun fromJson(json: BarValue): Bar {
    return json
  }
}

//object BarAdapter : JsonAdapter.Factory {
//  override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
//    if (type == Bar::class.java) {
//      val ownerAdapter = moshi.adapter(User::class.java)
//      val ingredientsAdapter: JsonAdapter<Set<Ingredient>> = moshi.adapter(Types.newParameterizedType(Set::class.java,
//                                                                                                      Ingredient::class.java))
//      return object : JsonAdapter<Bar>() {
//        override fun toJson(writer: JsonWriter, value: Bar) {
//          writer.beginObject()
//          value.id.apply { writer.name("id").value(this) }
//          value.title.apply { writer.name("title").value(this) }
//          value.description.apply { writer.name("description").value(this) }
//          value.owner.apply { writer.name("owner").value(ownerAdapter.toJson(this)) }
//          value.ingredients.apply { writer.name("ingredients").value(ingredientsAdapter.toJson(this)) }
//          writer.endArray()
//        }
//
//        override fun fromJson(reader: JsonReader): Bar {
//          reader.beginObject()
//
//          var id: Long? = null
//          var title: String? = null
//          var description: String? = null
//          var owner: User? = null
//          var ingredients: Set<Ingredient>? = null
//
//          while (reader.hasNext()) {
//            val name = reader.nextName()
//            if (reader.peek() == JsonReader.Token.NULL) {
//              reader.skipValue()
//              continue
//            }
//
//            when (name) {
//              "id" -> id = reader.nextLong()
//              "title" -> title = reader.nextString()
//              "description" -> description = reader.nextString()
//              "owner" -> owner = ownerAdapter.fromJson(reader)
//              "ingredients" -> ingredients = ingredientsAdapter.fromJson(reader)
//              else -> reader.skipValue()
//            }
//          }
//          reader.endObject()
//
//          return BarValue(id!!, title, description, owner, ingredients);
//        }
//      }
//    }
//    return null
//  }
//}
