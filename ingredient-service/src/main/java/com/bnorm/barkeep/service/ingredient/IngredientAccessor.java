package com.bnorm.barkeep.service.ingredient;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface IngredientAccessor {

    @Query("SELECT * FROM barkeep.ingredients")
    Result<Ingredient> ingredients();
}
