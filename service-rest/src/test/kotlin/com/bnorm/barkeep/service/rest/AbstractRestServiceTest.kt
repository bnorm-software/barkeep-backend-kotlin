// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.rest

import com.bnorm.barkeep.RestApplication
import com.bnorm.barkeep.service.db.DbUserService
import com.bnorm.barkeep.model.value.BarValueAdapter
import com.bnorm.barkeep.model.value.BookValueAdapter
import com.bnorm.barkeep.model.value.ComponentValueAdapter
import com.bnorm.barkeep.model.value.IngredientValueAdapter
import com.bnorm.barkeep.model.value.RecipeValueAdapter
import com.bnorm.barkeep.model.value.UserSpecValue
import com.bnorm.barkeep.model.value.UserValueAdapter
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
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.EnvironmentTestUtils
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Properties
import javax.persistence.Persistence

@RunWith(SpringRunner::class)
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

    internal val TEST_USER = UserSpecValue(username = "joe", password = "testmore", email = "joe@test.more")

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
      userService.createUser(TEST_USER.copy(password = passwordEncoder.encode(TEST_USER.password!!)))
    }
  }

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
    val moshi = Moshi.Builder()
            .add(BarValueAdapter)
            .add(BookValueAdapter)
            .add(ComponentValueAdapter)
            .add(IngredientValueAdapter)
            .add(RecipeValueAdapter)
            .add(UserValueAdapter)
            .build()
    val retrofit = Retrofit.Builder().baseUrl(url)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    service = retrofit.create(BarkeepService::class.java)
  }
}

class DockerContainer(image: String) : GenericContainer<DockerContainer>(image)
