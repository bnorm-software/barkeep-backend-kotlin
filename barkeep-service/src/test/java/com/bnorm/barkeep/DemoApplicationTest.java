// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.BarValue;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.BookValue;
import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.ComponentValue;
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
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class DemoApplicationTest extends AbstractIntegrationTest {

  @Test
  public void simpleTest() throws IOException {
    HttpUrl url = new HttpUrl.Builder().scheme("http")
                                       .host("localhost")
                                       .addPathSegment("barkeep")
                                       .addPathSegment("")
                                       .port(port)
                                       .build();

    Authenticator authenticator = (route, response) -> {
      return response.request().newBuilder().header("Authorization", Credentials.basic("joe", "testmore")).build();
    };
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new WireTraceInterceptor())
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

    BarkeepService barkeepService = retrofit.create(BarkeepService.class);


    {
      Call<List<Bar>> call = barkeepService.getBars();
      Response<List<Bar>> response = call.execute();
      List<Bar> bars = response.body();
      System.out.println(bars);
    }

    {
      Call<List<Bar>> call = barkeepService.getBars();
      Response<List<Bar>> response = call.execute();
      List<Bar> bars = response.body();
      System.out.println(bars);
    }
  }
}