package com.bnorm.barkeep.service;

import com.bnorm.barkeep.db.BarkeepRepository;
import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
@Path("/barkeep")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BarkeepService {

  private final BarkeepRepository repository;

  public BarkeepService(@Value("${barkeep.db.host:192.168.99.100}") String db) {
    Properties properties = new Properties();
    properties.put("hibernate.connection.url", "jdbc:mysql://" + db + "/barkeep");
    properties.put("hibernate.connection.username", "root");
    properties.put("hibernate.connection.password", "root");

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.bnorm.barkeep.jpa",
                                                                                       properties);
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    this.repository = new BarkeepRepository(entityManager);
  }

  @JsonView(Bar.class)
  @GET
  @Path("/bars")
  public List<Bar> getBars() {
    return repository.getUser(1).getBars();
  }

  @JsonView(Bar.class)
  @GET
  @Path("/bars/{id}")
  public Bar getBar(@PathParam("id") long id) {
    Bar bar = repository.getBar(id);
    if (bar == null) {
      throw new NotFoundException("Unable to find bar with id:" + id);
    }
    return bar;
  }

  @JsonView(Book.class)
  @GET
  @Path("/books")
  public List<Book> getBooks() {
    return repository.getUser(1).getBooks();
  }

  @JsonView(Book.class)
  @GET
  @Path("/books/{id}")
  public Book getBook(@PathParam("id") long id) {
    Book book = repository.getBook(id);
    if (book == null) {
      throw new NotFoundException("Unable to find book with id:" + id);
    }
    return book;
  }

  @JsonView(Recipe.class)
  @GET
  @Path("/books/{bookId}/recipes")
  public Map<Long, Recipe> getRecipes(@PathParam("bookId") long bookId) {
    Book book = getBook(bookId);
    return book.getRecipes();
  }

  @JsonView(Recipe.class)
  @GET
  @Path("/books/{bookId}/recipes/{recipeNumber}")
  public Recipe getRecipe(@PathParam("bookId") long bookId, @PathParam("recipeNumber") long recipeNumber) {
    Book book = getBook(bookId);
    Recipe recipe = book.getRecipes().get(recipeNumber);
    if (recipe == null) {
      throw new NotFoundException("Unable to find recipe with number:" + recipeNumber);
    }
    return recipe;
  }
}
