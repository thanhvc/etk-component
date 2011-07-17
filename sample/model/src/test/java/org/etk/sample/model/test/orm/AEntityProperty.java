package org.etk.sample.model.test.orm;

import javax.jcr.PropertyType;

import org.etk.model.api.annotations.Entity;
import org.etk.model.api.annotations.Property;

@Entity(name="AEntityProperty")
public abstract class AEntityProperty {

  @Property(name = "name", type = PropertyType.STRING)
  public abstract String getName();
  public abstract void setName(String name);

  @Property(name = "description", type = PropertyType.STRING)
  public abstract String getDescription();
  public abstract void setDescription(String description);
}
