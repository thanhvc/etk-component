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
package org.etk.model.plugins.entity.binder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.etk.model.core.MethodInvoker;
import org.etk.model.core.ObjectContext;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.entity.binding.EntityTypeKind;
import org.etk.model.plugins.entity.binding.AbstractPropertyBinding;
import org.etk.orm.api.format.ObjectFormatter;
import org.etk.reflect.api.MethodInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Jul 15, 2011  
 */
public class ObjectBinder<C extends ObjectContext<C>> {

  /** . */
  private final EntityBinding binding;

  /** . */
  protected final Class<?> objectClass;

  /** . */
  private final String entityTypeName;

  /** . */
  final Set<MethodBinder<C>> methodBinders;

  /** . */
  final Set<PropertyBinder<?, ?, C, ?>> propertyBinders;

  /** . */
  private final Map<Method, MethodInvoker<C>> dispatchers;

  /** The optional formatter for this object. */
  private final ObjectFormatter formatter;

  /** . */
  private final EntityTypeKind kind;

  /** . */
  private final boolean abstract_;

  /** . */
  private final Map<String, PropertyBinder<?, ?, C, ?>> propertyBinderMap;

  public ObjectBinder(EntityBinding mapping,
                      boolean abstract_,
                      Class<?> objectClass,
                      Set<PropertyBinder<?, ?, C, ?>> propertyMappers,
                      Set<MethodBinder<C>> methodMappers,
                      ObjectFormatter formatter,
                      String typeName,
                      EntityTypeKind kind) {

    // Build the mapper map
    Map<String, PropertyBinder<?, ?, C, ?>> propertyMapperMap = new HashMap<String, PropertyBinder<?, ?, C, ?>>();
    for (PropertyBinder<?, ?, C, ?> propertyMapper : propertyMappers) {
      propertyMapperMap.put(propertyMapper.getInfo().getName(), propertyMapper);
    }

    // Build the dispatcher map
    Map<Method, MethodInvoker<C>> dispatchers = new HashMap<Method, MethodInvoker<C>>();
    for (PropertyBinder<?, ?, C, ?> propertyMapper : propertyMappers) {
      AbstractPropertyBinding<?, ?, ?> info = propertyMapper.getInfo();
      MethodInfo getter = info.getProperty().getGetter();
      if (getter != null) {
        dispatchers.put((Method)getter.unwrap(), propertyMapper.getGetter());
      }
      MethodInfo setter = info.getProperty().getSetter();
      if (setter != null) {
        dispatchers.put((Method)setter.unwrap(), propertyMapper.getSetter());
      }
    }
    for (MethodBinder<C> methodMapper : methodMappers) {
      dispatchers.put((Method)methodMapper.getMethod().unwrap(), methodMapper);
    }

    //
    this.binding = mapping;
    this.abstract_ = abstract_;
    this.dispatchers = dispatchers;
    this.objectClass = objectClass;
    this.methodBinders = methodMappers;
    this.formatter = formatter;
    this.propertyBinders = propertyMappers;
    this.entityTypeName = typeName;
    this.kind = kind;
    this.propertyBinderMap = propertyMapperMap;
  }

  public MethodInvoker<C> getInvoker(Method method) {
    return dispatchers.get(method);
  }

  public EntityBinding getBinding() {
    return binding;
  }

  public boolean isAbstract() {
    return abstract_; 
  }

  public EntityTypeKind getKind() {
    return kind;
  }

  public String getEntityTypeName() {
    return entityTypeName;
  }

  public ObjectFormatter getFormatter() {
    return formatter;
  }

  public Set<MethodBinder<C>> getMethodBinders() {
    return methodBinders;
  }

  public Set<PropertyBinder<?, ?, C, ?>> getPropertyMappers() {
    return propertyBinders;
  }

  public PropertyBinder<?, ?, C, ?> getPropertyMapper(String name) {
    return propertyBinderMap.get(name);
  }

  public Class<?> getObjectClass() {
    return objectClass;
  }


  @Override
  public String toString() {
    return "EntityBinder[class=" + objectClass + ",typeName=" + entityTypeName + "]";
  }
}
