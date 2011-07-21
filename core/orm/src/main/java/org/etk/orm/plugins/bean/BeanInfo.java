package org.etk.orm.plugins.bean;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.introspection.AnnotationIntrospector;
import org.etk.reflect.api.introspection.AnnotationTarget;
import org.etk.reflect.core.AnnotationType;

public class BeanInfo {
  /** . */
  BeanInfo parent;

  /** . */
  final ClassTypeInfo classType;

  /** Declared means it was build by declaration and not by implicit resolution. */
  final boolean declared;

  /** . */
  final Map<String, PropertyInfo<?, ?>> properties;

  /** . */
  final Map<String, PropertyInfo<?, ?>> unmodifiableProperties;

  public BeanInfo(ClassTypeInfo classType, boolean declared) {
    this.classType = classType;
    this.declared = declared;
    this.properties = new HashMap<String, PropertyInfo<?, ?>>();
    this.unmodifiableProperties = Collections.unmodifiableMap(properties);
  }

  public BeanInfo getParent() {
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
    return Utils.resolveToClassType(classType, type);
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

  @Override
  public String toString() {
    return "BeanInfo[name=" + classType.getName() + "]";
  }
}
