package com.bnorm.barkeep;

import java.io.IOException;
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
import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Ingredient;
import com.bnorm.barkeep.model.Recipe;
import com.bnorm.barkeep.model.User;
import com.bnorm.barkeep.model.bean.BarBean;
import com.bnorm.barkeep.model.bean.BookBean;
import com.bnorm.barkeep.model.bean.ComponentBean;
import com.bnorm.barkeep.model.bean.IngredientBean;
import com.bnorm.barkeep.model.bean.RecipeBean;
import com.bnorm.barkeep.model.bean.UserBean;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
public class RestApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestApplication.class, args);
  }

  @Bean
  public Jackson2ObjectMapperBuilder jacksonBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    builder.deserializerByType(Bar.class, new DelegateJsonDeserializer(BarBean.class));
    builder.deserializerByType(Book.class, new DelegateJsonDeserializer(BookBean.class));
    builder.deserializerByType(Component.class, new DelegateJsonDeserializer(ComponentBean.class));
    builder.deserializerByType(Ingredient.class, new DelegateJsonDeserializer(IngredientBean.class));
    builder.deserializerByType(Recipe.class, new DelegateJsonDeserializer(RecipeBean.class));
    builder.deserializerByType(User.class, new DelegateJsonDeserializer(UserBean.class));
    builder.indentOutput(true);
    return builder;
  }

  @Bean
  public EntityManager entityManager(@Value("${barkeep.db.host:192.168.99.100}") String dbHost,
                                     @Value("${barkeep.db.port:3306}") String dbPort) {
    Properties properties = new Properties();
    properties.put("hibernate.connection.url", "jdbc:mysql://" + dbHost + ':' + dbPort + "/barkeep");
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

  private static class DelegateJsonDeserializer extends JsonDeserializer {

    private final Class<?> valueType;

    private DelegateJsonDeserializer(Class<?> valueType) {
      this.valueType = valueType;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return p.readValueAs(valueType);
    }
  }
}
