// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.rest

import com.bnorm.barkeep.RestApplication
import com.bnorm.barkeep.db.DbUserService
import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.BarValue
import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.BookValue
import com.bnorm.barkeep.model.Component
import com.bnorm.barkeep.model.ComponentValue
import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.IngredientValue
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.RecipeValue
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.UserValue
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.Matcher
import io.kotlintest.matchers.Matchers
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.EnvironmentTestUtils
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.rules.SpringClassRule
import org.springframework.test.context.junit4.rules.SpringMethodRule
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Properties
import javax.persistence.Persistence

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = arrayOf(RestApplication::class),
                      initializers = arrayOf(AbstractRestServiceTest.Initializer::class))
@DirtiesContext // forces a reload of app for every test - required because database is reloaded
abstract class AbstractRestServiceTest : Matchers {
  companion object {
    val beValid = object : Matcher<Long> {
      override fun test(value: Long) {
        if (value == -1L)
          throw AssertionError("$value is not a valid ID")
      }
    }

    @get:ClassRule
    @JvmStatic
    val SPRING_CLASS_RULE = SpringClassRule()

    private val MYSQL_PORT = 3306

    @get:ClassRule
    @JvmStatic
    val mysql: DockerContainer = DockerContainer("mysql:latest").apply {
      withClasspathResourceMapping("com/bnorm/barkeep/db/init", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
      addEnv("MYSQL_ROOT_PASSWORD", "root")
      addExposedPort(MYSQL_PORT)
    }

    // constants
    internal val CODE_SUCCESS = 200
    internal val BAD_REQUEST = 400
    internal val CODE_UNAUTHORIZED = 401
    internal val CODE_NOT_FOUND = 404

    internal val TEST_USER: User = UserValue.builder()
            .setUsername("joe")
            .setPassword("testmore")
            .setEmail("joe@test.more")
            .build()

    @BeforeClass
    @JvmStatic
    fun setupDatabase() {
      // todo figure out a better way to create test user
      val address = mysql.getContainerIpAddress()
      val port = mysql.getMappedPort(MYSQL_PORT)

      val properties = Properties()
      properties.put("hibernate.connection.url", "jdbc:mysql://$address:$port/barkeep")
      properties.put("hibernate.connection.username", "root")
      properties.put("hibernate.connection.password", "root")

      val factory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa", properties)
      val em = factory.createEntityManager()
      val userService = DbUserService(em)

      val passwordEncoder = BCryptPasswordEncoder()
      userService.createUser(UserValue.builder()
                                     .setUsername(TEST_USER.username)
                                     .setPassword(passwordEncoder.encode(TEST_USER.password!!))
                                     .setEmail(TEST_USER.email)
                                     .build())
    }
  }

  @Rule
  val springMethodRule = SpringMethodRule()

  @LocalServerPort
  private val port: Int = 0


  class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
      val address = mysql.getContainerIpAddress()
      val port = mysql.getMappedPort(MYSQL_PORT)
      EnvironmentTestUtils.addEnvironment("testcontainers",
                                          configurableApplicationContext.environment,
                                          "barkeep.db.host=$address",
                                          "barkeep.db.port=$port")
    }
  }

  // test service

  lateinit var service: BarkeepService

  @Before
  @Throws(Exception::class)
  fun setup() {
    val urlBuilder = HttpUrl.Builder()
    urlBuilder.scheme("http")
    urlBuilder.host("localhost")
    urlBuilder.port(port)
    urlBuilder.addPathSegment("rest")
    urlBuilder.addPathSegment("api")
    urlBuilder.addPathSegment("v1")
    // This last, empty segment adds a trailing '/' which is required for relative paths in the annotations
    urlBuilder.addPathSegment("")
    val url = urlBuilder.build()


    val authenticator = Authenticator { route, response ->
      response.request()
              .newBuilder()
              .header("Authorization", Credentials.basic(TEST_USER.username, TEST_USER.password))
              .build()
    }
    val client = OkHttpClient.Builder().addInterceptor(CookieInterceptor(url))
            .addInterceptor(WireTraceInterceptor())
            .authenticator(authenticator)
            .build()
    val moshi = Moshi.Builder().add(JsonAdapter.Factory { type, annotations, m ->
      if (type == Recipe::class.java) {
        return@Factory RecipeValue.jsonAdapter(m)
      } else if (type == Bar::class.java) {
        return@Factory BarValue.jsonAdapter(m)
      } else if (type == Book::class.java) {
        return@Factory BookValue.jsonAdapter(m)
      } else if (type == Ingredient::class.java) {
        return@Factory IngredientValue.jsonAdapter(m)
      } else if (type == Component::class.java) {
        return@Factory ComponentValue.jsonAdapter(m)
      } else if (type == User::class.java) {
        return@Factory UserValue.jsonAdapter(m)
      } else {
        return@Factory null
      }
    }).add(ImmutableSetAdapter.FACTORY).build()
    val retrofit = Retrofit.Builder().baseUrl(url)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    service = retrofit.create(BarkeepService::class.java)
  }
}

class DockerContainer(image: String) : GenericContainer<DockerContainer>(image)
