package org.etk.orm.plugins.bean.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.etk.orm.api.NameConflictResolution;
import org.etk.orm.plugins.bean.BeanInfo;
import org.etk.reflect.api.ClassTypeInfo;

public class BeanMapping {

  /** . */
  final BeanInfo bean;

  /** . */
  BeanMapping parent;

  /** . */
  final Map<String, PropertyMapping<?, ?, ?>> properties;

  /** . */
  final Map<String, PropertyMapping<?, ?, ?>> unmodifiableProperties;

  /** . */
  final List<MethodMapping> methods;

  /** . */
  final List<MethodMapping> unmodifiableMethods;

  /** . */
  final NodeTypeKind nodeTypeKind;

  /** . */
  final ClassTypeInfo formatterClassType;

  /** . */
  final String nodeTypeName;

  /** . */
  final boolean orderable;

  /** . */
  final boolean abstract_;

  /** . */
  final NameConflictResolution onDuplicate;

  /** . */
  final String prefix;

  public BeanMapping(
      BeanInfo bean,
      NodeTypeKind nodeTypeKind,
      String nodeTypeName,
      NameConflictResolution onDuplicate,
      ClassTypeInfo formatterClassType,
      boolean orderable,
      boolean abstract_,
      String prefix) {
    this.bean = bean;
    this.nodeTypeKind = nodeTypeKind;
    this.nodeTypeName = nodeTypeName;
    this.orderable = orderable;
    this.abstract_ = abstract_;
    this.properties = new HashMap<String, PropertyMapping<?, ?, ?>>();
    this.unmodifiableProperties = Collections.unmodifiableMap(properties);
    this.methods = new ArrayList<MethodMapping>();
    this.unmodifiableMethods = Collections.unmodifiableList(methods);
    this.onDuplicate = onDuplicate;
    this.formatterClassType = formatterClassType;
    this.prefix = prefix;
  }

  public NodeTypeKind getNodeTypeKind() {
    return nodeTypeKind;
  }

  public String getNodeTypeName() {
    return nodeTypeName;
  }

  public ClassTypeInfo getFormatterClassType() {
    return formatterClassType;
  }

  public NameConflictResolution getOnDuplicate() {
    return onDuplicate;
  }

  public boolean isOrderable() {
    return orderable;
  }

  public boolean isAbstract() {
    return abstract_;
  }

  public BeanInfo getBean() {
    return bean;
  }

  public Map<String, PropertyMapping<?, ?, ?>> getProperties() {
    return properties;
  }

  public Collection<MethodMapping> getMethods() {
    return methods;
  }

  public String getPrefix() {
    return prefix;
  }

  public <M extends PropertyMapping<?, ?, ?>> M getPropertyMapping(String name, Class<M> type) {
    PropertyMapping<?, ?, ?> mapping = properties.get(name);
    if (type.isInstance(mapping)) {
      return type.cast(mapping);
    } else {
      return null;
    }
  }

  public void accept(MappingVisitor visitor) {
    visitor.startBean(this);
    for (PropertyMapping<?, ?, ?> property : properties.values()) {
      property.accept(visitor);
    }
    for (MethodMapping method : methods) {
      method.accept(visitor);
    }
    visitor.endBean();
  }

  @Override
  public String toString() {
    return "BeanMapping[info=" + bean + "]";
  }
}
