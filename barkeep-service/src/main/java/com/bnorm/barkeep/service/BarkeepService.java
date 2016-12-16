package com.bnorm.barkeep.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bnorm.barkeep.db.BarkeepRepository;
import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/barkeep")
public class BarkeepService {

  private final BarkeepRepository repository;

  public BarkeepService(@Autowired BarkeepRepository repository) {
    this.repository = repository;
  }

  @JsonView(Bar.class)
  @RequestMapping(value = "/bars")
  public List<Bar> getBars() {
    return repository.getUser(1).getBars();
  }

  @JsonView(Bar.class)
  @RequestMapping(value = "/bars{id}")
  public Bar getBar(@PathVariable("id") long id) {
    Bar bar = repository.getBar(id);
    if (bar == null) {
      throw new ResourceNotFoundException("Unable to find bar with id:%d", id);
    }
    return bar;
  }

  @JsonView(Book.class)
  @RequestMapping("/books")
  public List<Book> getBooks() {
    return repository.getUser(1).getBooks();
  }

  @JsonView(Book.class)
  @RequestMapping("/books/{id}")
  public Book getBook(@PathVariable("id") long id) {
    Book book = repository.getBook(id);
    if (book == null) {
      throw new ResourceNotFoundException("Unable to find book with id:%d", id);
    }
    return book;
  }

  @JsonView(Recipe.class)
  @RequestMapping("/books/{bookId}/recipes")
  public Map<Long, Recipe> getRecipes(@PathVariable("bookId") long bookId) {
    Book book = getBook(bookId);
    return book.getRecipes();
  }

  @JsonView(Recipe.class)
  @RequestMapping("/books/{bookId}/recipes/{recipeNumber}")
  public Recipe getRecipe(@PathVariable("bookId") long bookId, @PathVariable("recipeNumber") long recipeNumber) {
    Book book = getBook(bookId);
    Recipe recipe = book.getRecipes().get(recipeNumber);
    if (recipe == null) {
      throw new ResourceNotFoundException("Unable to find recipe in book:%d with number:%d", bookId, recipeNumber);
    }
    return recipe;
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public static class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String s, Object... args) {
      super(String.format(s, args));
    }
  }
}
