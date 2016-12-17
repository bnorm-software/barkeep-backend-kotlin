package com.bnorm.barkeep;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.bnorm.barkeep.db.DbBarService;
import com.bnorm.barkeep.db.DbBookService;
import com.bnorm.barkeep.db.DbIngredientService;
import com.bnorm.barkeep.db.DbRecipeService;
import com.bnorm.barkeep.db.DbUserService;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Jackson2ObjectMapperBuilder jacksonBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    builder.indentOutput(true);
    return builder;
  }

  @Bean
  public EntityManager entityManager(@Value("${barkeep.db.host:192.168.99.100}") String db) {
    Properties properties = new Properties();
    properties.put("hibernate.connection.url", "jdbc:mysql://" + db + "/barkeep");
    properties.put("hibernate.connection.username", "root");
    properties.put("hibernate.connection.password", "root");

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa",
                                                                                       properties);
    return entityManagerFactory.createEntityManager();
  }

  @Bean
  public DbUserService DbUserService(EntityManager entityManager) {
    return new DbUserService(entityManager);
  }

  @Bean
  public DbBarService DbBarService(EntityManager entityManager) {
    return new DbBarService(entityManager);
  }

  @Bean
  public DbIngredientService DbIngredientService(EntityManager entityManager) {
    return new DbIngredientService(entityManager);
  }

  @Bean
  public DbBookService DbBookService(EntityManager entityManager) {
    return new DbBookService(entityManager);
  }

  @Bean
  public DbRecipeService DbRecipeService(EntityManager entityManager) {
    return new DbRecipeService(entityManager);
  }
}
