package com.bnorm.barkeep.service.ingredient;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
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
class IngredientRestController {

    private static final KeyspaceOptions KEYSPACE_CREATE = SchemaBuilder.createKeyspace("barkeep")
            .ifNotExists()
            .with()
            .replication(ImmutableMap.of("class", "SimpleStrategy", "replication_factor", 1));

    private final Mapper<Ingredient> mapper;
    private final IngredientAccessor accessor;

    @Autowired
    public IngredientRestController(@Value("${cassandra.host:local}") String cassandraHost) {
        Cluster cluster = Cluster.builder()
                .addContactPoint(cassandraHost)
                .build();
        Session session = cluster.connect();

        session.execute(KEYSPACE_CREATE);
        session.execute(Ingredient.TABLE_CREATE);

        MappingManager manager = new MappingManager(session);
        mapper = manager.mapper(Ingredient.class);
        accessor = manager.createAccessor(IngredientAccessor.class);
    }

    @RequestMapping(path = "/ingredients", method = RequestMethod.GET)
    public List<Ingredient> all() {
        return accessor.ingredients().all();
    }

    @RequestMapping(path = "/ingredients", method = RequestMethod.POST)
    public Ingredient create(@RequestBody Ingredient ingredient) {
        if (ingredient.getId() != null) {
            throw new IngredientIdAlreadyExists();
        }

        ingredient.setId(UUID.randomUUID());
        mapper.save(ingredient);
        return ingredient;
    }

    @RequestMapping(path = "/ingredients/{id}", method = RequestMethod.GET)
    public Ingredient get(@PathVariable("id") UUID id) {
        return mapper.get(id);
    }

    @RequestMapping(path = "/ingredients/{id}", method = RequestMethod.PUT)
    public Ingredient update(@PathVariable("id") UUID id, @RequestBody Ingredient ingredient) {
        Ingredient existing = mapper.get(id);
        if (existing == null) {
            throw new IngredientDoesNotExist();
        }
        if (!existing.getId().equals(ingredient.getId())) {
            throw new IngredientIdDoesNotMatchBody();
        }

        mapper.save(ingredient);
        return ingredient;
    }

    @RequestMapping(path = "/ingredients/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        if (mapper.get(id) == null) {
            throw new IngredientDoesNotExist();
        }

        mapper.delete(id);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such ingredient")
    private static class IngredientDoesNotExist extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Ingredient path ID does not match body")
    private static class IngredientIdDoesNotMatchBody extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Ingredient ID already exists")
    private static class IngredientIdAlreadyExists extends RuntimeException {
    }
}
