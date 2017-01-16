// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep

import com.bnorm.barkeep.db.DbBarService
import com.bnorm.barkeep.db.DbBookService
import com.bnorm.barkeep.db.DbIngredientService
import com.bnorm.barkeep.db.DbRecipeService
import com.bnorm.barkeep.db.DbUserService
import com.bnorm.barkeep.model.Bar
import com.bnorm.barkeep.model.Book
import com.bnorm.barkeep.model.Component
import com.bnorm.barkeep.model.Ingredient
import com.bnorm.barkeep.model.Recipe
import com.bnorm.barkeep.model.User
import com.bnorm.barkeep.model.bean.BarBean
import com.bnorm.barkeep.model.bean.BookBean
import com.bnorm.barkeep.model.bean.ComponentBean
import com.bnorm.barkeep.model.bean.IngredientBean
import com.bnorm.barkeep.model.bean.RecipeBean
import com.bnorm.barkeep.model.bean.UserBean
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.io.IOException
import java.util.Properties
import javax.persistence.EntityManager
import javax.persistence.Persistence

@SpringBootApplication
open class RestApplication {

  @Bean
  open fun jacksonBuilder(): Jackson2ObjectMapperBuilder {
    val builder = Jackson2ObjectMapperBuilder()
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    builder.deserializerByType(Bar::class.java, DelegateJsonDeserializer(BarBean::class.java))
    builder.deserializerByType(Book::class.java, DelegateJsonDeserializer(BookBean::class.java))
    builder.deserializerByType(Component::class.java, DelegateJsonDeserializer(ComponentBean::class.java))
    builder.deserializerByType(Ingredient::class.java, DelegateJsonDeserializer(IngredientBean::class.java))
    builder.deserializerByType(Recipe::class.java, DelegateJsonDeserializer(RecipeBean::class.java))
    builder.deserializerByType(User::class.java, DelegateJsonDeserializer(UserBean::class.java))
    builder.indentOutput(true)
    return builder
  }

  @Bean
  open fun entityManager(@Value("\${barkeep.db.host:192.168.99.100}") dbHost: String,
                         @Value("\${barkeep.db.port:3306}") dbPort: String): EntityManager {
    val properties = Properties()
    properties.put("hibernate.connection.url", "jdbc:mysql://$dbHost:$dbPort/barkeep")
    properties.put("hibernate.connection.username", "root")
    properties.put("hibernate.connection.password", "root")

    val entityManagerFactory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa", properties)
    return entityManagerFactory.createEntityManager()
  }

  @Bean
  open fun getDbUserService(entityManager: EntityManager): DbUserService {
    return DbUserService(entityManager)
  }

  @Bean
  open fun getDbBarService(entityManager: EntityManager): DbBarService {
    return DbBarService(entityManager)
  }

  @Bean
  open fun getDbIngredientService(entityManager: EntityManager): DbIngredientService {
    return DbIngredientService(entityManager)
  }

  @Bean
  open fun getDbBookService(entityManager: EntityManager): DbBookService {
    return DbBookService(entityManager)
  }

  @Bean
  open fun getDbRecipeService(entityManager: EntityManager): DbRecipeService {
    return DbRecipeService(entityManager)
  }
}

private class DelegateJsonDeserializer<T>(private val valueType: Class<T>) : JsonDeserializer<T>() {

  @Throws(IOException::class)
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
    return p.readValueAs<T>(valueType)
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(RestApplication::class.java, *args)
}
