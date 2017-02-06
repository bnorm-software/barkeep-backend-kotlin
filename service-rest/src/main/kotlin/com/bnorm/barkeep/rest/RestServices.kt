// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
internal class BadRequest(s: String) : RuntimeException(s)

@ResponseStatus(value = HttpStatus.NOT_FOUND)
internal class NotFound(s: String) : RuntimeException(s)

@ResponseStatus(value = HttpStatus.FORBIDDEN)
internal class Forbidden(s: String) : RuntimeException(s)


internal val TYPE_BAR = "bar"
internal val TYPE_BOOK = "book"
internal val TYPE_INGREDIENT = "ingredient"
internal val TYPE_RECIPE = "recipe"

internal fun MSG_NOT_FOUND(type: String, id: Long) = "Unable to find $type with id=$id"

internal fun MSG_DO_NOT_OWN(type: String, id: Long) = "Do not own $type with id=$id"

internal fun MSG_CREATE_WRONG_USER(type: String) = "Cannot create $type owned by another user"
