package org.etk.model.core.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.orm.plugins.bean.typegen.PropertyDefinition;

public class EntityType {

  public enum Kind {
    JSON, JDBC, JCR
  }
  /** . */
  private Kind entityKind;
  
  /** . */
  final EntityBinding binding;
  
  /** . */
  final String name;

  /** . */
  final String className;
  
  final Map<String, String> dataMap;
  
  /** . */
  final Map<String, PropertyDefinition> properties;

  public Kind getEntityKind() {
    return entityKind;
  }
  
  public EntityType(EntityBinding binding) {
    this(binding, Kind.JSON);
  }
  
  public EntityType(EntityBinding binding, Kind kind) {
    this.entityKind = Kind.JSON;
    this.binding = binding;
    this.name = binding.getEntityTypeName();
    this.className = binding.getEntity().getClassType().getName();
    this.properties = new HashMap<String, PropertyDefinition>();
    
    this.dataMap = new HashMap<String, String>();
  }
  
  public String getClassName() {
    return className;
  }

  public boolean isDeclared() {
    return binding.getEntity().isDeclared();
  }

  
  public String getName() {
    return name;
  }
  
  public PropertyDefinition getPropertyDefinition(String propertyName) {
    return properties.get(propertyName);
  }

  public Map<String, PropertyDefinition> getPropertyDefinitions() {
    return properties;
  }
  
  public Set<String> getPropertyNames() {
    return dataMap.keySet();
  }
  
  @Override
  public String toString() {
    return "EntityType[name=" + name + "]";
  }
  
}
