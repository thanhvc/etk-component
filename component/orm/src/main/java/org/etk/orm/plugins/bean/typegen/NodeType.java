/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.orm.plugins.bean.typegen;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.mapping.NodeTypeKind;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class NodeType {

  /** . */
  final BeanMapping mapping;

  /** . */
  final String name;

  /** . */
  final String className;

  /** . */
  final boolean mixin;

  /** . */
  final Map<String, NodeDefinition> children;

  /** . */
  final Map<String, PropertyDefinition> properties;

  /** . */
  final Set<NodeType> superTypes;

  /** . */
  final Set<NodeType> declaredSuperTypes;

  /** . */
  final boolean orderable;

  /** . */
  final boolean referenceable;

  NodeType(BeanMapping mapping, boolean referenceable) {
    this.mapping = mapping;
    this.name = mapping.getNodeTypeName();
    this.className = mapping.getBean().getClassType().getName();
    this.mixin = mapping.getNodeTypeKind() == NodeTypeKind.MIXIN;
    this.orderable = mapping.isOrderable();
    this.children = new HashMap<String, NodeDefinition>();
    this.properties = new HashMap<String, PropertyDefinition>();
    this.superTypes = new HashSet<NodeType>();
    this.declaredSuperTypes = new HashSet<NodeType>();
    this.referenceable = referenceable;
  }

  public String getClassName() {
    return className;
  }

  public boolean isDeclared() {
    return mapping.getBean().isDeclared();
  }

  public boolean isOrderable() {
    return orderable;
  }

  public boolean isReferenceable() {
    return referenceable;
  }

  public Collection<NodeType> getSuperTypes() {
    return superTypes;
  }

  public Set<NodeType> getDeclaredSuperTypes() {
    return declaredSuperTypes;
  }

  public PropertyDefinition getPropertyDefinition(String propertyName) {
    return properties.get(propertyName);
  }

  public Map<String, PropertyDefinition> getPropertyDefinitions() {
    return properties;
  }

  public String getName() {
    return name;
  }

  public boolean isMixin() {
    return mixin;
  }

  public boolean isPrimary() {
    return !mixin;
  }

  public Map<String, NodeDefinition> getChildNodeDefinitions() {
    return children;
  }

  public NodeDefinition getChildNodeDefinition(String childNodeName) {
    return children.get(childNodeName);
  }

  void addChildNodeType(String childNodeName, boolean mandatory, boolean autocreated, BeanMapping childNodeTypeMapping) {
    NodeDefinition nodeDefinition = children.get(childNodeName);
    if (nodeDefinition == null) {
      nodeDefinition = new NodeDefinition(childNodeName, mandatory, autocreated);
      children.put(childNodeName, nodeDefinition);
    }
    nodeDefinition.mappings.add(childNodeTypeMapping);
  }

  @Override
  public String toString() {
    return "NodeType[name=" + name + "]";
  }
}