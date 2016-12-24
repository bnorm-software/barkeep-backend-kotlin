package com.bnorm.barkeep.db;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SortNatural;

import com.bnorm.barkeep.model.Component;
import com.bnorm.barkeep.model.Recipe;

@Entity
@Table(name = "tblRecipes")
@NamedQueries({@NamedQuery(name = "RecipeEntity.findAll", query = "SELECT r FROM RecipeEntity r")})
public class RecipeEntity implements Recipe {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
  private UserEntity owner;

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
  @SortNatural
  private Set<ComponentEntity> components = new TreeSet<>();

  @ManyToMany
  @JoinTable(name = "lkpBooksRecipes",
             joinColumns = @JoinColumn(name = "recipe"),
             inverseJoinColumns = @JoinColumn(name = "book"))
  @SortNatural
  private Set<BookEntity> books;

  public RecipeEntity() {
  }

  @Override
  public Long getId() {
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
  public UserEntity getOwner() {
    return owner;
  }

  public void setOwner(UserEntity owner) {
    this.owner = owner;
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

  public Instant getCreateTime() {
    return createTime.toInstant();
  }

  public Instant getModifyTime() {
    return modifyTime.toInstant();
  }

  @Override
  public Set<Component> getComponents() {
    return Collections.unmodifiableSet(components);
  }

  public void addComponent(ComponentEntity componentEntity) {
    components.add(componentEntity);
  }
}
