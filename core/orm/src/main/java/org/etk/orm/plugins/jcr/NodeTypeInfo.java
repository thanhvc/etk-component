package org.etk.orm.plugins.jcr;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDefinition;

import org.etk.orm.plugins.jcr.type.PropertyDefinitionInfo;

/**
 * <p>
 * Meta information about a node type.
 * </p>
 * <p>
 * This object does not hold a reference to an existing node type object.
 * </p>
 */
public class NodeTypeInfo {

  /** . */
  private final String name;

  /** . */
  private final Map<String, PropertyDefinitionInfo> propertyDefinitions;

  public NodeTypeInfo(NodeType nodeType) {
    Map<String, PropertyDefinitionInfo> propertyDefinitions = new HashMap<String, PropertyDefinitionInfo>();
    for (PropertyDefinition propertyDefinition : nodeType.getPropertyDefinitions()) {
      PropertyDefinitionInfo propertyDefinitionInfo = new PropertyDefinitionInfo(propertyDefinition);
      propertyDefinitions.put(propertyDefinitionInfo.getName(), propertyDefinitionInfo);
    }

    //
    this.name = nodeType.getName();
    this.propertyDefinitions = propertyDefinitions;
  }

  public String getName() {
    return name;
  }

  public PropertyDefinitionInfo getPropertyDefinitionInfo(String name) {
    return propertyDefinitions.get(name);
  }

  public PropertyDefinitionInfo findPropertyDefinition(String propertyName) {
    PropertyDefinitionInfo propertyDefinitionInfo = getPropertyDefinitionInfo(propertyName);
    if (propertyDefinitionInfo == null) {
      return getPropertyDefinitionInfo("*");
    }
    return propertyDefinitionInfo;
  }
}
