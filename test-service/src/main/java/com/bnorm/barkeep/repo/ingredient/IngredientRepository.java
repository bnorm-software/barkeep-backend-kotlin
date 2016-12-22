package com.bnorm.barkeep.repo.ingredient;

import com.bnorm.barkeep.data.ingredient.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "ingredients", collectionResourceRel = "ingredients")
public interface IngredientRepository extends CrudRepository<Ingredient, UUID> {
}
