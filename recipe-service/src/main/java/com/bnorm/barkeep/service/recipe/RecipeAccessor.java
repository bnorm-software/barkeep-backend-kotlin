package com.bnorm.barkeep.service.recipe;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface RecipeAccessor {

    @Query("SELECT * FROM barkeep.recipes")
    Result<Recipe> recipes();
}
