// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import com.google.common.collect.ImmutableSet
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException
import java.lang.reflect.Type

class ImmutableSetAdapter<T> private constructor(private val elementAdapter: JsonAdapter<T>) : JsonAdapter<ImmutableSet<T>>() {

  @Throws(IOException::class)
  override fun fromJson(reader: JsonReader): ImmutableSet<T> {
    val builder = ImmutableSet.builder<T>()
    reader.beginArray()
    while (reader.hasNext()) {
      builder.add(elementAdapter.fromJson(reader))
    }
    reader.endArray()
    return builder.build()
  }

  @Throws(IOException::class)
  override fun toJson(writer: JsonWriter, value: ImmutableSet<T>) {
    writer.beginArray()
    for (element in value) {
      elementAdapter.toJson(writer, element)
    }
    writer.endArray()
  }

  companion object {

    val FACTORY: Factory = Factory { type, annotations, moshi ->
      val rawType = Types.getRawType(type)
      if (!annotations.isEmpty()) {
        return@Factory null
      }
      if (rawType == ImmutableSet::class.java) {
        return@Factory newImmutableSetAdapter<Any>(type, moshi).nullSafe()
      }
      null
    }

    internal fun <T> newImmutableSetAdapter(type: Type, moshi: Moshi): JsonAdapter<ImmutableSet<T>> {
      val elementType = Types.collectionElementType(type, ImmutableSet::class.java)
      val elementAdapter = moshi.adapter<T>(elementType)
      return ImmutableSetAdapter(elementAdapter)
    }
  }
}