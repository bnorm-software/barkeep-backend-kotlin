package com.bnorm.barkeep.data.ingredient;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@Table("ingredients")
public class Ingredient {

    @PrimaryKey
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
                '}';
    }
}
