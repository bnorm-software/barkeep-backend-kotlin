// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service.db

import com.google.common.truth.Subject
import com.google.common.truth.TestVerb
import com.google.common.truth.Truth.assert_
import org.junit.BeforeClass
import org.junit.ClassRule
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import java.util.Properties
import javax.persistence.EntityManager
import javax.persistence.Persistence

abstract class AbstractDbServiceTest {
  companion object {
    private val MYSQL_PORT = 3306

    @get:ClassRule
    @JvmStatic
    val mysql: DockerContainer = DockerContainer("mysql:latest").apply {
      withClasspathResourceMapping("com/bnorm/barkeep/db/init", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
      addEnv("MYSQL_ROOT_PASSWORD", "root")
      addExposedPort(MYSQL_PORT)
    }

    @JvmStatic
    protected lateinit var emPool: Pool<EntityManager>

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
      val em = factory.createEntityManager()

      emPool = object : Pool<EntityManager> {
        override fun take(): EntityManager = em
        override fun give(type: EntityManager) {}
      }
    }
  }
}

class DockerContainer(image: String) : GenericContainer<DockerContainer>(image)

inline fun assert(block: TestVerb.() -> Unit) = assert_().block()

inline fun <T, S : Subject<S, T>> all(subject: S, block: S.() -> Unit) = subject.block()
