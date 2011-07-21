package org.etk.component.orm.test.orm;

import javax.jcr.PropertyType;

import org.etk.orm.api.annotations.Property;

public class PropertyClass {

  private String name;

  @Property(name = "name", type = PropertyType.STRING)
  public String getName() {
    return name;
  }

  @Property(name = "name", type = PropertyType.STRING)
  public void setName(String name) {
    this.name = name;
  }

}
