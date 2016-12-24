// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.assertj.core.api.Condition;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import com.bnorm.barkeep.model.HasId;

public abstract class AbstractDbServiceTest {

  private static final int MYSQL_PORT = 3306;

  // conditions
  static final Condition<? super HasId> VALID_ID = new Condition<>(b -> b.getId() != null && b.getId() != -1,
                                                                   "valid id");

  @ClassRule
  public static final GenericContainer mysql;

  static {
    mysql = new GenericContainer("mysql:latest");
    mysql.withClasspathResourceMapping("com/bnorm/barkeep/db/init", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY);
    mysql.addEnv("MYSQL_ROOT_PASSWORD", "root");
    mysql.addExposedPort(MYSQL_PORT);
  }

  protected static EntityManager em;

  @BeforeClass
  public static void setupEntityManager() {
    Properties properties = new Properties();
    properties.put("hibernate.connection.url",
                   "jdbc:mysql://" + mysql.getContainerIpAddress() + ":" + mysql.getMappedPort(MYSQL_PORT)
                           + "/barkeep");
    properties.put("hibernate.connection.username", "root");
    properties.put("hibernate.connection.password", "root");

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa",
                                                                                       properties);
    em = entityManagerFactory.createEntityManager();
  }
}