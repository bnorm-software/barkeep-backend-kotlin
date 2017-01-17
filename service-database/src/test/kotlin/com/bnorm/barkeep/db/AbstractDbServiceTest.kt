// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import io.kotlintest.matchers.Matcher
import io.kotlintest.matchers.Matchers
import org.junit.BeforeClass
import org.junit.ClassRule
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import java.util.Properties
import javax.persistence.EntityManager
import javax.persistence.Persistence

abstract class AbstractDbServiceTest : Matchers {
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

    @JvmStatic
    protected lateinit var em: EntityManager

    @BeforeClass
    @JvmStatic
    fun setupEntityManager() {
      val address = mysql.getContainerIpAddress()
      val port = mysql.getMappedPort(MYSQL_PORT)

      val properties = Properties()
      properties.put("hibernate.connection.url", "jdbc:mysql://$address:$port/barkeep")
      properties.put("hibernate.connection.username", "root")
      properties.put("hibernate.connection.password", "root")

      val factory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa", properties)
      em = factory.createEntityManager()
    }
  }
}

class DockerContainer(image: String) : GenericContainer<DockerContainer>(image)
