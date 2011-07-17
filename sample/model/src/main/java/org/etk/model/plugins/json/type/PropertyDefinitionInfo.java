package org.etk.model.plugins.json.type;

/**
 * <p>Meta information about a property definition.</p>
 *
 * <p>This object does not hold a reference to an existing property definition object.</p>
 *
 */
public class PropertyDefinitionInfo {

  /** . */
  private final String name;



  public PropertyDefinitionInfo(String propertyName) {
    this.name = propertyName;
  }

  
  public String getName() {
    return name;
  }

  
}