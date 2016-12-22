package com.bnorm.barkeep.service.ingredient;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(keyspace = "barkeep", name = "ingredients", readConsistency = "QUORUM", writeConsistency = "QUORUM")
public class Ingredient {

    public static final Create TABLE_CREATE = SchemaBuilder.createTable("barkeep", "ingredients")
            .ifNotExists()
            .addPartitionKey("id", DataType.uuid())
            .addColumn("parent", DataType.uuid())
            .addColumn("name", DataType.text());

    @PartitionKey
    private UUID id;
    private String name;
    private UUID parent;

    public Ingredient(UUID id, String name, UUID parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public Ingredient() {
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

    public UUID getParent() {
        return parent;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
