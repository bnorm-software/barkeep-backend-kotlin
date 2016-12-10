package com.bnorm.barkeep.db;

import com.bnorm.barkeep.model.Component;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ComponentEntity implements Component {

  @ManyToOne
  @JoinColumn(name = "ingredient", referencedColumnName = "id", nullable = false)
  private IngredientEntity ingredient;

  @Column(name = "min", nullable = false)
  private double min;

  @Column(name = "max")
  private Double max;

  @Column(name = "componentNum", nullable = false)
  private long componentNum;

  @Column(name = "`order`", nullable = false)
  private long order;

  public ComponentEntity() {
  }

  @Override
  public IngredientEntity getIngredient() {
    return ingredient;
  }

  public void setIngredient(IngredientEntity ingredient) {
    this.ingredient = ingredient;
  }

  @Override
  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  @Override
  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }

  @Override
  public long getComponentNum() {
    return componentNum;
  }

  public void setComponentNum(long componentNum) {
    this.componentNum = componentNum;
  }

  @Override
  public long getOrder() {
    return order;
  }

  public void setOrder(long order) {
    this.order = order;
  }
}
