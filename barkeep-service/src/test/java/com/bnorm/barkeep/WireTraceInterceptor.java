// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;

class WireTraceInterceptor implements Interceptor {

  @Override
  public okhttp3.Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    RequestBody requestBody = request.body();
    if (requestBody != null) {
      if (requestBody.contentType() != null && requestBody.contentType().charset() != null) {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        String rawBody = buffer.readByteString().utf8();
        System.out.printf("Sending [%s %s] with body [%s]%n", request.method(), request.url(), rawBody);
      } else {
        System.out.printf("Sending [%s %s] with non-text body%n", request.method(), request.url());
      }
    } else {
      System.out.printf("Sending [%s %s]%n", request.method(), request.url());
    }

    Instant start = Instant.now();
    okhttp3.Response response = chain.proceed(request);

    Duration duration = Duration.between(start, Instant.now());
    ResponseBody responseBody = response.body();
    if (responseBody != null) {
      if (responseBody.contentType() != null && responseBody.contentType().charset() != null) {
        String rawBody = responseBody.string();
        System.out.printf("Received [%d] response for [%s %s] in %sms with body [%s]%n",
                          response.code(),
                          response.request().method(),
                          response.request().url(),
                          duration.toMillis(),
                          rawBody);

        // Reading the body as a string closes the stream so create a new response body
        response = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), rawBody)).build();
      } else {
        System.out.printf("Received [%d] response for [%s %s] in %sms with non-text body%n",
                          response.code(),
                          response.request().method(),
                          response.request().url(),
                          duration.toMillis());
      }
    } else {
      System.out.printf("Received [%d] response for [%s %s] in %sms%n",
                        response.code(),
                        response.request().method(),
                        response.request().url(),
                        duration.toMillis());
    }

    return response;
  }
}
