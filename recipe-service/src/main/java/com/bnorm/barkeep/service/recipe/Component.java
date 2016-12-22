package com.bnorm.barkeep.service.recipe;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.schemabuilder.CreateType;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.annotations.UDT;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@UDT(keyspace = "barkeep", name = "component")
public class Component {

    public static final CreateType COMPONENT_TYPE_CREATE = SchemaBuilder.createType("barkeep", "component")
            .ifNotExists()
            .addColumn("ingredients", DataType.list(DataType.uuid()))
            .addColumn("min", DataType.cint())
            .addColumn("max", DataType.cint());

    private List<UUID> ingredients;
    private int min;
    private Integer max;

    public Component(List<UUID> ingredients, int min, Integer max) {
        this.ingredients = ingredients;
        this.min = min;
        this.max = max;
    }

    public Component(List<UUID> ingredients, int min) {
        this(ingredients, min, null);
    }

    public Component(UUID ingredient, int min, Integer max) {
        this(Collections.singletonList(ingredient), min, max);
    }

    public Component(UUID ingredient, int min) {
        this(ingredient, min, null);
    }

    public Component() {
    }

    public List<UUID> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<UUID> ingredients) {
        this.ingredients = ingredients;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Component{" +
                "ingredients=" + ingredients +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
