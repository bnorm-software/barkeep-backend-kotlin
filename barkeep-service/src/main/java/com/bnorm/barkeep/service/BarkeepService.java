package com.bnorm.barkeep.service;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bnorm.barkeep.db.BarkeepRepository;
import com.bnorm.barkeep.db.UserEntityDetails;
import com.bnorm.barkeep.model.Bar;
import com.bnorm.barkeep.model.Book;
import com.bnorm.barkeep.model.Recipe;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/barkeep")
public class BarkeepService {

  private final Logger log = LoggerFactory.getLogger(BarkeepService.class);

  private final BarkeepRepository repository;

  public BarkeepService(@Autowired BarkeepRepository repository) {
    this.repository = repository;
  }

  private static long user(Principal principal) {
    if (principal instanceof UsernamePasswordAuthenticationToken) {
      UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
      if (token.getPrincipal() instanceof UserEntityDetails) {
        UserEntityDetails userDetails = (UserEntityDetails) token.getPrincipal();
        return userDetails.getId();
      }
    }
    throw new InternalServerErrorException("Unable to retrieve user from authentication");
  }

  @JsonView(Bar.class)
  @RequestMapping(value = "/bars")
  public Collection<Bar> getBars(Principal principal) {
    return repository.getBars(user(principal));
  }

  @JsonView(Bar.class)
  @RequestMapping(value = "/bars/{id}")
  public Bar getBar(@PathVariable("id") long id, Principal principal) {
    Bar bar = repository.getBar(user(principal), id);
    if (bar == null) {
      throw new NotFoundException("Unable to find bar with id:%d", id);
    }
    return bar;
  }

  @JsonView(Book.class)
  @RequestMapping("/books")
  public Collection<Book> getBooks(Principal principal) {
    return repository.getBooks(user(principal));
  }

  @JsonView(Book.class)
  @RequestMapping("/books/{id}")
  public Book getBook(@PathVariable("id") long id, Principal principal) {
    Book book = repository.getBook(user(principal), id);
    if (book == null) {
      throw new NotFoundException("Unable to find book with id:%d", id);
    }
    return book;
  }

  @JsonView(Recipe.class)
  @RequestMapping("/books/{bookId}/recipes")
  public Map<Long, Recipe> getRecipes(@PathVariable("bookId") long bookId, Principal principal) {
    Book book = getBook(bookId, principal);
    return book.getRecipes();
  }

  @JsonView(Recipe.class)
  @RequestMapping("/books/{bookId}/recipes/{recipeNumber}")
  public Recipe getRecipe(@PathVariable("bookId") long bookId,
                          @PathVariable("recipeNumber") long recipeNumber,
                          Principal principal) {
    Book book = getBook(bookId, principal);
    Recipe recipe = book.getRecipes().get(recipeNumber);
    if (recipe == null) {
      throw new NotFoundException("Unable to find recipe in book:%d with number:%d", bookId, recipeNumber);
    }
    return recipe;
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public static class NotFoundException extends RuntimeException {

    public NotFoundException(String s, Object... args) {
      super(String.format(s, args));
    }
  }


  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public static class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String s, Object... args) {
      super(String.format(s, args));
    }
  }


  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public static class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String s, Object... args) {
      super(String.format(s, args));
    }
  }
}
