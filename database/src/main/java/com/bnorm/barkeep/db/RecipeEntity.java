package com.bnorm.barkeep.db;

import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Recipe;

import javax.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tblRecipes")
public class RecipeEntity implements Recipe {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @OneToMany(mappedBy = "key.recipe")
  private List<BookRecipeEntity> books;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "imageUrl")
  private String imageUrl;

  @Column(name = "instructions")
  private String instructions;

  @Column(name = "source")
  private String source;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createTime", updatable = false)
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modifyTime", updatable = false)
  private Date modifyTime;

  @ElementCollection
  @CollectionTable(name = "tblRecipeComponents", joinColumns = @JoinColumn(name = "recipe"))
  private List<ComponentEntity> components = new ArrayList<>();

  public RecipeEntity() {
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageurl) {
    this.imageUrl = imageurl;
  }

  @Override
  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  @Override
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  @Override
  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  @Override
  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }

  @Override
  public List<Component> getComponents() {
    return Collections.unmodifiableList(components);
  }

  public void setComponents(List<ComponentEntity> components) {
    this.components = components;
  }
}
