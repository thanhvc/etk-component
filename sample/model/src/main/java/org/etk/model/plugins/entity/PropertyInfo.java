package org.etk.model.plugins.entity;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.introspection.AnnotationIntrospector;
import org.etk.reflect.api.introspection.AnnotationTarget;
import org.etk.reflect.core.AnnotationType;

public class PropertyInfo<V extends ValueInfo, K extends ValueKind> {

  /** The owner bean. */
  private final EntityInfo owner;

  /** The parent property. */
  private PropertyInfo parent;

  /** The property name. */
  private final String name;

  /** The the most adapter getter. */
  private final MethodInfo getter;

  /** The the most adapted setter. */
  private final MethodInfo setter;

  /** . */
  private final K valueKind;

  /** . */
  private final V value;

  PropertyInfo(
      EntityInfo owner,
      PropertyInfo parent,
      String name,
      MethodInfo getter,
      MethodInfo setter,
      K valueKind,
      V value)  throws NullPointerException, IllegalArgumentException {
    if (owner == null) {
      throw new NullPointerException("Owner cannot be null");
    }
    if (name == null) {
      throw new NullPointerException("Name cannot be null");
    }
    if (value == null) {
      throw new NullPointerException("Value cannot be null");
    }
    if (valueKind == null) {
      throw new NullPointerException("Value kind cannot be null");
    }
    if (getter == null && setter == null) {
      throw new IllegalArgumentException("Both setter and getter cannot be null");
    }

    //
    this.owner = owner;
    this.parent = parent;
    this.name = name;
    this.getter = getter;
    this.setter = setter;
    this.value = value;
    this.valueKind = valueKind;
  }

  public K getValueKind() {
    return valueKind;
  }

  public V getValue() {
    return value;
  }

  public EntityInfo getOwner() {
    return owner;
  }

  public PropertyInfo getParent() {
    return parent;
  }

  public String getName() {
    return name;
  }

  public MethodInfo getGetter() {
    return getter;
  }

  public MethodInfo getSetter() {
    return setter;
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

  public <A extends Annotation> A getAnnotation(Class<A> annotationClassType) {
    if (annotationClassType == null) {
      throw new NullPointerException();
    }

    //
    AnnotationTarget<MethodInfo, A> annotation = null;

    //
    AnnotationType<A, ?> annotationType = AnnotationType.get(annotationClassType);

    //
    if (getter != null) {
      annotation = new AnnotationIntrospector<A>(annotationType).resolve(getter);
    }

    //
    if (setter != null) {
      AnnotationTarget<MethodInfo, A> setterAnnotation = new AnnotationIntrospector<A>(annotationType).resolve(setter);
      if (setterAnnotation != null) {
        if (annotation != null) {
          throw new IllegalStateException("The same annotation " + annotation + " is present on a getter " +
            getter + " and setter" + setter);
        }
        annotation = setterAnnotation;
      }
    }

    //
    if (annotation != null) {
      return annotation.getAnnotation();
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    return "PropertyInfo[name=" + name + "]";
  }
}
