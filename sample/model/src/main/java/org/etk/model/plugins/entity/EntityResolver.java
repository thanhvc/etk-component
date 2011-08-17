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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.etk.model.plugins.entity.type.SimpleTypeBinding;
import org.etk.model.plugins.entity.type.SimpleTypeResolver;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.reflect.api.ArrayTypeInfo;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.ParameterizedTypeInfo;
import org.etk.reflect.api.SimpleTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.VoidTypeInfo;
import org.etk.reflect.api.definition.ClassKind;
import org.etk.reflect.api.introspection.MethodIntrospector;
import org.etk.reflect.api.visit.HierarchyVisitor;
import org.etk.reflect.api.visit.HierarchyVisitorStrategy;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public class EntityResolver {

  private final SimpleTypeResolver simpleTypeResolver;

  public EntityResolver(SimpleTypeResolver simpleTypeResolver) {
    this.simpleTypeResolver = simpleTypeResolver;

  }
  
  
  public Map<ClassTypeInfo, EntityInfo> build(Set<ClassTypeInfo> classTypes) {
    Context ctx = new Context(classTypes);
    ctx.build();
    return ctx.entityInfos;
  }
  
  private class Context {

    private class EntityHierarchyVisitorStrategy<V extends HierarchyVisitor<V>> extends HierarchyVisitorStrategy<V> {

      /** . */
      private final ClassTypeInfo current;

      private EntityHierarchyVisitorStrategy(ClassTypeInfo current) {
        this.current = current;
      }

      @Override
      protected boolean accept(ClassTypeInfo type) {
        return type == current || !classTypes.contains(type);
      }
    }

    /** The types to build. */
    private final Set<ClassTypeInfo> classTypes;

    /** The beans being built in a method call to the builder. */
    private final Map<ClassTypeInfo, EntityInfo> entityInfos;

    private Context(Set<ClassTypeInfo> classTypes) {
      this.classTypes = classTypes;
      this.entityInfos = new HashMap<ClassTypeInfo, EntityInfo>();
    }

    void build() {
      while (true) {
        Iterator<ClassTypeInfo> iterator = classTypes.iterator();
        if (iterator.hasNext()) {
          ClassTypeInfo cti = iterator.next();
          EntityInfo bean = resolve(cti);
        } else {
          break;
        }
      }
    }

    /**
     * Resolve the bean object from the specified class type. The returned bean
     * is in correct state. However it can trigger recursive resolving while the current
     * bean is in an incorrect state, i.e not finished to be fully constructed.
     *
     * To ensure the fact that we have a unique bean created per class type we must relax
     * the fact that objects are created in one step, i.e have:
     *
     * <ol>
     * <li>Instantiate bean</li>
     * <li>Make bean available for lookup</li>
     * <li>Terminate bean initialization</li>
     * </ol>
     *
     * Note: it could be possible to use future object to build the full state and leverage
     * multi processors.
     *
     * @param classType the bean class type
     * @return the corresponding bean instance
     */
    EntityInfo resolve(ClassTypeInfo classType) {
      EntityInfo entity = entityInfos.get(classType);
      if (entity == null) {
        boolean accept;
        Boolean declared;

        if (classType.getKind() == ClassKind.CLASS || classType.getKind() == ClassKind.INTERFACE) {
          //isPrimitive Object LONG, DOUBLE, STRING, DATE ...
          if (classType instanceof SimpleTypeInfo) {
            accept = false;
            declared = null;
           //BeanMappingBuilder.println("rejected simple type " + classType.getName());
          } else if (classType instanceof VoidTypeInfo) {
            accept = false;
            declared = null;
            //BeanMappingBuilder.println("rejected void " + classType.getName());
          } else {
            if (classTypes.remove(classType)) {
              //log.debug("resolved declared " + classType.getName());
              accept = true;
              declared = true;
            } else {
              accept = false;
              declared = null;
              //BeanMappingBuilder.println("rejected " + classType.getName());
            }
          }
        } else {
          accept = false;
          declared = null;
          //BeanMappingBuilder.println("rejected non class or non interface " + classType.getName());
        }
        if (accept) {
          entity = new EntityInfo(classType, declared);
          entityInfos.put(classType, entity);
          build(entity);
        }
      }
      return entity;
    }

    void build(EntityInfo entityInfo) {

      // Build parents
      for (ClassTypeInfo ancestorClassType = entityInfo.classType.getSuperClass();
                            ancestorClassType != null;ancestorClassType = ancestorClassType.getSuperClass()) {

        // Resolve the ancestor class type
        EntityInfo ancestorBean = resolve(ancestorClassType);

        // If the ancestor resolves as a bean then it becomes the parent bean and we are done
        if (ancestorBean != null) {
          entityInfo.parent = ancestorBean;
          break;
        }
      }

      //
      buildProperties(entityInfo);

      // Now resolve types references by method return type
      // this is needed for @Create for instance
      for (MethodInfo mi : entityInfo.classType.getDeclaredMethods()) {
        TypeInfo rti = mi.getReturnType();
        if (rti instanceof ClassTypeInfo) {
          resolve((ClassTypeInfo)rti);
        }
      }
    }

    private PropertyInfo resolveProperty(EntityInfo bean, String propertyName) {

      // We may have null in case we were dealing with java.lang.Object for instance
      if (bean == null) {
        return null;
      }

      //
      if (bean.properties == null) {
        // Defensive: it means we are looking for a bean in an incorrect state
        throw new AssertionError();
      }

      //
      PropertyInfo property = bean.properties.get(propertyName);

      // Try in the parent
      if (property == null) {
        property = resolveProperty(bean.parent, propertyName);
      }

      //
      return property;
    }

    class ToBuild {
      final TypeInfo type;
      final MethodInfo getter;
      final MethodInfo setter;
      ToBuild(TypeInfo type, MethodInfo getter, MethodInfo setter) {
        this.type = type;
        this.getter = getter;
        this.setter = setter;
      }
    }

    /**
     * Build properties of a bean.
     *
     * @param entityInfo the bean to build properties.
     */
    private void buildProperties(EntityInfo entityInfo) {

      EntityHierarchyVisitorStrategy strategy = new EntityHierarchyVisitorStrategy(entityInfo.classType);
      MethodIntrospector introspector = new MethodIntrospector(strategy, true);
      Map<String, MethodInfo> getterMap = introspector.getGetterMap(entityInfo.classType);
      Map<String, Set<MethodInfo>> setterMap = introspector.getSetterMap(entityInfo.classType);

      // Gather all properties on the bean
      Map<String, ToBuild> toBuilds = new HashMap<String,ToBuild>();
      for (Map.Entry<String, MethodInfo> getterEntry : getterMap.entrySet()) {
        String name = getterEntry.getKey();
        MethodInfo getter = getterEntry.getValue();
        TypeInfo getterTypeInfo = getter.getReturnType();

        //
        ToBuild toBuild = null;
        Set<MethodInfo> setters = setterMap.get(name);
        if (setters != null) {
          for (MethodInfo setter : setters) {
            TypeInfo setterTypeInfo = setter.getParameterTypes().get(0);
            if (getterTypeInfo.equals(setterTypeInfo)) {
              toBuild = new ToBuild(getterTypeInfo, getter, setter);
              break;
            }
          }
        }

        //
        if (toBuild == null) {
          toBuild = new ToBuild(getterTypeInfo, getter, null);
        }

        //
        if (toBuild != null) {
          toBuilds.put(name, toBuild);
        }
      }

      //
      setterMap.keySet().removeAll(toBuilds.keySet());
      for (Map.Entry<String, Set<MethodInfo>> setterEntry : setterMap.entrySet()) {
        String name = setterEntry.getKey();
        for (MethodInfo setter : setterEntry.getValue()) {
          TypeInfo setterTypeInfo = setter.getParameterTypes().get(0);
          toBuilds.put(name, new ToBuild(setterTypeInfo, null, setter));
        }
      }

      // Now we have all the info to build each property correctly
      Map<String, PropertyInfo<?, ?>> properties = new HashMap<String, PropertyInfo<?, ?>>();
      for (Map.Entry<String, ToBuild> toBuildEntry : toBuilds.entrySet()) {

        // Get parent property if any
        PropertyInfo parentProperty = resolveProperty(entityInfo.parent, toBuildEntry.getKey());

        //
        TypeInfo type = toBuildEntry.getValue().type;

        // First resolve as much as we can
        TypeInfo resolvedType = entityInfo.classType.resolve(type);

        //
        PropertyInfo<?, ?> property = null;

        // We could not resolve it, get the upper bound
        if (resolvedType instanceof TypeVariableInfo) {
          resolvedType = ((TypeVariableInfo)resolvedType).getBounds().get(0);
          resolvedType = entityInfo.classType.resolve(resolvedType);
          // is it really enough ? for now it should be OK but we should check
        }

        // Now let's analyse
        if (resolvedType instanceof ParameterizedTypeInfo) {
          ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) resolvedType;
          TypeInfo rawType = parameterizedType.getRawType();
          if (rawType instanceof ClassTypeInfo) {
            ClassTypeInfo rawClassType = (ClassTypeInfo)rawType;
            String rawClassName = rawClassType.getName();
            final ValueKind.Multi collectionKind;
            final TypeInfo elementType;
            if (rawClassName.equals("java.util.Collection")) {
              collectionKind = ValueKind.COLLECTION;
              elementType = parameterizedType.getTypeArguments().get(0);
            } else if (rawClassName.equals("java.util.List")) {
              collectionKind = ValueKind.LIST;
              elementType = parameterizedType.getTypeArguments().get(0);
            } else if (rawClassName.equals("java.util.Map")) {
              TypeInfo keyType = parameterizedType.getTypeArguments().get(0);
              TypeInfo resolvedKeyType = entityInfo.classType.resolve(keyType);
              if (resolvedKeyType instanceof ClassTypeInfo && resolvedKeyType.getName().equals("java.lang.String")) {
                elementType = parameterizedType.getTypeArguments().get(1);
                collectionKind = ValueKind.MAP;
              } else {
                elementType = null;
                collectionKind = null;
              }
            } else {
              elementType = null;
              collectionKind = null;
            }
            if (collectionKind != null) {
              if (elementType instanceof ParameterizedTypeInfo) {
                ParameterizedTypeInfo parameterizedElementType = (ParameterizedTypeInfo)elementType;
                TypeInfo parameterizedElementRawType = parameterizedElementType.getRawType();
                if (parameterizedElementRawType instanceof ClassTypeInfo) {
                  ClassTypeInfo parameterizedElementRawClassType = (ClassTypeInfo)parameterizedElementRawType;
                  String parameterizedElementRawClassName = parameterizedElementRawClassType.getName();
                  if (parameterizedElementRawClassName.equals("java.util.List")) {
                    TypeInfo listElementType = parameterizedElementType.getTypeArguments().get(0);
                    property = new PropertyInfo<SimpleValueInfo, ValueKind.Multi>(
                        entityInfo,
                        parentProperty,
                        toBuildEntry.getKey(),
                        toBuildEntry.getValue().getter,
                        toBuildEntry.getValue().setter,
                        collectionKind,
                        createSimpleValueInfo(entityInfo, listElementType, ValueKind.LIST));
                  }
                }
              } else {
                ClassTypeInfo elementClassType = entityInfo.resolveToClass(elementType);
                if (elementClassType != null) {
                  EntityInfo relatedBean = resolve(elementClassType);
                  if (relatedBean != null) {
                    property = new PropertyInfo<EntityValueInfo, ValueKind.Multi>(
                        entityInfo,
                        parentProperty,
                        toBuildEntry.getKey(),
                        toBuildEntry.getValue().getter,
                        toBuildEntry.getValue().setter,
                        collectionKind,
                        new EntityValueInfo(type, entityInfo.resolveToClass(elementType), relatedBean));
                  } else {
                    if (collectionKind == ValueKind.LIST) {
                      property = new PropertyInfo<SimpleValueInfo, ValueKind.Single>(
                          entityInfo,
                          parentProperty,
                          toBuildEntry.getKey(),
                          toBuildEntry.getValue().getter,
                          toBuildEntry.getValue().setter,
                          ValueKind.SINGLE,
                          createSimpleValueInfo(entityInfo, elementType, collectionKind));
                    } else if (collectionKind == ValueKind.MAP) {
                      property = new PropertyInfo<SimpleValueInfo, ValueKind.Map>(
                          entityInfo,
                          parentProperty,
                          toBuildEntry.getKey(),
                          toBuildEntry.getValue().getter,
                          toBuildEntry.getValue().setter,
                          ValueKind.MAP,
                          createSimpleValueInfo(entityInfo, elementType, ValueKind.SINGLE));
                    }
                  }
                }
              }
            }
          }
        } else if (resolvedType instanceof ArrayTypeInfo) {
          final TypeInfo componentType = ((ArrayTypeInfo)resolvedType).getComponentType();
          if (componentType instanceof SimpleTypeInfo) {
            SimpleTypeInfo componentSimpleType = (SimpleTypeInfo)componentType;
            switch (componentSimpleType.getLiteralType()) {
              case BOOLEAN:
              case DOUBLE:
              case FLOAT:
              case LONG:
              case INT:
                property = new PropertyInfo<SimpleValueInfo, ValueKind.Single>(
                    entityInfo,
                    parentProperty,
                    toBuildEntry.getKey(),
                    toBuildEntry.getValue().getter,
                    toBuildEntry.getValue().setter,
                    ValueKind.SINGLE,
                    createSimpleValueInfo(entityInfo, componentType, ValueKind.ARRAY));
                break;
              default:
                break;
            }
          } else {
            property = new PropertyInfo<SimpleValueInfo, ValueKind.Single>(
                entityInfo,
                parentProperty,
                toBuildEntry.getKey(),
                toBuildEntry.getValue().getter,
                toBuildEntry.getValue().setter,
                ValueKind.SINGLE,
                createSimpleValueInfo(entityInfo, componentType, ValueKind.ARRAY));
          }
        } else if (resolvedType instanceof ClassTypeInfo) {
          EntityInfo related = resolve((ClassTypeInfo)resolvedType);
          if (related != null) {
            property = new PropertyInfo<EntityValueInfo, ValueKind.Single>(
                entityInfo,
                parentProperty,
                toBuildEntry.getKey(),
                toBuildEntry.getValue().getter,
                toBuildEntry.getValue().setter,
                ValueKind.SINGLE,
                new EntityValueInfo(type, entityInfo.resolveToClass(type), related));
          }
        }

        // Otherwise consider everything as a single valued simple value
        if (property == null) {

          property = new PropertyInfo<SimpleValueInfo, ValueKind.Single>(
              entityInfo,
              parentProperty,
              toBuildEntry.getKey(),
              toBuildEntry.getValue().getter,
              toBuildEntry.getValue().setter,
              ValueKind.SINGLE,
              createSimpleValueInfo(entityInfo, type, ValueKind.SINGLE));
        }

        //
        properties.put(property.getName(), property);
      }

      // Update properties
      entityInfo.properties.putAll(properties);
    }

    private <K extends ValueKind> SimpleValueInfo createSimpleValueInfo(EntityInfo bean, TypeInfo type, K valueKind) {
      TypeInfo resolvedType = bean.getClassType().resolve(type);
      SimpleTypeBinding binding = simpleTypeResolver.resolveType(resolvedType);
      return new SimpleValueInfo<K>(type, resolvedType, binding, valueKind);
    }
  }
}
