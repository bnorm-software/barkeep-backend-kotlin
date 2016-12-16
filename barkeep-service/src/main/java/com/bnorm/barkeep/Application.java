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

import com.bnorm.barkeep.db.BarkeepRepository;
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
  public BarkeepRepository barkeepRepository(@Value("${barkeep.db.host:192.168.99.100}") String db) {
    Properties properties = new Properties();
    properties.put("hibernate.connection.url", "jdbc:mysql://" + db + "/barkeep");
    properties.put("hibernate.connection.username", "root");
    properties.put("hibernate.connection.password", "root");

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa",
                                                                                       properties);
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    return new BarkeepRepository(entityManager);
  }
}
