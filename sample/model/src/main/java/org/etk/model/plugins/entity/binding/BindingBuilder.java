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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.etk.model.api.annotations.Entity;
import org.etk.model.api.annotations.Method;
import org.etk.model.api.annotations.Property;
import org.etk.model.plugins.entity.EntityInfo;
import org.etk.model.plugins.entity.EntityResolver;
import org.etk.model.plugins.entity.PropertyInfo;
import org.etk.model.plugins.entity.SimpleValueInfo;
import org.etk.model.plugins.entity.type.SimpleTypeBinding;
import org.etk.model.plugins.entity.type.SimpleTypeResolver;
import org.etk.model.plugins.json.PropertyDefinitionMapping;
import org.etk.model.plugins.vt2.PropertyMetaType;
import org.etk.orm.api.annotations.DefaultValue;
import org.etk.orm.api.annotations.NamingPrefix;
import org.etk.orm.api.annotations.Properties;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.InvalidMappingException;
import org.etk.orm.plugins.bean.mapping.MethodMapping;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.api.introspection.AnnotationTarget;
import org.etk.reflect.api.introspection.MethodIntrospector;
import org.etk.reflect.api.visit.HierarchyScope;
import org.etk.reflect.core.AnnotationType;
import org.etk.reflect.core.TypeResolverImpl;
import org.etk.reflect.jlr.metadata.JLReflectionMetadata;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public class BindingBuilder {

  /** Used for receiving {@code java.lang.Object} */
  private final TypeResolver<Type> domain = TypeResolverImpl.create(JLReflectionMetadata.newInstance());
  
  private final SimpleTypeResolver simpleTypeResolver;
  
  public BindingBuilder() {
    this(new SimpleTypeResolver());
  }
  
  public BindingBuilder(SimpleTypeResolver simpleTypeResolver) {
    this.simpleTypeResolver = simpleTypeResolver;
    
  }
  
  public Map<ClassTypeInfo, EntityBinding> build(Set<ClassTypeInfo> classTypes) {

    // Clone for modification
    classTypes = new HashSet<ClassTypeInfo>(classTypes);

   
    Map<ClassTypeInfo, EntityInfo> entityMap = new EntityResolver(simpleTypeResolver).build(classTypes);

    // Create context to resolve the EntityMapping
    Context ctx = new Context(new SimpleTypeResolver(), new HashSet<EntityInfo>(entityMap.values()));

    // Build mappings
    Map<EntityInfo, EntityBinding> beanMappings = ctx.build();

   
    //
    Map<ClassTypeInfo, EntityBinding> classTypeBindings = new HashMap<ClassTypeInfo, EntityBinding>();
    
    for (Map.Entry<EntityInfo, EntityBinding> beanBinding : beanMappings.entrySet()) {
      classTypeBindings.put(beanBinding.getKey().getClassType(), beanBinding.getValue());
    }

    //
    return classTypeBindings;
  }

  private class Context {

    /** . */
    final SimpleTypeResolver typeResolver;

    /** . */
    final Map<ClassTypeInfo, EntityInfo> beanClassTypeMap;

    /** . */
    final Set<EntityInfo> entityList;

    /** . */
    final Map<EntityInfo, EntityBinding> entityMappings;

    private Context(SimpleTypeResolver typeResolver, Set<EntityInfo> entityList) {

      //
      Map<ClassTypeInfo, EntityInfo> beanClassTypeMap = new HashMap<ClassTypeInfo, EntityInfo>();
      for (EntityInfo entity : entityList) {
        beanClassTypeMap.put(entity.getClassType(), entity);
      }

      //
      this.typeResolver = typeResolver;
      this.beanClassTypeMap = beanClassTypeMap;
      this.entityList = entityList;
      this.entityMappings = new HashMap<EntityInfo, EntityBinding>();
    }

    public Map<EntityInfo, EntityBinding> build() {
      while (true) {
        Iterator<EntityInfo> iterator = entityList.iterator();
        if (iterator.hasNext()) {
          EntityInfo entityInfo = iterator.next();
          resolve(entityInfo);
        } else {
          return entityMappings;
        }
      }
    }

    /**
     * Executes to resolve each EntityInfo and return the EntityBinding.
     *  
     * @param entityInfo
     * @return
     */
    private EntityBinding resolve(EntityInfo entityInfo) {
      EntityBinding mapping = entityMappings.get(entityInfo);
      if (mapping == null) {
        //removes the entityInfo in entityList 
        if (entityList.remove(entityInfo)) {
          //create the EntityMapping here
          mapping = create(entityInfo);
          entityMappings.put(entityInfo, mapping);
          build(mapping);
        } else {
          // It does not resolve
        }
      }
      return mapping;
    }

    /**
     * Resolves the parent of current ClassTypeInfo
     * @param entityInfo
     * @return
     */
    private EntityBinding create(EntityInfo entityInfo) {

      Collection<? extends Annotation> annotations = entityInfo.getAnnotations(Entity.class);
      if (annotations.size() != 1) {
        throw new InvalidMappingException(entityInfo.getClassType(), "Class is not annotated with an Entity type");
      }
      
      //
      NamingPrefix namingPrefix = entityInfo.getAnnotation(NamingPrefix.class);
      String prefix = null;
      if (namingPrefix != null) {
        prefix = namingPrefix.value();
      }
      
      Annotation mappingAnnotation = annotations.iterator().next();
      Entity typeAnnotation = (Entity)mappingAnnotation;
      EntityTypeKind entityTypeKind = EntityTypeKind.ENTITY;
      String entityTypeName = typeAnnotation.name();
      boolean abstract_ = typeAnnotation.abstract_();
      
      return new EntityBinding(entityInfo, entityTypeKind, entityTypeName, abstract_, prefix);
    }

    
    private void build(EntityBinding entityMapping) {

      EntityInfo entityInfo = entityMapping.getEntity();

      // First build the parent mapping if any
      if (entityInfo.getParent() != null) {
        entityMapping.parent = resolve(entityInfo.getParent());
      }

      //
      Map<String, PropertyBinding<?, ?, ?>> properties = new HashMap<String, PropertyBinding<?, ?, ?>>();

      for (PropertyInfo<?, ?> property : entityInfo.getProperties().values()) {

        // Determine kind
        Collection<? extends Annotation> annotations = property.getAnnotations(Property.class);

        //
        if (annotations.size() > 1) {
          throw new InvalidMappingException(entityInfo.getClassType(), "The property " + property
              + " declares too many annotations " + annotations);
        }

        // Build the correct mapping or fail
        PropertyBinding<?, ?, ?> mapping = null;
        if (annotations.size() == 1) {
          Annotation annotation = annotations.iterator().next();
          ValueInfo value = property.getValue();
          if (property.getValueKind() == ValueKind.SINGLE) {
            if (value instanceof SimpleValueInfo<?>) {
              SimpleValueInfo<?> simpleValue = (SimpleValueInfo<?>) value;
              if (annotation instanceof Property) {
                Property propertyAnnotation = (Property) annotation;
                if (simpleValue.getValueKind() instanceof ValueKind.Single) {
                  PropertyInfo<SimpleValueInfo<ValueKind.Single>, ValueKind.Single> propertyInfo = (PropertyInfo<SimpleValueInfo<ValueKind.Single>, ValueKind.Single>) property;
                  mapping = createValueMapping(propertyAnnotation, propertyInfo);
                } else {
                  PropertyInfo<SimpleValueInfo<ValueKind.Multi>, ValueKind.Single> a = (PropertyInfo<SimpleValueInfo<ValueKind.Multi>, ValueKind.Single>) property;
                  mapping = createValueMapping(propertyAnnotation, a);
                }
              } else {
                throw new InvalidMappingException(entityInfo.getClassType(), "The property "
                    + property + " is not annotated");
              }
            } else {
              throw new AssertionError();
            }
          } else if (property.getValueKind() instanceof ValueKind.Multi) {
            if (value instanceof SimpleValueInfo) {
              SimpleValueInfo<?> simpleValue = (SimpleValueInfo<?>) value;
              if (annotation instanceof Property) {
                Property propertyAnnotation = (Property) annotation;
                if (simpleValue.getValueKind() instanceof ValueKind.Single) {
                  PropertyInfo<SimpleValueInfo<ValueKind.Single>, ValueKind.Single> a = (PropertyInfo<SimpleValueInfo<ValueKind.Single>, ValueKind.Single>) property;
                  mapping = createValueMapping(propertyAnnotation, a);
                } else {
                  PropertyInfo<SimpleValueInfo<ValueKind.Multi>, ValueKind.Single> a = (PropertyInfo<SimpleValueInfo<ValueKind.Multi>, ValueKind.Single>) property;
                  mapping = createValueMapping(propertyAnnotation, a);
                }
              } else if (annotation instanceof Properties) {
                mapping = createProperties((PropertyInfo<?, ValueKind.Map>) property);
              } else {
                throw new InvalidMappingException(entityInfo.getClassType(), "Annotation "
                    + annotation + " is forbidden " + " on property " + property);
              }
            } else if (value instanceof BeanValueInfo) {
              if (annotation instanceof Properties) {
                mapping = createProperties((PropertyInfo<?, ValueKind.Map>) property);
              } else {
                throw new InvalidMappingException(entityInfo.getClassType(), "Annotation "
                    + annotation + " is forbidden " + " on property " + property);
              }
            } else {
              throw new AssertionError();
            }
          } else {
            throw new AssertionError();
          }
        }

        //
        if (mapping != null) {

          // Resolve parent property without any check for now
          PropertyInfo parentProperty = property.getParent();
          if (parentProperty != null) {
            EntityInfo ancestor = parentProperty.getOwner();
            EntityBinding ancestorMapping = resolve(ancestor);
            mapping.parent = ancestorMapping.properties.get(parentProperty.getName());
          }

          //
          properties.put(mapping.property.getName(), mapping);
        }
      }

      // Wire
      entityMapping.properties.putAll(properties);
      for (PropertyBinding<?, ?, ?> propertyMapping : entityMapping.properties.values()) {
        propertyMapping.owner = entityMapping;
      }

      // Take care of methods
      MethodIntrospector introspector = new MethodIntrospector(HierarchyScope.ALL);
      Set<MethodBinding> methodMappings = new HashSet<MethodBinding>();
      Collection<AnnotationTarget<MethodInfo, Method>> methodInfos = introspector.resolveMethods(entityInfo.getClassType(), AnnotationType.get(Method.class));
      
      for(AnnotationTarget<MethodInfo, Method> method : methodInfos) {
        methodMappings.add(new MethodBinding(method.getTarget()));
      }
      //
      entityMapping.methods.addAll(methodMappings);
    }
    
    private <K extends ValueKind> ValueBinding<K> createValueMapping(Property propertyAnnotation,
                                                                     PropertyInfo<SimpleValueInfo<K>, ValueKind.Single> property) {

      //
      PropertyMetaType<?> propertyMetaType = PropertyMetaType.get(propertyAnnotation.type());

      //
      SimpleTypeBinding resolved = typeResolver.resolveType(property.getValue().getDeclaredType(),
                                                            propertyMetaType);
      if (resolved == null) {
        throw new InvalidMappingException(property.getOwner().getClassType(),
                                          "No simple type mapping "
                                              + property.getValue().getDeclaredType()
                                              + " for property " + property);
      }

      //
      List<String> defaultValueList = null;
      DefaultValue defaultValueAnnotation = property.getAnnotation(DefaultValue.class);
      if (defaultValueAnnotation != null) {
        String[] defaultValues = defaultValueAnnotation.value();
        defaultValueList = new ArrayList<String>(defaultValues.length);
        defaultValueList.addAll(Arrays.asList(defaultValues));
        defaultValueList = Collections.unmodifiableList(defaultValueList);
      }

      //
      PropertyDefinitionMapping<?> propertyDefinition = new PropertyDefinitionMapping(propertyAnnotation.name(),
                                                                                      resolved.getPropertyMetaType(),
                                                                                      defaultValueList,
                                                                                      false);

      //
      return new ValueBinding<K>(property, propertyDefinition);
    }
    
    
    
    private <V extends ValueInfo> PropertiesBinding<V> createProperties(PropertyInfo<V, ValueKind.Map> property) {
      if (property.getValueKind() != ValueKind.MAP) {
        throw new InvalidMappingException(property.getOwner().getClassType(), "The @Properties " + property +
            " must be of type java.util.Map instead of " + property.getValue().getEffectiveType());
      }
      TypeInfo type = property.getValue().getEffectiveType();

      //
      PropertyMetaType<?> mt = null;
      ValueKind valueKind;
      ValueInfo vi = property.getValue();
      if (vi instanceof SimpleValueInfo<?>) {
        SimpleValueInfo<?> svi = (SimpleValueInfo<?>)vi;
        if (svi.getTypeMapping() != null) {
          mt = svi.getTypeMapping().getPropertyMetaType();
        }
        valueKind = svi.getValueKind();
      } else {
        if (type.getName().equals(Object.class.getName())) {
          mt = null;
        }
        valueKind = ValueKind.SINGLE;
      }

      //
      String prefix = null;
      NamingPrefix namingPrefix = property.getAnnotation(NamingPrefix.class);
      if (namingPrefix != null) {
        prefix = namingPrefix.value();
      }

      //
      return new PropertiesBinding<V>(property, prefix, mt, valueKind);
    }

  }
  
}
