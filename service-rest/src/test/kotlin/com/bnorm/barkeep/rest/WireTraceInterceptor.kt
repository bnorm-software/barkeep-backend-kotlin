// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import okhttp3.Interceptor
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import java.time.Duration
import java.time.Instant

internal class WireTraceInterceptor : Interceptor {

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
    val request = chain.request()

    val requestBody = request.body()
    if (requestBody != null) {
      if (requestBody.contentType() != null && requestBody.contentType().charset() != null) {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        val rawBody = buffer.readByteString().utf8()
        System.out.println("Sending [${request.method()} ${request.url()}] with body [$rawBody]")
      } else {
        System.out.println("Sending [${request.method()} ${request.url()}] with non-text body")
      }
    } else {
      System.out.println("Sending [${request.method()} ${request.url()}]")
    }

    val start = Instant.now()
    var response: okhttp3.Response = chain.proceed(request)

    val duration = Duration.between(start, Instant.now())
    val responseBody = response.body()
    if (responseBody != null) {
      if (responseBody.contentType() != null && responseBody.contentType().charset() != null) {
        val rawBody = responseBody.string()
        System.out.printf("Received [%d] response for [%s %s] in %sms with body [%s]%n",
                          response.code(),
                          response.request().method(),
                          response.request().url(),
                          duration.toMillis(),
                          rawBody)

        // Reading the body as a string closes the stream so create a new response body
        response = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), rawBody)).build()
      } else {
        System.out.printf("Received [%d] response for [%s %s] in %sms with non-text body%n",
                          response.code(),
                          response.request().method(),
                          response.request().url(),
                          duration.toMillis())
      }
    } else {
      System.out.printf("Received [%d] response for [%s %s] in %sms%n",
                        response.code(),
                        response.request().method(),
                        response.request().url(),
                        duration.toMillis())
    }

    return response
  }
}
