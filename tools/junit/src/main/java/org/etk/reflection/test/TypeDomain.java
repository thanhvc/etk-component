package org.etk.reflection.test;

import java.util.Map;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.core.AnnotationType;


public class TypeDomain<T> {

  /** . */
  private TypeResolver<T> domain;

  /** . */
  private Map<String, T> types;

  public TypeDomain(TypeResolver<T> domain, Map<String, T> types) {
    this.domain = domain;
    this.types = types;
  }

  protected final TypeInfo getTypeInfo(String typeId) {
    T type = types.get(typeId);
    if (type == null) {
      throw new IllegalArgumentException("Could not find type " + typeId);
    }
    return getInfo(type);
  }

  private TypeInfo getInfo(T type) {
    return domain.resolve(type);
  }

  public MethodInfo getMethodInfo(String id) {
    for (T type : types.values()) {
      TypeInfo ti = domain.resolve(type);
      if (ti instanceof ClassTypeInfo) {
        for (MethodInfo mi : ((ClassTypeInfo)ti).getDeclaredMethods()) {
          org.etk.reflection.test.Method methodAnnocation = mi.getDeclaredAnnotation(AnnotationType.get(org.etk.reflection.test.Method.class));
          if (methodAnnocation != null && methodAnnocation.value().equals(id)) {
            return mi;
          }
        }
      }
    }
    return null;
  }
}