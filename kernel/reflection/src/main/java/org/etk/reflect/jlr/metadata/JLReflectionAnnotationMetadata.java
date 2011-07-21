package org.etk.reflect.jlr.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.etk.reflect.api.metadata.AnnotationMetadata;

public abstract class JLReflectionAnnotationMetadata<E> implements AnnotationMetadata<Type, E, Annotation, Method> {
  
  public Collection<Method> getAnnotationParameters(Annotation annotation) {
    Method[] methods = annotation.annotationType().getDeclaredMethods();
    ArrayList<Method> list = new ArrayList<Method>(methods.length);
    for (Method m : methods) {
      list.add(m);
    }
    return list;
  }

  public Type getAnnotationType(Annotation annotation) {
    return annotation.annotationType();
  }

  public String getAnnotationParameterName(Method parameter) {
    return parameter.getName();
  }

  public Type getAnnotationParameterType(Method parameter) {
    return parameter.getGenericReturnType();
  }

  public List<?> getAnnotationParameterValue(Annotation annotation, Method parameter) {
    try {
      Object value = parameter.invoke(annotation);
      if (value.getClass().isArray()) {
        Object[] array = (Object[])value;
        for (int i = 0;i < array.length;i++) {
          array[i] = unwrap(array[i]);
        }
        return Arrays.asList(array);
      } else {
        return Arrays.asList(unwrap(value));
      }
    }
    catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  private Object unwrap(Object o) {
    if (o instanceof Enum) {
      return ((Enum)o).name();
    } else {
      return o;
    }
  }
}
