// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import com.bnorm.barkeep.Application;
import com.bnorm.barkeep.db.DbUserService;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class, initializers = AbstractRestServiceTest.Initializer.class)
public abstract class AbstractRestServiceTest {

  private static final int MYSQL_PORT = 3306;

  @ClassRule
  public static final GenericContainer mysql;

  static {
    mysql = new GenericContainer("mysql:latest");
    mysql.withClasspathResourceMapping("com/bnorm/barkeep/db/init", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY);
    mysql.addEnv("MYSQL_ROOT_PASSWORD", "root");
    mysql.addExposedPort(MYSQL_PORT);
  }


  public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      EnvironmentTestUtils.addEnvironment("testcontainers",
                                          configurableApplicationContext.getEnvironment(),
                                          "barkeep.db.host=" + mysql.getContainerIpAddress(),
                                          "barkeep.db.port=" + mysql.getMappedPort(MYSQL_PORT));
    }
  }


  // constants
  static final int CODE_SUCCESS = 200;
  static final int BAD_REQUEST = 400;
  static final int CODE_UNAUTHORIZED = 401;
  static final int CODE_NOT_FOUND = 404;

  static final User TEST_USER = UserValue.builder()
                                         .setUsername("joe")
                                         .setPassword("testmore")
                                         .setEmail("joe@test.more")
                                         .build();

  // conditions
  static final Condition<? super HasId> VALID_ID = new Condition<>(b -> b.getId() != null && b.getId() != -1,
                                                                   "valid id");

  // test service

  @LocalServerPort
  private int port;

  BarkeepService service;

  @BeforeClass
  public static void setupDatabase() {
    // todo figure out a better way to create test user
    Properties properties = new Properties();
    properties.put("hibernate.connection.url",
                   String.format("jdbc:mysql://%s:%d/barkeep",
                                 mysql.getContainerIpAddress(),
                                 mysql.getMappedPort(MYSQL_PORT)));
    properties.put("hibernate.connection.username", "root");
    properties.put("hibernate.connection.password", "root");

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa",
                                                                                       properties);
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    DbUserService userService = new DbUserService(entityManager);

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    userService.createUser(UserValue.builder()
                                    .setUsername(TEST_USER.getUsername())
                                    .setPassword(passwordEncoder.encode(TEST_USER.getPassword()))
                                    .setEmail(TEST_USER.getEmail())
                                    .build());
  }

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


    Authenticator authenticator = (route, response) -> {
      return response.request()
                     .newBuilder()
                     .header("Authorization", Credentials.basic(TEST_USER.getUsername(), TEST_USER.getPassword()))
                     .build();
    };
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new CookieInterceptor(url))
                                                    // .addInterceptor(new WireTraceInterceptor())
                                                    .authenticator(authenticator).build();
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