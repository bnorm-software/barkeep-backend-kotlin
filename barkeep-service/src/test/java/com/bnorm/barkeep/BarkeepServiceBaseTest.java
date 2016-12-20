// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep;

import org.assertj.core.api.Condition;
import org.junit.Before;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.BarValue;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.BookValue;
import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.ComponentValue;
import com.bnorm.barkeep.model.HasId;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.IngredientValue;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.RecipeValue;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.model.UserValue;
import com.squareup.moshi.Moshi;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public abstract class BarkeepServiceBaseTest extends AbstractIntegrationTest {

  // constants
  static final int CODE_SUCCESS = 200;
  static final int BAD_REQUEST = 400;
  static final int CODE_UNAUTHORIZED = 401;
  static final int CODE_NOT_FOUND = 404;

  static final User TEST_USER = UserValue.builder().setUsername("joe").setPassword("testmore").build();

  // conditions
  static final Condition<? super HasId> VALID_ID = new Condition<>(b -> b.getId() != null && b.getId() != -1,
                                                                   "valid id");

  // test service
  BarkeepService service;

  @Before
  public void setup() throws Exception {
    HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
    urlBuilder.scheme("http");
    urlBuilder.host("localhost");
    urlBuilder.port(port);
    //    urlBuilder.addPathSegment("api");
    //    urlBuilder.addPathSegment("rest");
    //    urlBuilder.addPathSegment("v1");
    urlBuilder.addPathSegment("barkeep");
    // This last, empty segment adds a trailing '/' which is required for relative paths in the annotations
    urlBuilder.addPathSegment("");
    HttpUrl url = urlBuilder.build();

    //    NetModule netModule = new TestNetModule(urlBuilder.build());
    //    TestComponent testComponent = DaggerBarkeepServiceBaseTest_TestComponent.builder().netModule(netModule).build();
    //
    //    service = testComponent.service();


    Authenticator authenticator = (route, response) -> {
      return response.request()
                     .newBuilder()
                     .header("Authorization", Credentials.basic(TEST_USER.getUsername(), TEST_USER.getPassword()))
                     .build();
    };
    OkHttpClient client = new OkHttpClient.Builder()
//            .addInterceptor(new WireTraceInterceptor())
                                                    .addInterceptor(new CookieInterceptor(url))
                                                    .authenticator(authenticator)
                                                    .build();
    Moshi moshi = new Moshi.Builder().add((type, annotations, m) -> {
      if (type.equals(Recipe.class)) {
        return RecipeValue.jsonAdapter(m);
      } else if (type.equals(Bar.class)) {
        return BarValue.jsonAdapter(m);
      } else if (type.equals(Book.class)) {
        return BookValue.jsonAdapter(m);
      } else if (type.equals(Ingredient.class)) {
        return IngredientValue.jsonAdapter(m);
      } else if (type.equals(Component.class)) {
        return ComponentValue.jsonAdapter(m);
      } else if (type.equals(User.class)) {
        return UserValue.jsonAdapter(m);
      } else {
        return null;
      }
    }).add(ImmutableSetAdapter.FACTORY).build();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                              .client(client)
                                              .addConverterFactory(MoshiConverterFactory.create(moshi))
                                              .build();

    service = retrofit.create(BarkeepService.class);
  }
}
