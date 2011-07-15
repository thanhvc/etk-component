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
package org.etk.model.plugins.entity.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etk.model.plugins.entity.EntityInfo;
import org.etk.orm.plugins.bean.mapping.MappingVisitor;
import org.etk.orm.plugins.bean.mapping.MethodMapping;
import org.etk.orm.plugins.bean.mapping.PropertyMapping;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public class EntityBinding {

  /** . */
  final EntityInfo entityInfo;

  /** . */
  EntityBinding parent;

  /** . */
  final Map<String, PropertyBinding<?, ?, ?>> properties;

  /** . */
  final Map<String, PropertyBinding<?, ?, ?>> unmodifiableProperties;

  /** . */
  final List<MethodBinding> methods;

  /** . */
  final List<MethodBinding> unmodifiableMethods;

  /** . */
  final boolean abstract_;

  /** . */
  final String prefix;
  
  /** . */
  final EntityTypeKind entityTypeKind;

  /** . */
  final String entityTypeName;

  public EntityBinding(EntityInfo entity,
                       EntityTypeKind entityTypeKind,
                       String entityTypeName,
                       boolean abstract_,
                       String prefix) {
    this.entityInfo = entity;
    this.entityTypeKind = entityTypeKind;
    this.entityTypeName = entityTypeName;
    this.abstract_ = abstract_;
    this.prefix = prefix;
    
    
    this.properties = new HashMap<String, PropertyBinding<?, ?, ?>>();
    this.unmodifiableProperties = Collections.unmodifiableMap(properties);
    this.methods = new ArrayList<MethodBinding>();
    this.unmodifiableMethods = Collections.unmodifiableList(methods);
  }


  public EntityTypeKind getEntityTypeKind() {
    return this.entityTypeKind;
  }

  public String getEntityTypeName() {
    return this.entityTypeName;
  }
  
  public boolean isAbstract() {
    return abstract_;
  }

  public EntityInfo getBean() {
    return entityInfo;
  }

  public Map<String, PropertyBinding<?, ?, ?>> getProperties() {
    return properties;
  }

  public Collection<MethodBinding> getMethods() {
    return methods;
  }

  public String getPrefix() {
    return prefix;
  }

  public <M extends PropertyBinding<?, ?, ?>> M getPropertyMapping(String name, Class<M> type) {
    PropertyBinding<?, ?, ?> mapping = properties.get(name);
    if (type.isInstance(mapping)) {
      return type.cast(mapping);
    } else {
      return null;
    }
  }


  public void accept(BindingVisitor visitor) {
    visitor.startBean(this);
    for (PropertyBinding<?, ?, ?> property : properties.values()) {
      property.accept(visitor);
    }
    for (MethodBinding method : methods) {
      method.accept(visitor);
    }
    visitor.endBean();
  }
  
  @Override
  public String toString() {
    return "EntityBinding[info=" + entityInfo + "]";
  }
}
