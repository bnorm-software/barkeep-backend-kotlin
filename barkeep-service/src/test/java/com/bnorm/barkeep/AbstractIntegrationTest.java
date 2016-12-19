// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import com.bnorm.barkeep.db.DbUserService;
import com.bnorm.barkeep.model.UserValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(value = Application.class, initializers = AbstractIntegrationTest.Initializer.class)
@WebIntegrationTest(randomPort = true)
public abstract class AbstractIntegrationTest {

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


      // todo figure out a better way to create test user
      Properties properties = new Properties();
      properties.put("hibernate.connection.url",
                     "jdbc:mysql://" + mysql.getContainerIpAddress() + ":" + mysql.getMappedPort(MYSQL_PORT)
                             + "/barkeep");
      properties.put("hibernate.connection.username", "root");
      properties.put("hibernate.connection.password", "root");

      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa",
                                                                                         properties);
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      DbUserService userService = new DbUserService(entityManager);

      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      userService.createUser(UserValue.builder()
                                      .setUsername("joe")
                                      .setPassword(passwordEncoder.encode("testmore"))
                                      .setEmail("joe@test.more")
                                      .build());
    }
  }


  @Value("${local.server.port}")
  protected int port;
}