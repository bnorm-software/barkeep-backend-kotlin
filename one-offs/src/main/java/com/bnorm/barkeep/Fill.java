package com.bnorm.barkeep;

import com.bnorm.barkeep.service.ingredient.Ingredient;
import com.bnorm.barkeep.service.recipe.Component;
import com.bnorm.barkeep.service.recipe.Recipe;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.CreateType;
import com.datastax.driver.core.schemabuilder.KeyspaceOptions;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Function;

class Fill {

    public static final String KEYSPACE_NAME = "barkeep";
    public static final String TYPE_NAME_COMPONENT = "component";

    private static final KeyspaceOptions KEYSPACE_CREATE = SchemaBuilder.createKeyspace(KEYSPACE_NAME)
            .ifNotExists()
            .with()
            .replication(ImmutableMap.of("class", "SimpleStrategy", "replication_factor", 1));
    public static final CreateType TYPE_CREATE_COMPONENT = SchemaBuilder.createType(KEYSPACE_NAME, TYPE_NAME_COMPONENT)
            .ifNotExists()
            .addColumn("ingredients", DataType.list(DataType.uuid()))
            .addColumn("min", DataType.cint())
            .addColumn("max", DataType.cint());

    public static final Create TABLE_CREATE_INGREDIENTS = SchemaBuilder.createTable(KEYSPACE_NAME, "ingredients")
            .ifNotExists()
            .addPartitionKey("id", DataType.uuid())
            .addColumn("parent", DataType.uuid())
            .addColumn("name", DataType.text());

    public static final Function<UserType, Create> TABLE_CREATE_RECIPES = userType -> SchemaBuilder.createTable(KEYSPACE_NAME, "recipes")
            .ifNotExists()
            .addPartitionKey("id", DataType.uuid())
            .addColumn("name", DataType.text())
            .addColumn("description", DataType.text())
            .addColumn("instructions", DataType.list(DataType.text()))
            .addColumn("owner", DataType.uuid())
            .addColumn("image", DataType.uuid())
            .addColumn("components", DataType.list(userType))
            .addColumn("books", DataType.list(DataType.uuid()));

    public static void main(String[] args) {
        try (Cluster cluster = Cluster.builder()
                .addContactPoint("192.168.99.100")
                .build()) {
            try (Session session = cluster.connect()) {
                session.execute(KEYSPACE_CREATE);
                session.execute(TABLE_CREATE_INGREDIENTS);
                session.execute(TYPE_CREATE_COMPONENT);
                UserType component = cluster.getMetadata().getKeyspace(KEYSPACE_NAME).getUserType(TYPE_NAME_COMPONENT);
                session.execute(TABLE_CREATE_RECIPES.apply(component));

                MappingManager manager = new MappingManager(session);
                Mapper<Recipe> recipes = manager.mapper(Recipe.class);
                Mapper<Ingredient> ingredients = manager.mapper(Ingredient.class);

                // users
                UUID bnorman = UUID.randomUUID();
                UUID mnorman = UUID.randomUUID();

                // ingredients
                UUID bourbon = UUID.randomUUID();
                UUID bourbonWhiskey = UUID.randomUUID();
                UUID cointreau = UUID.randomUUID();
                UUID dryVermouth = UUID.randomUUID();
                UUID gin = UUID.randomUUID();
                UUID gommeSyrup = UUID.randomUUID();
                UUID lemonJuice = UUID.randomUUID();
                UUID limeJuice = UUID.randomUUID();
                UUID rum = UUID.randomUUID();
                UUID simpleSyrup = UUID.randomUUID();
                UUID sweetVermouth = UUID.randomUUID();
                UUID tequila = UUID.randomUUID();
                UUID vermouth = UUID.randomUUID();
                UUID vodka = UUID.randomUUID();
                UUID whiteRum = UUID.randomUUID();

                // books
                UUID classics = UUID.randomUUID();

                ingredients.save(new Ingredient(bourbon, "Bourbon", null));
                ingredients.save(new Ingredient(bourbonWhiskey, "Bourbon Whiskey", bourbon));
                ingredients.save(new Ingredient(cointreau, "Cointreau", null));
                ingredients.save(new Ingredient(dryVermouth, "Dry Vermouth", vermouth));
                ingredients.save(new Ingredient(gin, "Gin", null));
                ingredients.save(new Ingredient(gommeSyrup, "Gomme Syrup", null));
                ingredients.save(new Ingredient(lemonJuice, "Lemon Juice", null));
                ingredients.save(new Ingredient(limeJuice, "Lime Juice", null));
                ingredients.save(new Ingredient(rum, "Rum", null));
                ingredients.save(new Ingredient(simpleSyrup, "Simple Syrup", null));
                ingredients.save(new Ingredient(sweetVermouth, "Sweet Vermouth", vermouth));
                ingredients.save(new Ingredient(tequila, "Tequila", null));
                ingredients.save(new Ingredient(vermouth, "Vermouth", null));
                ingredients.save(new Ingredient(vodka, "Vodka", null));
                ingredients.save(new Ingredient(whiteRum, "WhiteRum", rum));

                recipes.save(new Recipe(UUID.randomUUID(), "Daiquiri", null,
                        Arrays.asList(
                                "Pour all ingredients into shaker with ice cubes.",
                                "Shake well.",
                                "Strain in chilled cocktail glass."),
                        mnorman, null,
                        Arrays.asList(
                                new Component(whiteRum, 9),
                                new Component(limeJuice, 5),
                                new Component(simpleSyrup, 3)),
                        Collections.singletonList(classics)));
                recipes.save(new Recipe(UUID.randomUUID(), "Margarita", null,
                        Arrays.asList(
                                "Rub the rim of the glass with the lime slice to make the salt stick to it.",
                                "Take care to moisten only the outer rim and sprinkle the salt on it.",
                                "The salt should present to the lips of the imbiber and never mix into the cocktail.",
                                "Shake the other ingredients with ice, then carefully pour into the glass."),
                        mnorman, null,
                        Arrays.asList(
                                new Component(tequila, 7),
                                new Component(cointreau, 4),
                                new Component(limeJuice, 3)),
                        Collections.singletonList(classics)));
                recipes.save(new Recipe(UUID.randomUUID(), "Whiskey Sour", null,
                        Arrays.asList(
                                "Shake with ice.",
                                "Strain into chilled glass, garnish and serve."),
                        bnorman, null,
                        Arrays.asList(
                                new Component(bourbonWhiskey, 3),
                                new Component(lemonJuice, 2),
                                new Component(gommeSyrup, 1)),
                        Collections.singletonList(classics)));
            }
        }
    }
}
