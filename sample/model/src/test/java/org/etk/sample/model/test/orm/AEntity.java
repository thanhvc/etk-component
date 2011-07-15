package org.etk.sample.model.test.orm;

import javax.jcr.PropertyType;

import org.etk.model.api.annotations.Entity;
import org.etk.model.api.annotations.Property;

@Entity(name="AEntity")
public class AEntity {

  private String name;
  private String description;

  public String getName() {
    return this.name;
  }

  @Property(name = "name", type = PropertyType.STRING)
  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    
    return this.description;
  }

  @Property(name = "description", type = PropertyType.STRING)
  public void setDescription(String description) {
    this.description = description;
    
  }
}
