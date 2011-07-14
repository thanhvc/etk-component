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
package org.etk.model.plugins.entity.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etk.model.plugins.entity.EntityInfo;
import org.etk.reflect.api.ClassTypeInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public class EntityMapping {

  /** . */
  final EntityInfo bean;

  /** . */
  EntityMapping parent;

  /** . */
  final Map<String, PropertyMapping<?, ?, ?>> properties;

  /** . */
  final Map<String, PropertyMapping<?, ?, ?>> unmodifiableProperties;

  /** . */
  final List<MethodMapping> methods;

  /** . */
  final List<MethodMapping> unmodifiableMethods;

  /** . */
  final ClassTypeInfo formatterClassType;

  /** . */
  final boolean abstract_;

  /** . */
  final String prefix;

  public EntityMapping (
      EntityInfo bean,
      ClassTypeInfo formatterClassType,
      boolean abstract_,
      String prefix) {
    this.bean = bean;
    this.abstract_ = abstract_;
    this.properties = new HashMap<String, PropertyMapping<?, ?, ?>>();
    this.unmodifiableProperties = Collections.unmodifiableMap(properties);
    this.methods = new ArrayList<MethodMapping>();
    this.unmodifiableMethods = Collections.unmodifiableList(methods);
    this.formatterClassType = formatterClassType;
    this.prefix = prefix;
  }

  public ClassTypeInfo getFormatterClassType() {
    return formatterClassType;
  }


  public boolean isAbstract() {
    return abstract_;
  }

  public EntityInfo getBean() {
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


  @Override
  public String toString() {
    return "EntityMapping[info=" + bean + "]";
  }
}
