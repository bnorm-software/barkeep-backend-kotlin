package com.bnorm.barkeep.service.recipe;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.schemabuilder.KeyspaceOptions;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
class RecipeRestController {

    private static final KeyspaceOptions KEYSPACE_CREATE = SchemaBuilder.createKeyspace("barkeep")
            .ifNotExists()
            .with()
            .replication(ImmutableMap.of("class", "SimpleStrategy", "replication_factor", 1));

    private final Mapper<Recipe> mapper;
    private final RecipeAccessor accessor;

    @Autowired
    public RecipeRestController(@Value("${cassandra.host:localhost}") String cassandraHost) {
        Cluster cluster = Cluster.builder()
                .addContactPoint(cassandraHost)
                .build();
        Session session = cluster.connect();

        session.execute(KEYSPACE_CREATE);
        session.execute(Component.COMPONENT_TYPE_CREATE);
        UserType component = cluster.getMetadata().getKeyspace("barkeep").getUserType("component");
        session.execute(Recipe.TABLE_CREATE.apply(component));

        MappingManager manager = new MappingManager(session);
        mapper = manager.mapper(Recipe.class);
        accessor = manager.createAccessor(RecipeAccessor.class);
    }

    @RequestMapping(path = "/recipes", method = RequestMethod.GET)
    public List<Recipe> all() {
        return accessor.recipes().all();
    }

    @RequestMapping(path = "/recipes", method = RequestMethod.POST)
    public Recipe create(@RequestBody Recipe recipe) {
        if (recipe.getId() != null) {
            throw new RecipeIdAlreadyExists();
        }

        recipe.setId(UUID.randomUUID());
        mapper.save(recipe);
        return recipe;
    }

    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.GET)
    public Recipe get(@PathVariable("id") UUID id) {
        return mapper.get(id);
    }

    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.PUT)
    public Recipe update(@PathVariable("id") UUID id, @RequestBody Recipe recipe) {
        Recipe existing = mapper.get(id);
        if (existing == null) {
            throw new RecipeDoesNotExist();
        }
        if (!existing.getId().equals(recipe.getId())) {
            throw new RecipeIdDoesNotMatchBody();
        }

        mapper.save(recipe);
        return recipe;
    }

    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        if (mapper.get(id) == null) {
            throw new RecipeDoesNotExist();
        }

        mapper.delete(id);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such recipe")
    private static class RecipeDoesNotExist extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Recipe path ID does not match body")
    private static class RecipeIdDoesNotMatchBody extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Recipe ID already exists")
    private static class RecipeIdAlreadyExists extends RuntimeException {
    }
}
