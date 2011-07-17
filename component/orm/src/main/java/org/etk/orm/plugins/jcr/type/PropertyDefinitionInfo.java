package org.etk.orm.plugins.jcr.type;

import javax.jcr.nodetype.PropertyDefinition;


/**
 * <p>Meta information about a property definition.</p>
 *
 * <p>This object does not hold a reference to an existing property definition object.</p>
 *
 */
public class PropertyDefinitionInfo {

  /** . */
  private final String name;

  /** . */
  private final int type;

  /** . */
  private final boolean multiple;

  public PropertyDefinitionInfo(PropertyDefinition propertyDefinition) {
    this.name = propertyDefinition.getName();
    this.type = propertyDefinition.getRequiredType();
    this.multiple = propertyDefinition.isMultiple();
  }

  public PropertyDefinitionInfo(String name, int type, boolean multiple) {
    this.name = name;
    this.type = type;
    this.multiple = multiple;
  }

  public String getName() {
    return name;
  }

  public int getType() {
    return type;
  }

  public boolean isMultiple() {
    return multiple;
  }

  
}