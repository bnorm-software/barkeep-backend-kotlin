// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest;

import java.io.IOException;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

class CookieInterceptor implements Interceptor {

  private Cookie cookie = null;
  private final HttpUrl baseUrl;

  CookieInterceptor(HttpUrl baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  public okhttp3.Response intercept(Chain chain) throws IOException {
    Request.Builder builder = chain.request().newBuilder();
    if (cookie != null) {
      builder.addHeader("Cookie", cookie.toString());
    }

    okhttp3.Response response = chain.proceed(builder.build());

    String setCookie = response.header("Set-Cookie");
    if (setCookie != null) {
      cookie = Cookie.parse(baseUrl, setCookie);
    }

    return response;
  }
}
