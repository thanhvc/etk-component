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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etk.orm.plugins.bean.mapping.JLOTypeInfo;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.WildcardTypeInfo;
import org.etk.reflect.api.introspection.AnnotationIntrospector;
import org.etk.reflect.api.introspection.AnnotationTarget;
import org.etk.reflect.core.AnnotationType;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public class EntityInfo {

  /** . */
  EntityInfo parent;

  /** . */
  final ClassTypeInfo classType;

  /** Declared means it was build by declaration and not by implicit resolution. */
  final boolean declared;

  /** . */
  final Map<String, PropertyInfo<?, ?>> properties;

  /** . */
  final Map<String, PropertyInfo<?, ?>> unmodifiableProperties;

  public EntityInfo(ClassTypeInfo classType, boolean declared) {
    this.classType = classType;
    this.declared = declared;
    this.properties = new HashMap<String, PropertyInfo<?, ?>>();
    this.unmodifiableProperties = Collections.unmodifiableMap(properties);
  }

  public EntityInfo getParent() {
    return parent;
  }

  public ClassTypeInfo getClassType() {
    return classType;
  }

  public boolean isDeclared() {
    return declared;
  }

  public PropertyInfo<?, ?> getProperty(String name) {
    return properties.get(name);
  }

  public Map<String, PropertyInfo<?, ?>> getProperties() {
    return properties;
  }

  public Collection<? extends Annotation> getAnnotations(Class<? extends Annotation>... annotationClassTypes) {
    List<Annotation> props = new ArrayList<Annotation>();
    for (Class<? extends Annotation> annotationClassType : annotationClassTypes) {
      Annotation annotation = getAnnotation(annotationClassType);
      if (annotation != null) {
        props.add(annotation);
      }
    }
    return props;
  }

  public ClassTypeInfo resolveToClass(TypeInfo type) {
    return resolveToClassType(classType, type);
  }

  public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
    return getAnnotation(AnnotationType.get(annotationClass));
  }

  public <A> A getAnnotation(AnnotationType<A, ?> annotationType) {
    if (annotationType == null) {
      throw new NullPointerException();
    }
    AnnotationIntrospector<A> introspector = new AnnotationIntrospector<A>(annotationType);
    AnnotationTarget<ClassTypeInfo,A> annotationTarget = introspector.resolve(classType);
    return annotationTarget != null ? annotationTarget.getAnnotation() : null;
  }

  
  private ClassTypeInfo resolveToClassType(ClassTypeInfo baseType, TypeInfo type) {
    TypeInfo resolvedType = baseType.resolve(type);

    if (resolvedType instanceof ClassTypeInfo) {
      return (ClassTypeInfo) resolvedType;
    } else if (resolvedType instanceof TypeVariableInfo) {
      return resolveToClassType(baseType, ((TypeVariableInfo) resolvedType).getBounds().get(0));
    } else if (resolvedType instanceof WildcardTypeInfo) {
      WildcardTypeInfo wti = (WildcardTypeInfo) resolvedType;
      List<TypeInfo> bounds = wti.getUpperBounds();
      if (bounds.size() == 0) {
        bounds = wti.getLowerBounds();
      }
      if (bounds.size() == 0) {
        return JLOTypeInfo.get();
      } else {
        return resolveToClassType(baseType, bounds.get(0));
      }
    } else {
      return null;
    }
  }
  
  
  @Override
  public String toString() {
    return "EntityInfo[name=" + classType.getName() + "]";
  }
}
