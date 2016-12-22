package com.bnorm.barkeep.service.recipe;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Table(keyspace = "barkeep", name = "recipes", readConsistency = "QUORUM", writeConsistency = "QUORUM")
public class Recipe {

    public static final Function<UserType, Create> TABLE_CREATE = userType -> SchemaBuilder.createTable("barkeep", "recipes")
            .ifNotExists()
            .addPartitionKey("id", DataType.uuid())
            .addColumn("name", DataType.text())
            .addColumn("description", DataType.text())
            .addColumn("instructions", DataType.list(DataType.text()))
            .addColumn("owner", DataType.uuid())
            .addColumn("image", DataType.uuid())
            .addColumn("components", DataType.list(userType))
            .addColumn("books", DataType.list(DataType.uuid()));

    @PartitionKey
    private UUID id;
    private String name;
    private String description;
    private List<String> instructions;
    private UUID owner;
    private UUID image;
    private List<Component> components;
    private List<UUID> books;

    public Recipe(UUID id, String name, String description, List<String> instructions, UUID owner, UUID image, List<Component> components, List<UUID> books) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.owner = owner;
        this.image = image;
        this.components = components;
        this.books = books;
    }

    public Recipe() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getImage() {
        return image;
    }

    public void setImage(UUID image) {
        this.image = image;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<UUID> getBooks() {
        return books;
    }

    public void setBooks(List<UUID> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", instructions=" + instructions +
                ", owner=" + owner +
                ", image=" + image +
                ", components=" + components +
                ", books=" + books +
                '}';
    }
}
