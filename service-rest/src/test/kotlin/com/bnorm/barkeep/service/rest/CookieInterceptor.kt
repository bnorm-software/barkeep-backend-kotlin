// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class CookieInterceptor(private val baseUrl: HttpUrl) : Interceptor {

  private var cookie: Cookie? = null

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val builder = chain.request().newBuilder()
    cookie?.apply { builder.addHeader("Cookie", toString()) }

    val response = chain.proceed(builder.build())

    val clearCookie = response.header("Clear-Cookie")
    clearCookie?.apply { cookie = null }

    val setCookie = response.header("Set-Cookie")
    setCookie?.apply { cookie = Cookie.parse(baseUrl, this) }

    return response
  }
}
