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
package org.etk.model.plugins.entity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.etk.model.plugins.entity.mapping.EntityMapping;
import org.etk.orm.api.annotations.FormattedBy;
import org.etk.orm.plugins.bean.BeanInfo;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.type.SimpleTypeResolver;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.api.annotation.AnnotationInfo;
import org.etk.reflect.core.AnnotationType;
import org.etk.reflect.core.TypeResolverImpl;
import org.etk.reflect.jlr.metadata.JLReflectionMetadata;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public class EntityMappingBuilder {

  /** Used for receiving {@code java.lang.Object} */
  private final TypeResolver<Type> domain = TypeResolverImpl.create(JLReflectionMetadata.newInstance());
  
  private final SimpleTypeResolver simpleTypeResolver;
  
  public EntityMappingBuilder() {
    this(new SimpleTypeResolver());
  }
  
  public EntityMappingBuilder(SimpleTypeResolver simpleTypeResolver) {
    this.simpleTypeResolver = simpleTypeResolver;
    
  }
  
  public Map<ClassTypeInfo, EntityMapping> build(Set<ClassTypeInfo> classTypes) {

    // Clone for modification
    classTypes = new HashSet<ClassTypeInfo>(classTypes);

    // Build beans
    final AtomicReference<ClassTypeInfo> objectCTI = new AtomicReference<ClassTypeInfo>();
    
    Map<ClassTypeInfo, EntityInfo> beans = new EntityInfoBuilder(simpleTypeResolver).build(classTypes);

    // Create context
    Context ctx = new Context(new SimpleTypeResolver(), new HashSet<EntityInfo>(beans.values()));

    // Build mappings
    Map<EntityInfo, EntityMapping> beanMappings = ctx.build();

   
    //
    Map<ClassTypeInfo, EntityMapping> classTypeMappings = new HashMap<ClassTypeInfo, EntityMapping>();
    for (Map.Entry<EntityInfo, EntityMapping> beanMapping : beanMappings.entrySet()) {
      classTypeMappings.put(beanMapping.getKey().getClassType(), beanMapping.getValue());
    }

    //
    return classTypeMappings;
  }

  private class Context {

    /** . */
    final SimpleTypeResolver typeResolver;

    /** . */
    final Map<ClassTypeInfo, EntityInfo> beanClassTypeMap;

    /** . */
    final Set<EntityInfo> beans;

    /** . */
    final Map<EntityInfo, EntityMapping> beanMappings;

    private Context(SimpleTypeResolver typeResolver, Set<EntityInfo> beans) {

      //
      Map<ClassTypeInfo, EntityInfo> beanClassTypeMap = new HashMap<ClassTypeInfo, EntityInfo>();
      for (EntityInfo entity : beans) {
        beanClassTypeMap.put(entity.getClassType(), entity);
      }

      //
      this.typeResolver = typeResolver;
      this.beanClassTypeMap = beanClassTypeMap;
      this.beans = beans;
      this.beanMappings = new HashMap<EntityInfo, EntityMapping>();
    }

    public Map<EntityInfo, EntityMapping> build() {
      return null;
    }

    private EntityMapping resolve(ClassTypeInfo classType) {
     return null;
    }

    private EntityMapping resolve(BeanInfo bean) {
      
      return null;
    }

    private EntityMapping create(EntityInfo bean) {

      return null;
    }

    private void build(EntityMapping entityMapping) {

     
    }
  }
}
