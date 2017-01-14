// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.web;

import java.io.IOException;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class CookieInterceptor implements Interceptor {

  private Cookie cookie = null;
  private final HttpUrl baseUrl;

  CookieInterceptor(HttpUrl baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request.Builder builder = chain.request().newBuilder();
    if (cookie != null) {
      builder.addHeader("Cookie", cookie.toString());
    }

    Response response = chain.proceed(builder.build());

    String setCookie = response.header("Set-Cookie");
    if (setCookie != null) {
      cookie = Cookie.parse(baseUrl, setCookie);
    }

    return response;
  }
}
